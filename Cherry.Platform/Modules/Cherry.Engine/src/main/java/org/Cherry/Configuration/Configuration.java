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

import org.Cherry.Configuration.MongoDB.Mongo;

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

  public Concurrency getConcurrency() {
    return _concurrency;
  }

  public void setConcurrency(final Concurrency concurrency) {
    _concurrency = concurrency;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Configuration");

    sb.append(" profile=[").append(getProfile()).append("], version=[").append(getVersion()).append("].");

    return sb.toString();
  }

  public Web getWeb() {
    return _web;
  }

  public void setWeb(final Web web) {
    _web = web;
  }

  public Mongo getMongo() {
    return _mongo;
  }

  public void setMongo(final Mongo mongo) {
    _mongo = mongo;
  }

  private Concurrency _concurrency;
  private Web _web;
  private Mongo _mongo;

  private String _profile, _version;

  /**
   *
   */
  public Configuration() {
  }

  private static final long serialVersionUID = 1L;
}
