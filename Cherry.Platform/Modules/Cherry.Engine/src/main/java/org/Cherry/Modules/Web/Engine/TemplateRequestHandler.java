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

import static org.Cherry.Modules.Web.MimeAndFileResourcesUtil.APPLICATION_HTML_CONTENT_TYPE;

import java.io.IOException;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.Cherry.Core.FastStringWriter;
import org.Cherry.Modules.Freemarker.DictionaryModel;
import org.Cherry.Modules.JSON.Agents.JSONAgentDefinition;
import org.Cherry.Modules.REST.RESTMethodDefinition;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;

@Singleton
class TemplateRequestHandler extends RequestHandlerTemplate {
  @Override
  public void handle(final RequestCommand command) throws HttpException, IOException {
    final WeakReference<CallDef> call = new WeakReference<CallDef>(scanRESTBeanURI(command.getUri()));

    final JSONAgentDefinition jsonAgent = getController(call.get().getControllerURI());

    assert null != jsonAgent : "Undefined controller for URI [" + command.getUri() + "]";

    final RESTMethodDefinition restMethodDef = jsonAgent.get(call.get().getMethodURI());

    assert null != restMethodDef : "Undefined controller method for URI [" + command.getUri() + "/" + call.get().getMethodURI() + "]";

    final Object result = restMethodDef.invoke((Object[]) null);

    AbstractHttpEntity entity = null;

    if (null != result) {
      final WeakReference<DictionaryModel> model = new WeakReference<DictionaryModel>(createModel());

      model.get().putAll((Map<?, ?>) result);

      final WeakReference<Writer> writer = new WeakReference<Writer>(new FastStringWriter());
      final WeakReference<RequestTemplateCommand> tmpltCmd = new WeakReference<RequestTemplateCommand>(new RequestTemplateCommand(command));
      String tmpltDoc;

      if (tmpltCmd.get().isRootCall())
        tmpltDoc = getWelcomeDoc();
      else tmpltDoc = tmpltCmd.get().getUri();

      try {
        getFreeMarkerService().process(tmpltDoc, model.get(), writer.get());
        entity = new ByteArrayEntity(((FastStringWriter) writer.get()).getBuffer().toString().getBytes(), APPLICATION_HTML_CONTENT_TYPE);
      } catch (final Throwable t) {
        throw new IllegalStateException(t);
      }
    } else warn("JSON Controller returned null for expected result of type [{}]", restMethodDef.getBeanParameterType());

    if (null == entity)
      throw new IllegalStateException("Template file [" + command.getUri() + "] not found!");

    command.getResponse().setStatusCode(HttpStatus.SC_OK);
    command.getResponse().setEntity(entity);
  }

  @PostConstruct
  protected void postConstruct() {
  }

  @PreDestroy
  protected void preDestroy() {
  }

  TemplateRequestHandler() {
  }

  private static final long serialVersionUID = 1L;
}