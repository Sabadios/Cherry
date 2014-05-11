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

import static org.Cherry.Environment.getInstanceID;
import static org.Cherry.Environment.Properties.INSTANCE_ID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Cherry;
import org.Cherry.Core.ServiceTemplate;
import org.jboss.weld.environment.se.events.ContainerInitialized;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
class EnvironmentManager extends ServiceTemplate {
  private SessionManager getSessionManagerService() {
    if (null == _sessionManager) {
      final SessionManager stub = Cherry.instance().getBean(SessionManager.class);

      if (null == stub)
        throw new IllegalStateException("Couldn't load 'SessionManager'");

      return stub;
    }

    return _sessionManager;
  }

  void containerInitialized(@Observes final ContainerInitialized event) {
    assert null != getSessionManagerService();

    getSessionManagerService().putSession(INSTANCE_ID, new SystemSession<>(getInstanceID()));
  }

  @Inject
  @Singleton
  private SessionManager _sessionManager;

  @PostConstruct
  void postConstruct() {
  }

  @PreDestroy
  void preDestroy() {

  }

  EnvironmentManager() {
  }

  private static final long serialVersionUID = 1L;
}
