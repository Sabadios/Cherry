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

import static org.Cherry.Modules.Web.Engine.RequestHandlerTemplate.scanRESTBeanURI;
import static org.Cherry.Modules.Web.MimeAndFileResourcesUtil.supported;
import static org.Cherry.Utils.Utils.isNotEmpty;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.Cherry.Modules.Web.Agents.Standard.HttpStatusCode;
import org.Cherry.Modules.Web.Engine.RequestHandlerTemplate.CallDef;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

/**
 * @author Cristian.Malinescu
 * 
 */
class RequestCommand {
  public String getUri() {
    return getRequest().getRequestLine().getUri();
  }

  public HttpRequest getRequest() {
    return _request;
  }

  public HttpResponse getResponse() {
    return _response;
  }

  public HttpCoreContext getContext() {
    return _context;
  }

  public String getDocRoot() {
    return _docRoot;
  }

  public Attributes<String> getAttributes() {
    if (null == _attributes)
      _attributes = new Attributes<String>(new LinkedList<String>());

    return _attributes;
  }

  public CallDef getCallDef() {
    return _callDef;
  }

  public Map<String, Object> getMetadata() {
    if (null == _metadata)
      _metadata = new LinkedHashMap<String, Object>();

    return _metadata;
  }

  public Boolean isRootCall() {
    return CallDef.RootCall.equals(getCallDef());
  }

  public Boolean isBadJuju() {
    return null != getStatusCode();
  }

  public HttpStatusCode getStatusCode() {
    return _statusCode;
  }

  public void setStatusCode(final HttpStatusCode statusCode) {
    _statusCode = statusCode;
  }

  private final HttpRequest _request;
  private final HttpResponse _response;
  private final HttpCoreContext _context;
  private final String _docRoot;
  private final CallDef _callDef;
  private Attributes<String> _attributes;
  private Map<String, Object> _metadata;
  private HttpStatusCode _statusCode;

  /**
   * @throws MethodNotSupportedException
   * 
   */
  public RequestCommand(final HttpRequest request, final HttpResponse response, final HttpContext context, final String docRoot) throws MethodNotSupportedException {
    assert null != request;
    assert null != response;
    assert null != context;

    _request = request;

    final String uri = supported(request);

    _callDef = scanRESTBeanURI(uri);

    _response = response;
    _context = HttpCoreContext.adapt(context);

    assert isNotEmpty(docRoot);

    _docRoot = docRoot;
  }
}
