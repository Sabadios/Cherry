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
package org.Cherry.Modules.JSON.Agents;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.REST.RESTMethodDefinition;
import org.Cherry.Modules.REST.RESTPathVisitor;
import org.glassfish.jersey.server.internal.scanning.PackageNamesScanner;

public final class JSONAgentsDictionary extends ServiceTemplate implements Map<String, JSONAgentDefinition>, Serializable {
  public void add(final JSONAgentDefinition jsonAgent) {
    assert null != jsonAgent;
    put(jsonAgent.getRESTRootPath(), jsonAgent);
  }

  @Override
  public int size() {
    return getJSONAgents().size();
  }

  @Override
  public boolean isEmpty() {
    return getJSONAgents().isEmpty();
  }

  @Override
  public boolean containsKey(final Object key) {
    return getJSONAgents().containsKey(key);
  }

  @Override
  public boolean containsValue(final Object value) {
    return getJSONAgents().containsValue(value);
  }

  @Override
  public JSONAgentDefinition get(final Object key) {
    return getJSONAgents().get(key);
  }

  @Override
  public JSONAgentDefinition put(final String key, final JSONAgentDefinition value) {
    return getJSONAgents().put(key, value);
  }

  @Override
  public JSONAgentDefinition remove(final Object key) {
    return getJSONAgents().remove(key);
  }

  @Override
  public void putAll(final Map<? extends String, ? extends JSONAgentDefinition> m) {
    getJSONAgents().putAll(m);
  }

  @Override
  public void clear() {
    getJSONAgents().clear();
  }

  @Override
  public Set<String> keySet() {
    return getJSONAgents().keySet();
  }

  @Override
  public Collection<JSONAgentDefinition> values() {
    return getJSONAgents().values();
  }

  @Override
  public Set<java.util.Map.Entry<String, JSONAgentDefinition>> entrySet() {
    return getJSONAgents().entrySet();
  }

  public Map<String, JSONAgentDefinition> getJSONAgents() {
    return _jsonAgents;
  }

  private Set<String> getControllerNamespaces() {
    return _namespaces;
  }

  public JSONAgentsDictionary scan() {
    for (final String namespace : getControllerNamespaces()) {
      info("Scanning namespace(package) [{}] for controller definitions : ", namespace);
      scan(namespace);
    }

    return this;
  }

  void scan(final String namespace) {
    final WeakReference<PackageNamesScanner> scanner = new WeakReference<PackageNamesScanner>(new PackageNamesScanner(new String[] { namespace }, false));
    final WeakReference<RESTPathVisitor> restPathsVisitor = new WeakReference<RESTPathVisitor>(new RESTPathVisitor(this, namespace));

    String name;
    while (scanner.get().hasNext()) {
      name = scanner.get().next();
      info("Scanned resource [{}]", name);

      if (restPathsVisitor.get().onAccept(name))
        continue;
    }

    if (isDebugEnabled()) {
      WeakReference<JSONAgentDefinition> restBeanDef;
      WeakReference<Set<String>> methodPaths;
      WeakReference<RESTMethodDefinition> restMethodDef;

      final WeakReference<Set<String>> servicesPaths = new WeakReference<Set<String>>(getJSONAgents().keySet());

      for (final String servicePath : servicesPaths.get()) {
        restBeanDef = new WeakReference<JSONAgentDefinition>(getJSONAgents().get(servicePath));
        assert null != restBeanDef;

        methodPaths = new WeakReference<Set<String>>(restBeanDef.get().keySet());

        for (final String methodPath : methodPaths.get()) {
          restMethodDef = new WeakReference<RESTMethodDefinition>(restBeanDef.get().get(methodPath));

          info("Caching controller definition -> servicePath:[{}], methodPath:[{}], method:[{}], methodParams:[{}] & returnType:[{}]",
              servicePath, methodPath, restMethodDef.get(), restMethodDef.get().getParameterTypes(), restMethodDef.get().getReturnType());
        }

        info("Found cached controller definition -> servicePath:[{}].", servicePath);
      }
    }
  }

  public Set<String> getNamespaces() {
    return _namespaces;
  }

  public JSONAgentsDictionary setNamespaces(final Set<String> namespaces) {
    _namespaces = namespaces;
    return this;
  }

  private final Map<String, JSONAgentDefinition> _jsonAgents = new ConcurrentHashMap<String, JSONAgentDefinition>();
  private Set<String> _namespaces;

  public JSONAgentsDictionary() {
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
}
