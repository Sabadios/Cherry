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
package org.Cherry.Modules.Mongo.Middleware;

import static org.Cherry.Utils.Utils.failIfEmpty;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Configuration.ConfigurationService;
import org.Cherry.Core.ServiceTemplate;

import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoServerSelectionException;
import com.mongodb.ServerAddress;

@Singleton
public class MongoRepositoryService extends ServiceTemplate {
  public DB getDB(final String name) {
    failIfEmpty(name, "Database name can't be empty!");
    return getMongo().getDB(name);
  }

  public DB getDB() {
    return getDB(getDBName());
  }

  public void setup() {
    setMongo(new MongoClient(getServerAddresses()));

    info("Using Mongo connector version [{}].", getMongo().getVersion());

    final DB db = getMongo().getDB(DEFAULT_DB_NAME);

    assert null != db;

    try {
      final CommandResult dbStats = db.getStats();

      final Set<?> keys = dbStats.keySet();
      Object entry;

      info("Executing & listing 'dbStats' for [{}].", db.getName());
      for (final Object key : keys) {
        entry = dbStats.get(key);

        info("{'key':'{}', 'value':'{}'}", key, entry);
      }
    } catch (final MongoServerSelectionException t) {
      error(
          "\n-----------------------------------------------------------------------------------------------\n!Error [{}] occured when testing connectivity to [{}] database!\n-----------------------------------------------------------------------------------------------",
          t.getMessage(), db.getName());
    } catch (final Throwable t) {
      error(t, t.getMessage());
    }
  }

  private List<ServerAddress> getServerAddresses() {
    final List<org.Cherry.Configuration.MongoDB.ServerAddress> addresses = getConfigurationService().getServerAddresses();
    final List<ServerAddress> serverAddresses = new LinkedList<ServerAddress>();

    if (null != addresses && !addresses.isEmpty())
      for (final org.Cherry.Configuration.MongoDB.ServerAddress address : addresses)
        serverAddresses.add(address.asServerAddress());

    return serverAddresses;
  }

  private String getDBName() {
    return getConfigurationService().getDBName();
  }

  @PostConstruct
  void postConstruct() {
  }

  private ConfigurationService getConfigurationService() {
    assert null != _configurationService;
    return _configurationService;
  }

  @PreDestroy
  void preDestroy() {
    getMongo().close();
  }

  @Inject
  @Singleton
  private ConfigurationService _configurationService;

  private MongoClient _mongo;

  public MongoRepositoryService() {
  }

  private MongoClient getMongo() {
    return _mongo;
  }

  private void setMongo(final MongoClient mongo) {
    _mongo = mongo;
  }

  static public final String DEFAULT_DB_NAME = "test";

  /**
   *
   */
  private static final long serialVersionUID = 1L;
}
