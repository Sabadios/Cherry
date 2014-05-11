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

import static org.Cherry.Main.ApplicationKey.JSON_AGENTS_DICTIONARY;
import static org.Cherry.Utils.Utils.failIfEmpty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Main.ApplicationRepositoryService;
import org.Cherry.Modules.JSON.Agents.JSONAgentDefinition;
import org.Cherry.Modules.JSON.Agents.JSONAgentsDictionary;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
public class ControllersManager extends ServiceTemplate {
  public JSONAgentDefinition getController(final String uri) {
    failIfEmpty(uri);

    return getJSONAgentsDictionary().get(uri);
  }

  private JSONAgentsDictionary getJSONAgentsDictionary() {
    return (JSONAgentsDictionary) getApplicationRepositoryService().get(JSON_AGENTS_DICTIONARY).getValue();
  }

  private ApplicationRepositoryService getApplicationRepositoryService() {
    assert null != _applicationRepositoryService;
    return _applicationRepositoryService;
  }

  @Inject
  @Singleton
  ApplicationRepositoryService _applicationRepositoryService;

  @PostConstruct
  void postConstruct() {

  }

  @PreDestroy
  void preDestroy() {
  }

  /**
   *
   */
  public ControllersManager() {
  }

  /**
   *
   */
  private static final long serialVersionUID = 1L;
}
