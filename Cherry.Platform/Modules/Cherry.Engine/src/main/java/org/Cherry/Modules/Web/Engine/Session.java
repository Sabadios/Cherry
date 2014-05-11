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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class Session<K extends Serializable, T extends Serializable> implements Map<K, T>, Serializable {
  @Override
  public int size() {
    return getRepository().size();
  }

  @Override
  public boolean isEmpty() {
    return getRepository().isEmpty();
  }

  @Override
  public boolean containsKey(final Object key) {
    return getRepository().containsKey(key);
  }

  @Override
  public boolean containsValue(final Object value) {
    return getRepository().containsValue(value);
  }

  @Override
  public T get(final Object key) {
    return getRepository().get(key);
  }

  @Override
  public T put(final K key, final T value) {
    return getRepository().put(key, value);
  }

  @Override
  public T remove(final Object key) {
    return getRepository().remove(key);
  }

  @Override
  public void putAll(final Map<? extends K, ? extends T> m) {
    getRepository().putAll(m);
  }

  @Override
  public void clear() {
    getRepository().clear();
  }

  @Override
  public Set<K> keySet() {
    return getRepository().keySet();
  }

  @Override
  public Collection<T> values() {
    return getRepository().values();
  }

  @Override
  public Set<java.util.Map.Entry<K, T>> entrySet() {
    return getRepository().entrySet();
  }

  private Map<K, T> getRepository() {
    if (null == _repository)
      _repository = new HashMap<K, T>();

    return _repository;
  }

  @Override
  public int hashCode() {
    return getJSessionID().hashCode();
  }

  @Override
  public String toString() {
    return getJSessionID().toString();
  }

  @Override
  public boolean equals(final Object other) {
    if (null == other)
      return false;

    if (this == other)
      return true;

    if (other instanceof Session) return getJSessionID().equals(((Session) other).getJSessionID());

    return false;
  }

  public UUID getJSessionID() {
    return _jSessionID;
  }

  private final UUID _jSessionID;
  private Map<K, T> _repository;

  public Session() {
    _jSessionID = UUID.randomUUID();
  }

  public Session(final UUID jSessionID) {
    if (null == jSessionID)
      throw new IllegalArgumentException("Undefined JSESSIONID value!");
    _jSessionID = jSessionID;
  }

  private static final long serialVersionUID = 1L;
}
