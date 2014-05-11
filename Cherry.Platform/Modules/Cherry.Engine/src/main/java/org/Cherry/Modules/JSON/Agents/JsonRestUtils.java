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
package org.Cherry.Modules.JSON.Agents;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.lang.annotation.Annotation;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

public abstract class JsonRestUtils {
  static public final Produces JSONProducer = new Produces() {
    @Override
    public Class<? extends Annotation> annotationType() {
      return Produces.class;
    }

    @Override
    public String toString() {
      return JSON_MEDIA_TYPE_STR_REPR;
    }

    @Override
    public String[] value() {
      return DEFAULT_MEDIA_TYPES;
    }
  };

  static final String[] DEFAULT_MEDIA_TYPES = new String[] { APPLICATION_JSON };
  static final String JSON_MEDIA_TYPE_STR_REPR = "value=[" + APPLICATION_JSON + "]";

  static public final Consumes JSONConsumer = new Consumes() {
    @Override
    public Class<? extends Annotation> annotationType() {
      return Consumes.class;
    }

    @Override
    public String toString() {
      return JSON_MEDIA_TYPE_STR_REPR;
    }

    @Override
    public String[] value() {
      return DEFAULT_MEDIA_TYPES;
    }
  };

  private JsonRestUtils() {
    throw new IllegalStateException();
  }
}
