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

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static org.Cherry.Modules.Web.MimeAndFileResourcesUtil.isFileSig;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Modules.JSON.Agents.JSONAgentDefinition;
import org.Cherry.Modules.REST.RESTMethodDefinition;
import org.Cherry.Modules.Web.Agents.Standard.HttpStatusCode;
import org.apache.http.HttpException;
import org.apache.http.MethodNotSupportedException;

@Singleton
class RequestDispatcher extends RequestHandlerTemplate {
  @Override
  public void handle(final RequestCommand command) throws HttpException, IOException {
    if (command.isBadJuju()) {
      getBadJujuRequestHandlerService().handle(command);
      return;
    }

    final String uri = command.getUri();

    debug("Handling Request with URI [{}]", uri);

    final JSONAgentDefinition jsonAgent = getController(command.getCallDef().getControllerURI());

    if (null != jsonAgent) {
      final RESTMethodDefinition restMthdDef = jsonAgent.get(command.getCallDef().getMethodURI());

      if (null != restMthdDef &&
          null != restMthdDef.getProduces() &&
          null != restMthdDef.getProduces().value() &&
          1 == restMthdDef.getProduces().value().length)
        switch (restMthdDef.getProduces().value()[0]) {
          case APPLICATION_JSON:
            getJSONRequestHandlerService().handle(command);
            return;
          case TEXT_HTML:
            getTemplateRequestHandlerService().handle(command);
            return;
        }
    }

    getHttpRequestHandler(command).handle(command);
  }

  private IRequestHandlerEx getHttpRequestHandler(final RequestCommand command) throws MethodNotSupportedException {
    final String uri = command.getUri();

    debug("Handling Request with URI [{}]", uri);

    if (isFileSig(uri))
      return getFileRequestHandlerService();

    command.setStatusCode(HttpStatusCode.BadRequest);

    return getBadJujuRequestHandlerService();
  }

  private FileRequestHandler getFileRequestHandlerService() {
    assert null != _fileRqstHndlrSrvc;
    return _fileRqstHndlrSrvc;
  }

  private JSONRequestHandler getJSONRequestHandlerService() {
    assert null != _jsontRqstHndlrSrvc;
    return _jsontRqstHndlrSrvc;
  }

  private TemplateRequestHandler getTemplateRequestHandlerService() {
    assert null != _templateRequestHandler;
    return _templateRequestHandler;
  }

  private BadJujuRequestHandlerService getBadJujuRequestHandlerService() {
    assert null != _badJujuRqstHndlrSrvc;
    return _badJujuRqstHndlrSrvc;
  }

  @Inject
  @Singleton
  private BadJujuRequestHandlerService _badJujuRqstHndlrSrvc;

  @Inject
  @Singleton
  private FileRequestHandler _fileRqstHndlrSrvc;

  @Inject
  @Singleton
  private JSONRequestHandler _jsontRqstHndlrSrvc;

  @Inject
  @Singleton
  private TemplateRequestHandler _templateRequestHandler;

  @PostConstruct
  protected void postConstruct() {
  }

  @PreDestroy
  protected void preDestroy() {
  }

  public RequestDispatcher() {
  }

  /**
   *
   */
  private static final long serialVersionUID = 1L;
}