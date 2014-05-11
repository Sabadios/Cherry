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

import static org.Cherry.Modules.Web.WebConstants.ROOT_URI;
import static org.Cherry.Utils.Utils.path;

public abstract class ConfigurationConstants {
  static public final String USER_HOME = System.getProperty("user.home");
  static public final String USER_DIR = System.getProperty("user.dir");
  static public final Integer HTTP_PORT = 8080, COOKIE_VERSION = 1, MAX_POST_SIZE = 2 * 1024 * 1024;
  static public final Long COOKIE_TTL = 60L;
  static public final String WELCOME_DOC = "index.html", SESSION_COOKIE = "JSESSIONID", COOKIE_DOMAIN = "localhost", COOKIE_PATH = ROOT_URI;
  static public final String ROOT_SGMNT = USER_HOME,
      HOME_SGMNT = ".sabadios",
      PLATFORM_SGMNT = ".platform",
      DOMAIN_SGMNT = ".http",
      MODULE_SGMNT = ".server",
      DOC_ROOT_SGMNT = "www",
      SERVER_HOME = path(ROOT_SGMNT, HOME_SGMNT, PLATFORM_SGMNT, DOMAIN_SGMNT, MODULE_SGMNT),
      SERVER_CONFIG = "server-config.json",
      DOC_ROOT = path(SERVER_HOME, DOC_ROOT_SGMNT);

  private ConfigurationConstants() {
    throw new IllegalStateException();
  }
}
