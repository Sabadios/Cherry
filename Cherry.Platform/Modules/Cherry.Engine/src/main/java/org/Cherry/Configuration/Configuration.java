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
public class Configuration implements Serializable {
  public String getProfile() {
    return _profile;
  }

  public void setProfile(final String profile) {
    _profile = profile;
  }

  public String getVersion() {
    return _version;
  }

  public void setVersion(final String version) {
    _version = version;
  }

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

  public String getSessionCookie() {
    return _sessionCookie;
  }

  public void setSessionCookie(final String sessionCookie) {
    _sessionCookie = sessionCookie;
  }

  public String getCookieDomain() {
    return _cookieDomain;
  }

  public void setCookieDomain(final String cookieDomain) {
    _cookieDomain = cookieDomain;
  }

  public Integer getCookieVersion() {
    return _cookieVersion;
  }

  public void setCookieVersion(final Integer cookieVersion) {
    _cookieVersion = cookieVersion;
  }

  public Long getCookieTimeToLive() {
    return _cookieTimeToLive;
  }

  public void setCookieTimeToLive(final Long cookieTimeToLive) {
    _cookieTimeToLive = cookieTimeToLive;
  }

  public String getCookiePath() {
    return _cookiePath;
  }

  public void setCookiePath(final String cookiePath) {
    _cookiePath = cookiePath;
  }

  public Integer getMaxPostSize() {
    return _maxPostSize;
  }

  public void setMaxPostSize(final Integer maxPostSize) {
    _maxPostSize = maxPostSize;
  }

  public Integer getCorePoolSize() {
    return _corePoolSize;
  }

  public Integer getMaximumPoolSize() {
    return _maximumPoolSize;
  }

  public void setCorePoolSize(final Integer corePoolSize) {
    _corePoolSize = corePoolSize;
  }

  public void setMaximumPoolSize(final Integer maximumPoolSize) {
    _maximumPoolSize = maximumPoolSize;
  }

  public Long getKeepAliveTime() {// 60L
    return _keepAliveTime;
  }

  public void setKeepAliveTime(final Long keepAliveTime) {
    _keepAliveTime = keepAliveTime;
  }

  public Boolean getServerSessions() {
    return _serverSessions;
  }

  public void setServerSessions(final Boolean serverSessions) {
    _serverSessions = serverSessions;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Configuration");

    sb.append(" profile=[").append(getProfile()).append("], version=[").append(getVersion()).append("].");

    return sb.toString();
  }

  private String _profile, _version, _docRoot, _welcomeDoc, _sessionCookie, _cookieDomain, _cookiePath;
  private Integer _httpPort, _cookieVersion, _maxPostSize, _corePoolSize, _maximumPoolSize;
  private Long _cookieTimeToLive, _keepAliveTime;
  private Boolean _serverSessions;
  private Set<String> _controllerNamespaces;

  /**
   *
   */
  public Configuration() {
  }

  private static final long serialVersionUID = 1L;
}
