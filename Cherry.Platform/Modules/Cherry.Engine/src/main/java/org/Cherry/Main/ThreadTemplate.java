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
package org.Cherry.Main;

/**
 * @author Cristian.Malinescu
 * 
 */
public abstract class ThreadTemplate extends Thread {
  /**
   * @param target
   */
  public ThreadTemplate(final Runnable target) {
    super(target);
  }

  /**
   * @param name
   */
  public ThreadTemplate(final String name) {
    super(name);
  }

  /**
   * @param group
   * @param target
   */
  public ThreadTemplate(final ThreadGroup group, final Runnable target) {
    super(group, target);
  }

  /**
   * @param group
   * @param name
   */
  public ThreadTemplate(final ThreadGroup group, final String name) {
    super(group, name);
  }

  /**
   * @param target
   * @param name
   */
  public ThreadTemplate(final Runnable target, final String name) {
    super(target, name);
  }

  /**
   * @param group
   * @param target
   * @param name
   */
  public ThreadTemplate(final ThreadGroup group, final Runnable target, final String name) {
    super(group, target, name);
  }

  /**
   * @param group
   * @param target
   * @param name
   * @param stackSize
   */
  public ThreadTemplate(final ThreadGroup group, final Runnable target, final String name, final long stackSize) {
    super(group, target, name, stackSize);
  }

  /**
   * 
   */
  public ThreadTemplate() {
  }
}
