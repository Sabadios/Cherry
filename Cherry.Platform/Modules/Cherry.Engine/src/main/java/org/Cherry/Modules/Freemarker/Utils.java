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
package org.Cherry.Modules.Freemarker;

public class Utils {
  static final Integer ZERO_I = Integer.valueOf(0);
  static final Long ZERO_L = Long.valueOf(0);
  static final Float ZERO_F = Float.valueOf(0);
  static final Double ZERO_D = Double.valueOf(0);

  static public final String
      WRAPPER_SIMPLE = "simple",
      WRAPPER_BEANS = "beans",
      TEMPLATE_EXCEPTION_HANDLER = "TemplateExceptionHandler",
      TEMPLATE_EXCEPTION_HANDLER_RETHROW = "rethrow",
      TEMPLATE_EXCEPTION_HANDLER_DEBUG = "debug",
      TEMPLATE_EXCEPTION_HANDLER_HTML_DEBUG = "htmlDebug",
      TEMPLATE_EXCEPTION_HANDLER_IGNORE = "ignore",
      KEY_REQUEST = "Request";

  static final Boolean isNegative(final Integer number) {
    assert null != number;
    return 0 > ZERO_I.compareTo(number);
  }

  static final Boolean isNegative(final Long number) {
    assert null != number;
    return 0 > ZERO_L.compareTo(number);
  }

  static final Boolean isNegative(final Float number) {
    assert null != number;
    return 0 > ZERO_F.compareTo(number);
  }

  static final Boolean isNegative(final Double number) {
    assert null != number;
    return 0 > ZERO_D.compareTo(number);
  }
}
