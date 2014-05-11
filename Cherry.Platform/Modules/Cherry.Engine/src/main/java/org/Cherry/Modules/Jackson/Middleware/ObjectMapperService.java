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
package org.Cherry.Modules.Jackson.Middleware;

import static com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS;
import static com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_INTEGER_FOR_INTS;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
public class ObjectMapperService extends ObjectMapper {
  @PostConstruct
  void postConstruct() {
    enable(USE_BIG_DECIMAL_FOR_FLOATS, USE_BIG_INTEGER_FOR_INTS);
  }

  @PreDestroy
  void preDestroy() {
  }

  public ObjectMapperService() {
    log.info("Instance [{}] created.", this);
  }

  public ObjectMapperService(final JsonFactory jf) {
    super(jf);
    log.info("Instance [{}] created.", this);
  }

  public ObjectMapperService(final ObjectMapper src) {
    super(src);
    log.info("Instance [{}] created.", this);
  }

  public ObjectMapperService(final JsonFactory jf, final DefaultSerializerProvider sp, final DefaultDeserializationContext dc) {
    super(jf, sp, dc);
    log.info("Instance [{}] created.", this);
  }

  static private final Logger log = LoggerFactory.getLogger(ObjectMapperService.class);

  private static final long serialVersionUID = 1L;
}
