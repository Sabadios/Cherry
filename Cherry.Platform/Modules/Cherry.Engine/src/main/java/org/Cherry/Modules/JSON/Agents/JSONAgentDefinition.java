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

import static org.Cherry.Modules.Web.WebConstants.URI_TOKEN;
import static org.Cherry.Utils.Utils.failIfEmpty;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.Cherry.Modules.REST.RESTMethodDefinition;
import org.Cherry.Utils.Utils;

public final class JSONAgentDefinition implements Map<String, RESTMethodDefinition> {
  static private String normalize(final String restPath) {
    if (Utils.isEmpty(restPath))
      return URI_TOKEN;
    return restPath;
  }

  public String getRESTRootPath() {
    return _restRootPath;
  }

  public void setRESTRootPath(final String restPath) {
    failIfEmpty(restPath);
    _restRootPath = restPath;
  }

  public Class<?> getJSONAgentType() {
    return _jsonAgentType;
  }

  public void setJSONAgentType(final Class<?> jsonAgentType) {
    assert null != jsonAgentType;
    _jsonAgentType = jsonAgentType;
  }

  public Object getJSONAgent() {
    return _jsonAgent;
  }

  public void setJSONAgent(final Object jsonAgent) {
    _jsonAgent = jsonAgent;
  }

  @Override
  public int size() {
    return getRestMethodDefinitions().size();
  }

  @Override
  public boolean isEmpty() {
    return getRestMethodDefinitions().isEmpty();
  }

  @Override
  public boolean containsKey(final Object key) {
    return getRestMethodDefinitions().containsKey(key);
  }

  @Override
  public boolean containsValue(final Object value) {
    return getRestMethodDefinitions().containsValue(value);
  }

  @Override
  public RESTMethodDefinition get(final Object key) {
    final String methodPath = normalize((String) key);
    return getRestMethodDefinitions().get(methodPath);
  }

  @Override
  public RESTMethodDefinition put(final String key, final RESTMethodDefinition restMethodDef) {
    assert null != restMethodDef;

    if (this != restMethodDef.getJSONAgentDefinition())
      restMethodDef.setJSONAgentDefinition(this);

    return getRestMethodDefinitions().put(key, restMethodDef);
  }

  @Override
  public RESTMethodDefinition remove(final Object key) {
    return getRestMethodDefinitions().remove(key);
  }

  @Override
  public void putAll(final Map<? extends String, ? extends RESTMethodDefinition> m) {
    getRestMethodDefinitions().putAll(m);
  }

  @Override
  public void clear() {
    getRestMethodDefinitions().clear();
  }

  @Override
  public Set<String> keySet() {
    return getRestMethodDefinitions().keySet();
  }

  @Override
  public Collection<RESTMethodDefinition> values() {
    return getRestMethodDefinitions().values();
  }

  @Override
  public Set<java.util.Map.Entry<String, RESTMethodDefinition>> entrySet() {
    return getRestMethodDefinitions().entrySet();
  }

  private Map<String, RESTMethodDefinition> getRestMethodDefinitions() {
    return _restMethodDefinitions;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(super.toString());

    sb.append(" # JSON Agent path [").append(getRESTRootPath()).append("],");
    sb.append(" Type [").append(getJSONAgentType()).append("],");
    sb.append(" Instance [").append(getJSONAgent()).append("].");

    return sb.toString();
  }

  @Override
  public int hashCode() {
    return getRESTRootPath().hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other instanceof JSONAgentDefinition)
      return getRESTRootPath().equals(((JSONAgentDefinition) other).getRESTRootPath());
    return false;
  }

  private String _restRootPath;
  private Class<?> _jsonAgentType;
  private Object _jsonAgent;
  private final Map<String, RESTMethodDefinition> _restMethodDefinitions = new ConcurrentHashMap<String, RESTMethodDefinition>();

  public JSONAgentDefinition(final String restRootPath, final Class<?> restServiceType) {
    setRESTRootPath(restRootPath);
    setJSONAgentType(restServiceType);
  }

  public JSONAgentDefinition() {
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
}
