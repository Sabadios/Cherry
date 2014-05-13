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
package org.Cherry.Modules.Web.Agents.Model;

import static org.Cherry.Utils.Utils.failIfEmpty;

import java.io.Serializable;

/**
 * @author Cristian.Malinescu
 * 
 */
public class StatusTemplate implements Serializable {
  public String getCode() {
    return _code;
  }

  @Override
  public int hashCode() {
    return _code.hashCode();
  }

  @Override
  public String toString() {
    return _code;
  }

  @Override
  public boolean equals(final Object other) {
    if (null == other) return false;

    if (this == other) return true;

    if (other instanceof StatusTemplate) return getCode().equals(((StatusTemplate) other).getCode());

    return false;
  }

  private final String _code;

  public StatusTemplate(final String code) {
    failIfEmpty(code);
    _code = code;
  }

  static public final StatusTemplate SUCCESS = new StatusTemplate("SUCCESS") {
    private static final long serialVersionUID = 1L;
  }, FAILURE = new StatusTemplate("FAILURE") {
    private static final long serialVersionUID = 1L;
  }, UNKNOWN = new StatusTemplate("UNKNOWN") {
    private static final long serialVersionUID = 1L;
  };

  static private final long serialVersionUID = 1L;
}
