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

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Configuration.ConfigurationService;
import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.Hazelcast.Middleware.HazelcastService;
import org.slf4j.Logger;

import com.hazelcast.core.IMap;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
class SessionManager extends ServiceTemplate {
  Boolean isActive(final UUID jSessionID) {
    return getHazelcastService().getMap(getSessionCookie()).containsKey(jSessionID);
  }

  Boolean isExpired(final UUID jSessionID) {
    return !getHazelcastService().getMap(getSessionCookie()).containsKey(jSessionID);
  }

  Session<?, ?> getSession() {
    final IMap<Object, Object> sessions = getHazelcastService().getMap(getSessionCookie());

    final Session<? extends Serializable, ? extends Serializable> session = new Session<>();
    sessions.put(session.getJSessionID(), session, getSessionCookieTTL(), TimeUnit.MINUTES);

    log.debug("Session [{}] created!", session.toString());

    return (Session<?, ?>) sessions.get(session.getJSessionID());
  }

  Session<? extends Serializable, ? extends Serializable> getSession(final UUID jSessionID) {
    return (Session<?, ?>) getHazelcastService().getMap(getSessionCookie()).get(jSessionID);
  }

  void putSession(final String key, final Session<? extends Serializable, ? extends Serializable> session) {
    final IMap<Object, Object> sessions = getHazelcastService().getMap(key);

    sessions.put(session.getJSessionID(), session);

    log.debug("System Session [{}] created!", sessions.get(session.getJSessionID()));
  }

  private ConfigurationService getConfigurationService() {
    assert null != _configurationService;
    return _configurationService;
  }

  private String getSessionCookie() {
    return getConfigurationService().getSessionCookie();
  }

  private Long getSessionCookieTTL() {
    return getConfigurationService().getCookieTimeToLive();
  }

  private HazelcastService getHazelcastService() {
    assert null != _hazelcastService;
    return _hazelcastService;
  }

  @Inject
  @Singleton
  private HazelcastService _hazelcastService;

  @Inject
  @Singleton
  private ConfigurationService _configurationService;

  /**
   *
   */
  public SessionManager() {
  }

  private static final Logger log = Loggers.Session.getLogger();

  private static final long serialVersionUID = 1L;
}
