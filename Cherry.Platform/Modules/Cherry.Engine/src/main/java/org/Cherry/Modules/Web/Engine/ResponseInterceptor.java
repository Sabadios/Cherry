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
import static org.Cherry.Modules.Web.HttpHeaderKey.Set_Cookie;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Configuration.ConfigurationService;
import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.Web.BasicServerCookie;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;
import org.joda.time.DateTime;
import org.slf4j.Logger;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
class ResponseInterceptor extends ServiceTemplate implements HttpResponseInterceptor {
  /*
   * (non-Javadoc)
   * 
   * @see org.apache.http.HttpResponseInterceptor#process(org.apache.http.HttpResponse, org.apache.http.protocol.HttpContext)
   */
  @Override
  public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
    log.debug("Intercepted response [{}] for context [{}].", response, context);

    if (authenticated(context))
      if (createCookie(context)) {
        final WeakReference<BasicServerCookie> cookie = new WeakReference<BasicServerCookie>(new BasicServerCookie(getSessionCookie(),
            getServerSessions() ? getSession().toString() : UUID.randomUUID().toString()));

        cookie.get().setDomain(getSessionCookieDomain());
        cookie.get().setPath(getSessionCookiePath());
        cookie.get().setVersion(getSessionCookieVersion());
        cookie.get().setSecure(true);

        final Date expiryDate = new DateTime().plusMinutes(getSessionCookieTimeToLive().intValue()).toDate();

        cookie.get().setExpiryDate(expiryDate);

        response.addHeader(Set_Cookie, cookie.get().toString());
      }
  }

  private Boolean authenticated(final HttpContext context) {
    final Object authenticated = context.getAttribute(Authenticated);

    return null == authenticated ? false : (Boolean) authenticated;
  }

  private Boolean createCookie(final HttpContext context) {
    final Object createCookie = context.getAttribute(CreateCookie);

    return null == createCookie ? false : (Boolean) createCookie;
  }

  private String getSessionCookiePath() {
    return getConfigurationService().getCookiePath();
  }

  private String getSessionCookieDomain() {
    return getConfigurationService().getCookieDomain();
  }

  private Integer getSessionCookieVersion() {
    return getConfigurationService().getCookieVersion();
  }

  private Long getSessionCookieTimeToLive() {
    return getConfigurationService().getCookieTimeToLive();
  }

  private String getSessionCookie() {
    return getConfigurationService().getSessionCookie();
  }

  public Boolean getServerSessions() {
    return getConfigurationService().getServerSessions();
  }

  private ConfigurationService getConfigurationService() {
    assert null != _configurationService;
    return _configurationService;
  }

  private Session<? extends Serializable, ? extends Serializable> getSession() {
    return getSessionManagerService().getSession();
  }

  private SessionManager getSessionManagerService() {
    assert null != _sessionManager;
    return _sessionManager;
  }

  @Inject
  @Singleton
  private SessionManager _sessionManager;

  @Inject
  @Singleton
  private ConfigurationService _configurationService;

  @PostConstruct
  void postConstruct() {
  }

  @PreDestroy
  void preDestroy() {
  }

  public ResponseInterceptor() {
  }

  static private final Logger log = Loggers.Intercept.getLogger();

  private static final long serialVersionUID = 1L;
}
