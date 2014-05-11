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
package org.Cherry;

import static org.Cherry.Environment.getInstanceID;
import static org.Cherry.Environment.setInstanceID;

import java.util.UUID;

import org.Cherry.CDI.CDIService;
import org.Cherry.Core.ServiceTemplate;

/**
 * @author Cristian.Malinescu
 * 
 */
public final class Cherry extends ServiceTemplate {
  public CDIService getCDIService() {
    if (null == _cdiService) _cdiService = CDIService.start();

    return _cdiService;
  }

  public <T> T getBean(final Class<T> type) {
    return getCDIService().getBean(type);
  }

  public UUID ID() {
    return _id;
  }

  private final UUID _id;
  private CDIService _cdiService;

  private Cherry() {
    _id = UUID.randomUUID();

    setInstanceID(_id);

    info("Platform instance [{}] launching.", getInstanceID());
  }

  static public final Cherry instance() {
    return Holder.INSTANCE;
  }

  static private final class Holder {
    static public final Cherry INSTANCE = new Cherry();
  }

  /**
   *
   */
  private static final long serialVersionUID = 1L;
}
