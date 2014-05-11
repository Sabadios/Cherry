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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.Cherry.Modules.JSON.Agents.JSONAgentDefinition;
import org.Cherry.Modules.REST.RESTMethodDefinition;
import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;

@Singleton
class JSONRequestHandler extends RequestHandlerTemplate {
  @Override
  public void handle(final RequestCommand command) throws HttpException, IOException {
    final CallDef call = scanRESTBeanURI(command.getUri());

    final JSONAgentDefinition jsonAgent = getController(call.getControllerURI());

    assert null != jsonAgent : "Undefined controller for URI [" + command.getUri() + "]";

    final RESTMethodDefinition restMethodDef = jsonAgent.get(call.getMethodURI());

    assert null != restMethodDef : "Undefined controller method for URI [" + command.getUri() + "/" + call.getMethodURI() + "]";

    WeakReference<Object> wkRef = null;

    if (null != restMethodDef.getBeanParameterType())
      wkRef = new WeakReference<Object>(restMethodDef.invoke(getBeanParameter(command, restMethodDef.getBeanParameterType())));
    else wkRef = new WeakReference<Object>(restMethodDef.invoke((Object[]) null));

    AbstractHttpEntity entity = null;

    if (null != wkRef && null != wkRef.get())
      if (getObjectMapperService().canSerialize(wkRef.get().getClass())) {
        debug("Handled URI [{}] with controller [{}] and result [{}]", command.getUri(), jsonAgent, wkRef.get());

        final WeakReference<ByteArrayOutputStream> os = new WeakReference<ByteArrayOutputStream>(new ByteArrayOutputStream());

        getObjectMapperService().writeValue(os.get(), wkRef.get());

        entity = new InputStreamEntity(new ByteArrayInputStream(os.get().toByteArray()), APPLICATION_JSON_CONTENT_TYPE);
      } else warn("Object [{}] not JSON serializable!", wkRef.get());
    else warn("JSON Controller returned null for expected result of type [{}]", restMethodDef.getBeanParameterType());

    command.getResponse().setStatusCode(HttpStatus.SC_OK);

    if (null == entity) entity = new StringEntity(EMPTY_JSON_OBJECT, APPLICATION_JSON_CONTENT_TYPE);

    command.getResponse().setEntity(entity);
  }

  static public final ContentType APPLICATION_JSON_CONTENT_TYPE = ContentType.create("application/json", "UTF-8");
  static public final String EMPTY_JSON_OBJECT = "{}";

  @PostConstruct
  protected void postConstruct() {
  }

  @PreDestroy
  protected void preDestroy() {
  }

  JSONRequestHandler() {
  }

  private static final long serialVersionUID = 1L;
}