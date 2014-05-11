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
package org.Cherry.Modules.Messaging.Model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Head implements Serializable {
  public void setHeaders(final Set<Header> headers) {
    if (null == headers)
      throw new IllegalArgumentException("Undefined headers set by the caller/client ?!");

    _headers = headers;
  }

  public Set<Header> getHeaders() {
    if (_headers.isEmpty())
      _headers = new HashSet<Header>();
    return _headers;
  }

  private Set<Header> _headers = Collections.emptySet();

  @JsonCreator
  Head() {
  }

  private static final long serialVersionUID = 1L;
}// Head