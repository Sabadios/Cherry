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
package org.Cherry.Modules.Web;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicServerCookie extends BasicClientCookie {
  @Override
  public Object clone() throws CloneNotSupportedException {
    final BasicServerCookie clone = (BasicServerCookie) super.clone();
    return clone;
  }

  @Override
  public String toString() {
    final StringBuilder cookieVal = new StringBuilder(getName()).append("=").append(getValue())
        .append("; path=").append(getPath())
        .append("; domain=").append(getDomain())
        .append("; version=").append(getVersion())
        .append("; expiry=").append(getExpiryDate());

    return cookieVal.toString();
  }

  public BasicServerCookie(final String name, final String value) {
    super(name, value);
    log.debug("{'{}':'{}'}", getName(), getValue());
  }

  static private final Logger log = LoggerFactory.getLogger(BasicServerCookie.class);

  private static final long serialVersionUID = 1L;
}
