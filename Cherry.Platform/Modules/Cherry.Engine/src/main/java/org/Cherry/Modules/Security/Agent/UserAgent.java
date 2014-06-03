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
package org.Cherry.Modules.Security.Agent;

import static org.Cherry.Modules.Web.WebConstants.URI_TOKEN;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.Hazelcast.Middleware.HazelcastService;
import org.Cherry.Modules.Security.Crypto.CryptoService;
import org.Cherry.Modules.Security.Messaging.FailureMessage;
import org.Cherry.Modules.Security.Messaging.SuccessMessage;
import org.Cherry.Modules.Security.Middleware.UserRepository;
import org.Cherry.Modules.Security.Model.User;
import org.Cherry.Modules.Security.Model.Users;
import org.Cherry.Modules.Web.Agents.Model.MessageTemplate;
import org.Cherry.Modules.Web.Engine.Context;
import org.Cherry.Modules.Web.Engine.InvocationContext;
import org.Cherry.Modules.Web.Engine.SessionManager;
import org.apache.http.HttpRequest;
import org.jboss.weld.environment.se.events.ContainerInitialized;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
@Path(value = AgentURI.User.BASE_URI)
public final class UserAgent extends ServiceTemplate {
  @Path(value = URI_TOKEN)
  @Produces(MediaType.TEXT_HTML)
  public Map<?, ?> get() {
    final HttpRequest request = Context.getInvocationContext().get(InvocationContext.Key.Request);

    debug("Invoked by [{}]", request);

    return Collections.emptyMap();
  }

  @Path(value = AgentURI.User.SAVE_URI)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public MessageTemplate save(@BeanParam final User user) {
    final HttpRequest request = Context.getInvocationContext().get(InvocationContext.Key.Request);

    debug("Invoked by [{}] with bean parameter [{}]", request, user);

    user.setParole(getCryptoService().encryptParole(user.getParole()));

    try {
      getUserRepository().save(user);
    } catch (final Throwable t) {
      error(t, t.getMessage());
      return new FailureMessage();
    }

    return new SuccessMessage();
  }

  @Path(value = AgentURI.User.USERS_URI)
  @Produces(MediaType.APPLICATION_JSON)
  public Users users() {
    final HttpRequest request = Context.getInvocationContext().get(InvocationContext.Key.Request);

    debug("Invoked by [{}]", request);

    final Set<User> data = new HashSet<User>();

    data.add(new User("abcdefghij", "ABCD1234$%"));

    final Users users = new Users(data);

    return users;
  }

  private SessionManager getSessionManager() {
    assert null != _sessionManager;
    return _sessionManager;
  }

  private UserRepository getUserRepository() {
    assert null != _userRepository;
    return _userRepository;
  }

  private HazelcastService getHazelcastService() {
    assert null != _hazelcastService;
    return _hazelcastService;
  }

  private CryptoService getCryptoService() {
    assert null != _cryptoService;
    return _cryptoService;
  }

  @Inject
  @Singleton
  private SessionManager _sessionManager;

  @Inject
  @Singleton
  private UserRepository _userRepository;

  @Inject
  @Singleton
  private HazelcastService _hazelcastService;

  @Inject
  @Singleton
  private CryptoService _cryptoService;

  @PostConstruct
  protected void postConstruct() {
  }

  void containerInitialized(@Observes final ContainerInitialized event) {
    info("CDI container initialialization completed - notification [{}] received.", event);

    info("Attached to service dependency : [{}]", getSessionManager());
    info("Attached to service dependency : [{}]", getUserRepository());
    info("Attached to service dependency : [{}]", getHazelcastService());
  }

  @PreDestroy
  protected void preDestroy() {
  }

  public UserAgent() {
  }

  private static final long serialVersionUID = 1L;
}
