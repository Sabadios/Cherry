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

import static org.Cherry.Modules.Web.MimeAndFileResourcesUtil.getFileResource;
import static org.Cherry.Modules.Web.WebConstants.ROOT_URI;
import static org.Cherry.Modules.Web.WebConstants.URI_TOKEN;
import static org.Cherry.Utils.Utils.failIfEmpty;
import static org.Cherry.Utils.Utils.isEmpty;
import static org.Cherry.Utils.Utils.isNotEmpty;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Configuration.ConfigurationService;
import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.Freemarker.DictionaryModel;
import org.Cherry.Modules.Freemarker.Middleware.FreeMarkerService;
import org.Cherry.Modules.JSON.Agents.JSONAgentDefinition;
import org.Cherry.Modules.Jackson.Middleware.ObjectMapperService;
import org.Cherry.Modules.Web.Agents.Standard.HttpStatusCode;
import org.Cherry.Modules.Web.Engine.InvocationContext.Key;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author Cristian.Malinescu
 * 
 */
abstract class RequestHandlerTemplate extends ServiceTemplate implements IRequestHandlerEx {
  /*
   * (non-Javadoc)
   * 
   * @see org.apache.http.protocol.HttpRequestHandler#handle(org.apache.http.HttpRequest, org.apache.http.HttpResponse, org.apache.http.protocol.HttpContext)
   */
  @Override
  public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
    Context.instance().getInvocationContext().set(Key.Request, request);

    final RequestCommand command = new RequestCommand(request, response, context, getDocRoot());

    try {
      handle(command);
    } catch (final Throwable t) {
      error(t, t.getMessage());

      if (null != command) {
        command.setStatusCode(HttpStatusCode.InternalServerError);
        handle(command);
      }
    } finally {
      Context.instance().flush();
    }
  }

  static class CallDef {
    private String _controllerUri = ROOT_URI;
    private String _methodUri = ROOT_URI;

    String getControllerURI() {
      return _controllerUri;
    }

    void setControllerURI(final String controllerUri) {
      if (isNotEmpty(controllerUri))
        _controllerUri = controllerUri;
    }

    String getMethodURI() {
      return _methodUri;
    }

    void setMethodURI(final String methodUri) {
      if (isNotEmpty(methodUri))
        _methodUri = methodUri;
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("{'controllerUri':'").append(getControllerURI());
      sb.append("','methodUri':'").append(getMethodURI()).append("'}");

      return sb.toString();
    }

    CallDef(final String controllerUri, final String methodUri) {
    }

    private CallDef() {
    }

    static final CallDef RootCall = new CallDef();
  }

  static CallDef scanRESTBeanURI(final String uri) {
    if (isEmpty(uri) || ROOT_URI.equals(uri))
      return CallDef.RootCall;

    final CallDef call = new CallDef();

    final WeakReference<StringTokenizer> st = new WeakReference<StringTokenizer>(new StringTokenizer(uri, URI_TOKEN));

    if (st.get().hasMoreTokens()) {
      String token = st.get().nextToken();

      if (isNotEmpty(token)) {
        call.setControllerURI(URI_TOKEN + token);

        if (st.get().hasMoreTokens()) {
          token = st.get().nextToken();

          if (isNotEmpty(token))
            call.setMethodURI(URI_TOKEN + token);
        }
      }
    }

    return call;
  }

  protected Object getBeanParameter(final RequestCommand command, final Class<?> beanParameterType) throws JsonParseException, JsonMappingException, IOException {
    Object result = null;

    final HttpRequest request = command.getRequest();

    if (request instanceof HttpEntityEnclosingRequest) {
      final HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();

      final long contentLength = entity.getContentLength();

      if (0 < contentLength) {
        final WeakReference<InputStream> is = new WeakReference<InputStream>(entity.getContent());
        result = getObjectMapperService().readValue(is.get(), beanParameterType);
      } else warn("{}", "Undefined content/length 0! Did the sender missed it?!");
    } else throw new IllegalArgumentException("Expected request of type [" + HttpEntityEnclosingRequest.class + "] but found [" + request.getClass() + "]");

    return result;
  }

  protected DictionaryModel createModel() {
    return getFreeMarkerService().createModel();
  }

  protected ObjectMapperService getObjectMapperService() {
    assert null != _objectMapperService;
    return _objectMapperService;
  }

  protected FreeMarkerService getFreeMarkerService() {
    assert null != _freeMarkerService;
    return _freeMarkerService;
  }

  protected JSONAgentDefinition getController(final String uri) {
    failIfEmpty(uri);
    return getControllersManagerService().getController(uri);
  }

  protected ControllersManager getControllersManagerService() {
    assert null != _controllersManager;
    return _controllersManager;
  }

  protected File getFile(final String uri) throws UnsupportedEncodingException {
    if (!getFilesCache().containsKey(uri)) {
      final File file = getFileResource(getDocRoot(), uri);
      getFilesCache().putIfAbsent(uri, file);
      return file;
    }

    return getFilesCache().get(uri);
  }

  protected ConcurrentMap<String, File> getFilesCache() {
    return _filesCache;
  }

  @Inject
  @Singleton
  private ControllersManager _controllersManager;

  @Inject
  @Singleton
  private ObjectMapperService _objectMapperService;

  @Inject
  @Singleton
  private FreeMarkerService _freeMarkerService;

  /*
   * (non-Javadoc)
   * 
   * @see net.Sabadios.RnD.Http.HttpServer.IHttpRequestHandlerEx#handle(net.Sabadios.RnD.Http.HttpServer.HttpRequestHandlerCommand)
   */
  @Override
  public abstract void handle(RequestCommand command) throws HttpException, IOException;

  protected String getDocRoot() {
    return _configurationService.getDocRoot();
  }

  protected String getWelcomeDoc() {
    return _configurationService.getWelcomeDoc();
  }

  @Inject
  @Singleton
  private ConfigurationService _configurationService;

  private final ConcurrentMap<String, File> _filesCache = new ConcurrentHashMap<String, File>();

  /**
   *
   */
  protected RequestHandlerTemplate() {
  }

  /**
   *
   */
  private static final long serialVersionUID = 1L;
}
