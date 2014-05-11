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

import static org.Cherry.Modules.Web.WebConstants.HTML_TEMPLATE_POSTFIX;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpContext;

class RequestTemplateCommand extends RequestCommand {
  @Override
  public String getUri() {
    return normalizeTemplatePath(super.getUri());
  }

  public RequestTemplateCommand(final RequestCommand original) throws MethodNotSupportedException {
    this(original.getRequest(), original.getResponse(), original.getContext(), original.getDocRoot());
  }

  public RequestTemplateCommand(final HttpRequest request, final HttpResponse response, final HttpContext context, final String docRoot)
      throws MethodNotSupportedException {
    super(request, response, context, docRoot);
  }

  static public String normalizeTemplatePath(final String uri) {
    return uri + HTML_TEMPLATE_POSTFIX;
  }
}
