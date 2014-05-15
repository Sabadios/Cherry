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
package org.Cherry.Context;

import static org.Cherry.Configuration.ConfigurationConstants.USER_DIR;
import static org.Cherry.Configuration.ConfigurationConstants.USER_HOME;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.Freemarker.Middleware.FreeMarkerService;
import org.Cherry.Modules.Hazelcast.Middleware.HazelcastService;
import org.Cherry.Modules.Mongo.Middleware.MongoRepositoryService;
import org.Cherry.Modules.Web.Engine.EnvironmentManager;
import org.Cherry.Modules.Web.Engine.WebEngineManager;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
public class ApplicationContextService extends ServiceTemplate {
  public void start() {
    getMongoRepositoryService().setup();
    getHazelcastService().setup();
    getFreeMarkerService().setup();
    getEnvironmentManager().setup();

    getExecutorService().execute(getWebEngineManager().getRequestWorkerManager());

    info("Http Server started in 'user.dir' : [{}] ...", USER_DIR);
    info("Http Server using 'user.home' : [{}] ...", USER_HOME);
  }

  public void shutdown() {
    getExecutorService().shutdown();

    try {
      getExecutorService().awaitTermination(5, TimeUnit.MINUTES);
    } catch (final InterruptedException e) {
      error(e, "");
    }
  }

  public WebEngineManager getWebEngineManager() {
    assert null != _webEngineManager;
    return _webEngineManager;
  }

  public ExecutorService getExecutorService() {
    assert null != _executorService;
    return _executorService;
  }

  private FreeMarkerService getFreeMarkerService() {
    assert null != _freeMarkerService;
    return _freeMarkerService;
  }

  public MongoRepositoryService getMongoRepositoryService() {
    assert null != _mongoRepositoryService;
    return _mongoRepositoryService;
  }

  public EnvironmentManager getEnvironmentManager() {
    assert null != _environmentManager;
    return _environmentManager;
  }

  private HazelcastService getHazelcastService() {
    assert null != _hazelcastService;
    return _hazelcastService;
  }

  @Inject
  @Singleton
  private EnvironmentManager _environmentManager;

  @Inject
  @Singleton
  private WebEngineManager _webEngineManager;

  @Inject
  @Singleton
  private ExecutorService _executorService;

  @Inject
  @Singleton
  private FreeMarkerService _freeMarkerService;

  @Inject
  @Singleton
  private MongoRepositoryService _mongoRepositoryService;

  @Inject
  @Singleton
  private HazelcastService _hazelcastService;

  @PostConstruct
  protected void postConstruct() {
  }

  @PreDestroy
  protected void preDestroy() {
    shutdown();
  }

  public ApplicationContextService() {
  }

  private static final long serialVersionUID = 1L;
}
