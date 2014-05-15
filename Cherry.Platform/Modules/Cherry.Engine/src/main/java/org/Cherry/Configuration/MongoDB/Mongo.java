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
package org.Cherry.Configuration.MongoDB;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Cristian.Malinescu
 * 
 */
public class Mongo {
  public List<ServerAddress> getServerAddresses() {
    if (null == _serverAddresses)
      _serverAddresses = new LinkedList<ServerAddress>();
    return _serverAddresses;
  }

  public void setServerAddresses(final List<ServerAddress> serverAddresses) {
    _serverAddresses = serverAddresses;
  }

  public String getDbName() {
    return _dbName;
  }

  public void setDbName(final String dbName) {
    _dbName = dbName;
  }

  private List<ServerAddress> _serverAddresses;
  private String _dbName;

  public Mongo() {
  }
}
