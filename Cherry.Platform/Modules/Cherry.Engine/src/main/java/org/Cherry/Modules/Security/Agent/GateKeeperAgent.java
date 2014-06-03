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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Singleton;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.Security.Messaging.FailureMessage;
import org.Cherry.Modules.Security.Messaging.SuccessMessage;
import org.Cherry.Modules.Security.Model.User;
import org.Cherry.Modules.Web.Agents.Model.MessageTemplate;
import org.Cherry.Modules.Web.Engine.Context;
import org.Cherry.Modules.Web.Engine.InvocationContext;
import org.apache.http.HttpRequest;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
@Path(value = AgentURI.GeteKeeper.BASE_URI)
@NotThreadSafe
public final class GateKeeperAgent extends ServiceTemplate {
  @Path(value = AgentURI.GeteKeeper.OPEN_URI)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public MessageTemplate open(@BeanParam final User user) {
    final HttpRequest request = Context.getInvocationContext().get(InvocationContext.Key.Request);
    final Boolean authenticated = Context.getInvocationContext().get(InvocationContext.Key.Authenticated);

    debug("Invoked by [{}] with bean parameter [{}] and authenticated [{}]", request, user, authenticated);

    if (authenticated)
      return new SuccessMessage();

    return new FailureMessage();
  }

  @Path(value = AgentURI.GeteKeeper.CLOSE_URI)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public MessageTemplate close() {
    final HttpRequest request = Context.getInvocationContext().get(InvocationContext.Key.Request);
    final Boolean authenticated = Context.getInvocationContext().get(InvocationContext.Key.Authenticated);

    debug("Invoked by [{}] and authenticated [{}]", request, authenticated);

    return new SuccessMessage();
  }

  @PostConstruct
  protected void postConstruct() {
  }

  @PreDestroy
  protected void preDestroy() {
  }

  public GateKeeperAgent() {
  }

  private static final long serialVersionUID = 1L;
}
