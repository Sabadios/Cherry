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

import java.lang.ref.WeakReference;

import javax.inject.Singleton;

import org.Cherry.Core.ServiceTemplate;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpServerConnection;
import org.slf4j.Logger;

@Singleton
class MetricsService extends ServiceTemplate {
  public void examine(final HttpServerConnection connection) {
    if (null != connection &&
        log.isDebugEnabled() &&
        !connection.isOpen()) {
      log.debug("Connection [{}] closed. Capturing metrics.", connection);

      final HttpConnectionMetrics metrics = connection.getMetrics();

      final WeakReference<StringBuilder> sb = new WeakReference<StringBuilder>(new StringBuilder("{'connection_metrics':{'requests':'"));

      sb.get().append(metrics.getRequestCount()).append("','responses':'").append(metrics.getResponseCount())
          .append("', 'received_bytes':'").append(metrics.getReceivedBytesCount()).append("', 'sent_bytes':'").append(metrics.getSentBytesCount()).append("'}}");

      log.debug("[{}]", sb.get().toString());
    }
  }

  public MetricsService() {
  }

  static private final Logger log = Loggers.Metrics.getLogger();

  private static final long serialVersionUID = 1L;
}
