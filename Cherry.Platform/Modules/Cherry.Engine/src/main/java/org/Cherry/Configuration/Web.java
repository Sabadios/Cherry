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
package org.Cherry.Configuration;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Cristian.Malinescu
 * 
 */
public class Web implements Serializable {
  public Integer getHttpPort() {
    return _httpPort;
  }

  public void setHttpPort(final Integer httpPort) {
    _httpPort = httpPort;
  }

  public Set<String> getControllerNamespaces() {
    return _controllerNamespaces;
  }

  public void setControllerNamespaces(final Set<String> controllerNamespaces) {
    _controllerNamespaces = controllerNamespaces;
  }

  public String getDocRoot() {
    return _docRoot;
  }

  public void setDocRoot(final String docRoot) {
    _docRoot = docRoot;
  }

  public String getWelcomeDoc() {
    return _welcomeDoc;
  }

  public void setWelcomeDoc(final String welcomeDoc) {
    _welcomeDoc = welcomeDoc;
  }

  public Integer getMaxPostSize() {
    return _maxPostSize;
  }

  public void setMaxPostSize(final Integer maxPostSize) {
    _maxPostSize = maxPostSize;
  }

  public Boolean getServerSessions() {
    return _serverSessions;
  }

  public void setServerSessions(final Boolean serverSessions) {
    _serverSessions = serverSessions;
  }

  public Cookie getCookie() {
    return _cookie;
  }

  public void setCookie(final Cookie cookie) {
    _cookie = cookie;
  }

  private Cookie _cookie;

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Configuration");

    return sb.toString();
  }

  private String _docRoot, _welcomeDoc;
  private Integer _httpPort, _maxPostSize;
  private Boolean _serverSessions;
  private Set<String> _controllerNamespaces;

  /**
   *
   */
  public Web() {
  }

  private static final long serialVersionUID = 1L;
}
