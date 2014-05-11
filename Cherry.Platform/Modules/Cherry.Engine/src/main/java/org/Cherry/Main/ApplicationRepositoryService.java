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
package org.Cherry.Main;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Singleton;

import org.Cherry.Core.ServiceTemplate;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
public class ApplicationRepositoryService extends ServiceTemplate implements Map<ApplicationKey, ApplicationEntry<?>> {
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
  public ApplicationEntry<?> get(final Object key) {
    return getRepository().get(key);
  }

  @Override
  public ApplicationEntry<?> remove(final Object key) {
    return getRepository().remove(key);
  }

  @Override
  public void clear() {
    getRepository().clear();
  }

  @Override
  public Set<ApplicationKey> keySet() {
    return getRepository().keySet();
  }

  @Override
  public ApplicationEntry<?> put(final ApplicationKey key, final ApplicationEntry<?> value) {
    return getRepository().put(key, value);
  }

  @Override
  public void putAll(final Map<? extends ApplicationKey, ? extends ApplicationEntry<?>> m) {
    getRepository().putAll(m);
  }

  @Override
  public Collection<ApplicationEntry<?>> values() {
    return getRepository().values();
  }

  @Override
  public Set<java.util.Map.Entry<ApplicationKey, ApplicationEntry<?>>> entrySet() {
    return getRepository().entrySet();
  }

  private Map<ApplicationKey, ApplicationEntry<?>> getRepository() {
    return _repository;
  }

  private final ConcurrentMap<ApplicationKey, ApplicationEntry<?>> _repository = new ConcurrentHashMap<ApplicationKey, ApplicationEntry<?>>();

  /**
   *
   */
  public ApplicationRepositoryService() {
  }

  private static final long serialVersionUID = 1L;
}
