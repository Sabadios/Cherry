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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Modules.Mongo.Middleware.MongoRepositoryService;
import org.Cherry.Modules.Mongo.Morphia.MorphiaService;
import org.Cherry.Modules.Security.Crypto.CryptoService;
import org.Cherry.Modules.Security.Model.User;
import org.Cherry.Modules.Security.Repository.UserDAO;
import org.apache.http.auth.InvalidCredentialsException;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
public final class UserRepository extends MorphiaRepositoryTemplate {
  public void save(final User user) {
    failDuplicates(user);

    final Key<User> key = persist(user);

    final Object id = key.getId();
  }

  private void failDuplicates(final User user) {
    if (null != find(user))
      throw new IllegalStateException("Duplicated identity found!");
  }

  public User find(final User criteria) {
    final Query<User> qry = createQuery(User.class);
    qry.and(qry.criteria("name").equal(criteria.getName()));

    return getUserDAO().find(qry).get();
  }

  public InvalidCredentialsException authenticate(final User criteria) {
    final User found = find(criteria);

    if (null == found)
      return new InvalidCredentialsException("Non existing identity [" + criteria.getName() + "]!");

    final Boolean paroleMatch = getCryptoService().checkParole(criteria.getParole(), found.getParole());// found.getParole().equals(criteria.getParole())

    if (!paroleMatch)
      return new InvalidCredentialsException("Parole failure for identity [" + criteria.getName() + "]!");

    return null;
  }

  private UserDAO getUserDAO() {
    if (null == _userDAO)
      _userDAO = new UserDAO(getMongoRepository().getMongo(), getMorphia(), getMongoRepository().getDB().getName());

    return _userDAO;
  }

  private CryptoService getCryptoService() {
    assert null != _cryptoService;
    return _cryptoService;
  }

  @Inject
  @Singleton
  private MongoRepositoryService _mongoRepository;

  @Inject
  @Singleton
  private MorphiaService _morphia;

  @Inject
  @Singleton
  private CryptoService _cryptoService;

  private UserDAO _userDAO;

  @PostConstruct
  protected void postConstruct() {
  }

  @PreDestroy
  protected void preDestroy() {
  }

  public UserRepository() {
  }

  private static final long serialVersionUID = 1L;
}
