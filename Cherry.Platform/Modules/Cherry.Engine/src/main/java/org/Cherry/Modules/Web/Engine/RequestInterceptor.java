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

import static org.Cherry.Modules.Web.Engine.HttpContextKey.Session_Cookie_Path_Hit;
import static org.Cherry.Modules.Web.Engine.HttpContextKey.Session_Cookie_Present;
import static org.Cherry.Modules.Web.HttpHeaderKey.Cookie;

import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Configuration.ConfigurationService;
import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.Web.TamperedCookieException;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
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
  public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
    log.debug("Intercepted request [{}] with URI [{}] for context [{}].", request, request.getRequestLine().getUri(), context);

    if (request instanceof BasicHttpRequest) {
      final BasicHttpRequest bRequest = (BasicHttpRequest) request;

      if (isDebugEnabled())
        getTracerService().examine(bRequest);

      if (bRequest.getRequestLine().getUri().equals(getSessionCookiePath())) {
        context.setAttribute(Session_Cookie_Path_Hit, Boolean.TRUE);

        final WeakReference<HeaderIterator> headerIterator = new WeakReference<HeaderIterator>(bRequest.headerIterator(Cookie));

        Header header;
        HeaderElement[] elements;
        String value;

        while (headerIterator.get().hasNext()) {
          header = headerIterator.get().nextHeader();

          elements = header.getElements();

          for (final HeaderElement element : elements)
            if (getSessionCookie().equalsIgnoreCase(element.getName())) {
              value = element.getValue();

              if (isTampered(value))
                throw new TamperedCookieException();
              else context.setAttribute(Session_Cookie_Present, Boolean.TRUE);

              return;
            }
        }
      } else context.setAttribute(Session_Cookie_Path_Hit, Boolean.FALSE);
    }

    context.setAttribute(Session_Cookie_Present, Boolean.FALSE);
  }

  private Boolean isTampered(final String value) {
    return false;
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

  @Inject
  @Singleton
  private ConfigurationService _configurationService;

  @Inject
  @Singleton
  private TracerService _tracerService;

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
