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
package org.Cherry.Utils;

import java.io.File;

public abstract class Utils {
  static final Integer ZERO_I = Integer.valueOf(0);
  static final Long ZERO_L = Long.valueOf(0);
  static final Float ZERO_F = Float.valueOf(0);
  static final Double ZERO_D = Double.valueOf(0);

  static public Boolean isEmpty(final String txt) {
    return null == txt || 0 == txt.trim().length();
  }

  static public Boolean isNotEmpty(final String txt) {
    return !isEmpty(txt);
  }

  static public void failIfEmpty(final String txt) {
    assert isNotEmpty(txt) : "Undefined text!";
  }

  static public void failIfEmpty(final String txt, final String msg) {
    if (isEmpty(msg))
      failIfEmpty(txt);
    else assert isEmpty(txt) : msg;
  }

  static public void failIfNegative(final Integer number) {
    assert null == number || isNegative(number) : "Undefined integer value!";
  }

  static public final Boolean isNegative(final Integer number) {
    return !isPositive(number);
  }

  static public final Boolean isPositive(final Integer number) {
    return null != number && 0 > ZERO_I.compareTo(number);
  }

  static public final Boolean isNegative(final Long number) {
    return !isPositive(number);
  }

  static public final Boolean isPositive(final Long number) {
    return null != number && 0 > ZERO_L.compareTo(number);
  }

  static public final Boolean isNegative(final Float number) {
    return null != number && 0 > ZERO_F.compareTo(number);
  }

  static public final Boolean isNegative(final Double number) {
    return null != number && 0 > ZERO_D.compareTo(number);
  }

  private Utils() {
    throw new IllegalStateException();
  }

  static public String path(final String... sgmnts) {
    StringBuilder sb = null;

    for (final String sgmnt : sgmnts) {
      if (null == sb) {
        sb = new StringBuilder(sgmnt);
        continue;
      }

      sb.append(File.separatorChar);
      sb.append(sgmnt);
    }

    return sb.toString();
  }
}
