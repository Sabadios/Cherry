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
package org.Cherry.Modules.Web.Engine;

import javax.inject.Singleton;

import org.Cherry.Core.ServiceTemplate;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;

@Singleton
class TracerService extends ServiceTemplate {
  public void examine(final HttpRequest request) {
    if (log.isDebugEnabled()) {
      final Header[] headers = request.getAllHeaders();
      Header header;
      HeaderElement[] elements;
      HeaderElement element;
      NameValuePair[] nameValues;
      NameValuePair nameValue;
      final int headerCnt = headers.length;
      int elementCnt, nameValCnt;

      for (int i = 0; i < headerCnt; i++) {
        header = headers[i];
        log.debug(" {'request_header_{}':{'{}':'{}'}}", i, header.getName(), header.getValue());

        elements = header.getElements();
        elementCnt = elements.length;

        for (int j = 0; j < elementCnt; j++) {
          element = elements[j];
          log.debug("  {'header_element_{}.{}':{'{}':'{}'}}", i, j, element.getName(), element.getValue());

          nameValues = element.getParameters();
          nameValCnt = nameValues.length;

          for (int k = 0; k < nameValCnt; k++) {
            nameValue = nameValues[k];
            log.debug("   {'name_value_{}.{}.{}':{'{}':'{}'}}", i, j, k, nameValue.getName(), nameValue.getValue());
          }
        }
      }
    }
  }

  public TracerService() {
  }

  static private final Logger log = Loggers.Tracer.getLogger();

  private static final long serialVersionUID = 1L;
}
