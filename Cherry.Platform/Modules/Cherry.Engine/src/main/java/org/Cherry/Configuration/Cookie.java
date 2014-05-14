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

/**
 * @author Cristian.Malinescu
 * 
 */
public class Cookie implements Serializable {
  public String getName() {
    return _name;
  }

  public void setName(final String name) {
    _name = name;
  }

  public String getDomain() {
    return _domain;
  }

  public void setDomain(final String domain) {
    _domain = domain;
  }

  public Integer getVersion() {
    return _version;
  }

  public void setVersion(final Integer version) {
    _version = version;
  }

  public Long getTimeToLive() {
    return _timeToLive;
  }

  public void setTimeToLive(final Long timeToLive) {
    _timeToLive = timeToLive;
  }

  public String getPath() {
    return _path;
  }

  public void setPath(final String path) {
    _path = path;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Configuration");

    return sb.toString();
  }

  private String _name, _domain, _path;
  private Integer _version;
  private Long _timeToLive;

  /**
   *
   */
  public Cookie() {
  }

  private static final long serialVersionUID = 1L;
}
