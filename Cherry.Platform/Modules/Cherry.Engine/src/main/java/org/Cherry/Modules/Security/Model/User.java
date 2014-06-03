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
package org.Cherry.Modules.Security.Model;

import org.Cherry.Modules.Mongo.Model.EntityTemplate;
import org.mongodb.morphia.annotations.Entity;

/**
 * @author Cristian.Malinescu
 * 
 */
@Entity
public class User extends EntityTemplate {
  public String getName() {
    return name;
  }

  public void setName(final String _name) {
    assert null != _name && 8 <= _name.trim().length();
    name = _name;
  }

  public String getParole() {
    return parole;
  }

  public void setParole(final String _parole) {
    assert null != _parole && 8 <= _parole.trim().length();
    parole = _parole;
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other instanceof User) {
      final User that = (User) other;
      return getName().equals(that.getName());
    }

    return false;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("{User:{");
    sb.append("'name':'").append(getName()).append("', 'parole':'").append(getParole()).append("'");

    sb.append("}}");

    return sb.toString();
  }

  private String name, parole;

  public User(final String name, final String parole) {
    setName(name);
    setParole(parole);
  }

  public User() {
  }

  static public final String TYPE_ID = User.class.getCanonicalName();

  private static final long serialVersionUID = 1L;
}
