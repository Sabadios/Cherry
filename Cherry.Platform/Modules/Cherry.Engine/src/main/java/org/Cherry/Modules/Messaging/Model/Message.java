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

package org.Cherry.Modules.Messaging.Model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonCreator;

abstract class Message<T> implements Serializable {
  public void setHead(final Head head) {
    if (null == head)
      throw new IllegalArgumentException("Undefined header set by the caller/client ?!");
    _head = head;
  }

  public Head getHead() {
    if (null == _head) _head = new Head();
    return _head;
  }

  public void setBody(final Body<T> body) {
    if (null == body)
      throw new IllegalArgumentException("Undefined body set by the caller/client ?!");
    _body = body;
  }

  public Body<T> getBody() {
    if (null == _body) _body = new Body<T>();
    return _body;
  }

  public Status getStatus() {
    if (null == _status) _status = Status.UNKNOWN;
    return _status;
  }

  public void setStatus(final Status status) {
    if (null == status)
      throw new IllegalArgumentException("Undefined status set by the caller/client ?!");
    _status = status;
  }

  public void setID(final Long id) {
    if (null == id)
      throw new IllegalArgumentException("Undefined ID set by the caller/client ?!");
    _ID = id;
  }

  public Long getID() {
    return _ID;
  }

  @Override
  public int hashCode() {
    return _ID.intValue();
  }

  @Override
  public String toString() {
    return _ID.toString();
  }

  @Override
  public boolean equals(final Object other) {
    if (null == other) return false;

    if (this == other) return true;

    if (other instanceof Message) return getID().equals(((Message<?>) other).getID());

    return false;
  }

  private Head _head;
  private Body<T> _body;
  private Status _status;
  private Long _ID;

  @JsonCreator
  public Message() {
    _ID = Counter.incrementAndGet();
  }

  static private final AtomicLong Counter = new AtomicLong();

  private static final long serialVersionUID = 1L;
}
