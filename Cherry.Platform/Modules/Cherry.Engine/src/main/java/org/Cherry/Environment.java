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

package org.Cherry;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;

import java.util.UUID;

public abstract class Environment {
  static public final String MAJOR = "Cherry";

  static public final class Properties {
    static public final String MAJOR = "Platform",
        INSTANCE_ID = build(new String[] { Environment.MAJOR, Properties.MAJOR, "Instance_ID" });

    private Properties() {
      throw new IllegalStateException();
    }
  }

  static public final void setInstanceID(final UUID id) {
    set(Properties.INSTANCE_ID, id.toString());
  }

  static public final UUID getInstanceID() {
    final String ID = get(Properties.INSTANCE_ID);
    return null != ID && 0 < ID.trim().length() ? UUID.fromString(ID) : null;
  }

  static public final void set(final String prop, final String entry) {
    setProperty(prop, entry);
  }

  static public final String get(final String prop) {
    return getProperty(prop);
  }

  static public final String build(final String... props) {
    final StringBuilder sb = new StringBuilder();

    for (int i = 0; i < props.length; i++) {
      sb.append(props[i]);
      if (i < props.length - 1)
        sb.append(".");
    }

    return sb.toString();
  }

  private Environment() {
    throw new IllegalStateException();
  }
}
