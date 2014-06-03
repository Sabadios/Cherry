/*******************************************************************************
 * Copyright (c) 2013-2014 Cherry Platform
 *
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL
 * 1.0 (the "Licenses"). You can select the license that you prefer but you may
 * not use this file except in compliance with one of these Licenses.
 *
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 *
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0
 *
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1
 *
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1
 *
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 *
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 *
 *
 * Cherry Platform is a registered trademark of Sabadios
 *
 * Contributors:
 * Cristian Malinescu - initial design, API and implementation
 *******************************************************************************/
package org.Cherry.Modules.Web.Engine;

import static org.Cherry.Modules.Web.Engine.HttpContextKey.Authenticated;
import static org.Cherry.Modules.Web.Engine.HttpContextKey.CreateCookie;
import static org.Cherry.Modules.Web.HttpHeaderKey.Cookie;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Configuration.ConfigurationService;
import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.Jackson.Middleware.ObjectMapperService;
import org.Cherry.Modules.Security.Middleware.UserRepository;
import org.Cherry.Modules.Security.Model.User;
import org.Cherry.Modules.Web.Engine.InvocationContext.Key;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;

@Singleton
class RequestInterceptor extends ServiceTemplate implements HttpRequestInterceptor {
  /*
   * (non-Javadoc)
   * 
   * @see org.apache.http.HttpRequestInterceptor#process(org.apache.http.HttpRequest, org.apache.http.protocol.HttpContext)
   */
  @Override
  public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException, InvalidCredentialsException {
    log.debug("Intercepted request [{}] with URI [{}] for context [{}].", request, request.getRequestLine().getUri(), context);

    final Boolean authenticated = authenticate(request, context);

    context.setAttribute(Authenticated, authenticated);
    Context.instance().getInvocationContext().set(Key.Authenticated, authenticated);

    if (request instanceof BasicHttpRequest) if (isDebugEnabled())
      getTracerService().examine(request);
  }

  static final String openGateRESTCmd = "/gate/open";

  private Boolean authenticate(final HttpRequest request, final HttpContext context) throws InvalidCredentialsException {
    context.setAttribute(CreateCookie, false);

    if (isOpenGateMessage(request)) {
      final User identity = getUser(request);

      if (null == authenticate(identity)) {
        context.setAttribute(CreateCookie, true);
        return true;
      }

      return false;
    }

    if (isCookiePathHit(request)) {
      final WeakReference<HeaderIterator> headerIterator = new WeakReference<HeaderIterator>(request.headerIterator(Cookie));

      Header header;
      HeaderElement[] elements;
      String value;

      while (headerIterator.get().hasNext()) {
        header = headerIterator.get().nextHeader();

        elements = header.getElements();

        for (final HeaderElement element : elements)
          if (getSessionCookie().equalsIgnoreCase(element.getName())) {
            value = element.getValue();

            if (!isTampered(value))
              return true;
            else return false;
          }
      }

      return false;
    }

    throw new IllegalStateException("Unknown execution context!");
  }

  protected User getUser(final HttpRequest request) {
    User result = null;

    if (request instanceof HttpEntityEnclosingRequest) {
      final HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();

      final long contentLength = entity.getContentLength();

      if (0 < contentLength) {
        WeakReference<InputStream> is;

        try {
          is = new WeakReference<InputStream>(entity.getContent());
        } catch (IllegalStateException | IOException err) {
          throw new IllegalStateException(err);
        }

        try {
          result = getObjectMapperService().readValue(is.get(), User.class);
        } catch (final IOException err) {
          throw new IllegalStateException(err);
        }
      } else warn("{}", "Undefined content/length 0! Did the sender missed it?!");
    } else throw new IllegalArgumentException("Expected request of type [" + HttpEntityEnclosingRequest.class + "] but found [" + request.getClass() + "]");

    return result;
  }

  private Boolean isOpenGateMessage(final HttpRequest request) {
    return openGateRESTCmd.equalsIgnoreCase(request.getRequestLine().getUri());
  }

  private Boolean isCookiePathHit(final HttpRequest request) {
    if (null == cookiePathRegEx)
      cookiePathRegEx = new StringBuilder(getSessionCookiePath()).append(REGEX_PATTERN).toString();

    return request.getRequestLine().getUri().matches(cookiePathRegEx);
  }

  private String cookiePathRegEx = null;

  private static final String REGEX_PATTERN = ".*";

  private Boolean isTampered(final String value) {
    return false;
  }

  private InvalidCredentialsException authenticate(final User user) {
    return getUserRepository().authenticate(user);
  }

  private String getSessionCookiePath() {
    return getConfigurationService().getCookiePath();
  }

  private String getSessionCookie() {
    return getConfigurationService().getSessionCookie();
  }

  private ConfigurationService getConfigurationService() {
    assert null != _configurationService;
    return _configurationService;
  }

  private TracerService getTracerService() {
    assert null != _tracerService;
    return _tracerService;
  }

  private UserRepository getUserRepository() {
    assert null != _userRepository;
    return _userRepository;
  }

  protected ObjectMapperService getObjectMapperService() {
    assert null != _objectMapperService;
    return _objectMapperService;
  }

  @Inject
  @Singleton
  private ObjectMapperService _objectMapperService;

  @Inject
  @Singleton
  private ConfigurationService _configurationService;

  @Inject
  @Singleton
  private TracerService _tracerService;

  @Inject
  @Singleton
  private UserRepository _userRepository;

  @PostConstruct
  void postConstruct() {
  }

  @PreDestroy
  void preDestroy() {
  }

  public RequestInterceptor() {
  }

  static private final Logger log = Loggers.Intercept.getLogger();

  private static final long serialVersionUID = 1L;
}
