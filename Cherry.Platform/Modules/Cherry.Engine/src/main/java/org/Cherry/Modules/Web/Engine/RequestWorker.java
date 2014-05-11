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

import java.io.Serializable;

import org.Cherry.Cherry;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RequestWorker implements Runnable, Serializable {
  @Override
  public void run() {
    log.debug("New connection thread");

    final HttpContext context = new BasicHttpContext(null);

    while (getHttpServerConnection().isOpen()) {
      if (Thread.interrupted())
        throw new IllegalStateException(new InterruptedException());

      try {
        getHttpService().handleRequest(getHttpServerConnection(), context);
      } catch (final ConnectionClosedException err) {
        log.debug("[{}]", err.getMessage());
        break;
      } catch (final Throwable err) {
        log.error(err.getMessage(), err);
        break;
      }
    }

    getMetricsService().examine(getHttpServerConnection());
  }

  private HttpService getHttpService() {
    return _httpService;
  }

  private HttpServerConnection getHttpServerConnection() {
    return _httpServerConnection;
  }

  private MetricsService getMetricsService() {
    if (null == _metricsService)
      _metricsService = Cherry.instance().getBean(MetricsService.class);

    return _metricsService;
  }

  private final HttpService _httpService;
  private final HttpServerConnection _httpServerConnection;
  private MetricsService _metricsService;

  RequestWorker(final HttpService httpService, final HttpServerConnection connection) {
    super();

    _httpService = httpService;
    _httpServerConnection = connection;
  }

  static private final Logger log = LoggerFactory.getLogger("Cherry-Modules-Web-Metrics");

  private static final long serialVersionUID = 1L;
}