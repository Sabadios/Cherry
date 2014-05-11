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
package org.Cherry.Modules.Web.Agents.Standard;

import static org.Cherry.Modules.Web.WebConstants.URI_TOKEN;

import org.apache.http.HttpStatus;

public enum HttpStatusCode {
  OK(HttpStatus.SC_OK),
  BadRequest(HttpStatus.SC_BAD_REQUEST),
  Unauthorized(HttpStatus.SC_UNAUTHORIZED),
  Forbbiden(HttpStatus.SC_FORBIDDEN),
  NotFound(HttpStatus.SC_NOT_FOUND),
  NotAllowed(HttpStatus.SC_METHOD_NOT_ALLOWED),
  NotAcceptable(HttpStatus.SC_NOT_ACCEPTABLE),
  InternalServerError(HttpStatus.SC_INTERNAL_SERVER_ERROR),
  NotImplemented(HttpStatus.SC_NOT_IMPLEMENTED),
  ServiceUnavailable(HttpStatus.SC_SERVICE_UNAVAILABLE);

  public String asURI() {
    return new StringBuilder(URI_TOKEN).append(_statusCode.toString()).toString();
  }

  public int hashcode() {
    return _statusCode;
  }

  @Override
  public String toString() {
    return _statusCode.toString();
  }

  private final Integer _statusCode;

  private HttpStatusCode(final Integer statusCode) {
    _statusCode = statusCode;
  }
}
