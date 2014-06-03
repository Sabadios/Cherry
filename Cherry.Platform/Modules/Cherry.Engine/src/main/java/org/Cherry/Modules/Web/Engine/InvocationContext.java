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

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Cristian.Malinescu
 */
public final class InvocationContext {
  /**
   * We help the client code here keeping shorter; if a type cast exception occurs anyway will be proactively signaled.
   */
  @SuppressWarnings("unchecked")
  public <T> T get(final Key key) {
    return (T) _repository.get(key);
  }

  /**
   * We help the client code here keeping shorter; if a type cast exception occurs anyway will be proactively signaled.
   */
  @SuppressWarnings("unchecked")
  <T> T set(final Key key, final T value) {
    return (T) _repository.put(key, value);
  }

  public Long getID() {
    return _ID;
  }

  private final Map<Key, Object> _repository = new WeakHashMap<Key, Object>();
  private final Long _ID;

  static public enum Key {
    Request, Authenticated
  }

  static private final AtomicLong counter = new AtomicLong();

  InvocationContext() {
    _ID = counter.incrementAndGet();
  }

  @Override
  public int hashCode() {
    return _ID.intValue();
  }

  @Override
  public String toString() {
    return _ID.toString();
  }

  @Override
  public boolean equals(final Object other) {
    if (null == other) return false;
    if (this == other) return true;
    if (other instanceof InvocationContext) return getID().equals(((InvocationContext) other).getID());
    return false;
  }
}
