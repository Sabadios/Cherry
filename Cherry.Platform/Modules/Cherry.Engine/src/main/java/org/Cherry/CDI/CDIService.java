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
package org.Cherry.CDI;

import org.Cherry.Core.ServiceTemplate;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * @author Cristian.Malinescu
 * 
 */
public final class CDIService extends ServiceTemplate {
  public CDIService initialize() {
    assert null != getWeldContainer();
    info("Instance [{}] initialized for [{}].", this, getWeldContainer());
    return this;
  }

  public void shutdown() {
    info("Instance [{}] will shutdown [{}].", this, getWeldContainer());
    getWeld().shutdown();
  }

  public <T> T getBean(final Class<T> type) {
    return getWeldContainer().instance().select(type).get();
  }

  Weld getWeld() {
    if (null == _weld)
      _weld = new Weld();

    return _weld;
  }

  WeldContainer getWeldContainer() {
    if (null == _weldContainer)
      _weldContainer = getWeld().initialize();

    return _weldContainer;
  }

  private Weld _weld;
  private WeldContainer _weldContainer;

  /**
   *
   */
  private CDIService() {
  }

  static public CDIService start() {
    return getInstance().initialize();
  }

  static public CDIService getInstance() {
    return Holder.getInstance();
  }

  static private final class Holder {
    static CDIService getInstance() {
      return INSTANCE;
    }

    static private final CDIService INSTANCE = new CDIService();
  }

  /**
   *
   */
  static private final long serialVersionUID = 1L;

}
