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
package org.Cherry.Modules.Security.Middleware;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.Mongo.Middleware.MongoRepositoryService;
import org.Cherry.Modules.Mongo.Morphia.MorphiaService;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import com.mongodb.WriteConcern;

/**
 * @author Cristian.Malinescu
 * 
 */
public abstract class MorphiaRepositoryTemplate extends ServiceTemplate {
  protected <T> Query<T> createQuery(final Class<T> type) {
    return getDatastore().createQuery(type);
  }

  protected <T> Key<T> persist(final T entity) {
    return persist(entity, WriteConcern.ACKNOWLEDGED);
  }

  protected <T> Key<T> persist(final T entity, final WriteConcern wc) {
    return getDatastore().save(entity, wc);
  }

  protected Datastore getDatastore() {
    return getMorphia().getDatastore();
  }

  protected MongoRepositoryService getMongoRepository() {
    assert null != _mongoRepository;
    return _mongoRepository;
  }

  protected MorphiaService getMorphia() {
    assert null != _morphia;
    return _morphia;
  }

  @Inject
  @Singleton
  private MongoRepositoryService _mongoRepository;

  @Inject
  @Singleton
  private MorphiaService _morphia;

  public MorphiaRepositoryTemplate() {
  }

  private static final long serialVersionUID = 1L;
}
