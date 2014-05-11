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
package org.Cherry.Core;

import static org.Cherry.CDI.Utils.count;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian.Malinescu
 * 
 */
public abstract class ServiceTemplate extends EntityTemplate {
  protected void info(final String format, final Object... messages) {
    log().info(format, messages);
  }

  protected void debug(final String format, final Object... messages) {
    log().debug(format, messages);
  }

  protected void debug(final Object message) {
    log().debug("{}", message);
  }

  protected void warn(final String format, final Object... messages) {
    log().warn(format, messages);
  }

  protected void error(final Throwable t, final String message) {
    log().error(message, t);
  }

  protected void error(final String format, final Object... messages) {
    log().error(format, messages);
  }

  protected Boolean isDebugEnabled() {
    return _log.isDebugEnabled();
  }

  protected Logger log() {
    if (null == _log)
      _log = LoggerFactory.getLogger(getClass());
    return _log;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    final Object clone = super.clone();
    return clone;
  }

  private Logger _log;

  protected ServiceTemplate() {
    info("Instance [{}] created with ordinal [{}].", this, count());
  }

  static private final long serialVersionUID = 1L;
}
