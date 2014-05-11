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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Core.ServiceTemplate;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.protocol.HttpService;
import org.jboss.weld.exceptions.IllegalArgumentException;

@Singleton
@NotThreadSafe
public class WebEngineManager extends ServiceTemplate {
  class RequestWorkerManager extends ServiceTemplate implements Runnable {
    @Override
    public void run() {
      while (!Thread.interrupted())
        try {
          getExecutorService().execute(getRequestWorker());
        } catch (final Throwable t) {
          error(t, t.getMessage());
          break;
        }
    }

    private static final long serialVersionUID = 1L;
  }

  public Runnable getRequestWorkerManager() {
    if (null == _requestWorkerManager)
      _requestWorkerManager = new RequestWorkerManager();

    return _requestWorkerManager;
  }

  HttpService getHttpService() {
    return getWebEngine().getHttpService();
  }

  private Socket getSocket() throws IOException {
    return getServerSocket().accept();
  }

  private ServerSocket getServerSocket() throws IOException {
    return getWebEngine().getServerSocket();
  }

  RequestWorker getRequestWorker() throws IOException {
    return new RequestWorker(getHttpService(), getHttpServerConnection());
  }

  HttpServerConnection getHttpServerConnection() throws IOException {
    final Socket socket = getSocket();

    if (null == socket)
      throw new IllegalStateException(new IllegalArgumentException(new NullPointerException("Undefined 'java.net.Socket' returned by underlying framework!")));

    debug("Incoming connection from [{}]", socket.getInetAddress());

    return getHttpConnectionFactory().createConnection(socket);
  }

  private HttpConnectionFactory<DefaultBHttpServerConnection> getHttpConnectionFactory() {
    return getWebEngine().getHttpConnectionFactory();
  }

  public ExecutorService getExecutorService() {
    assert null != _executorService;
    return _executorService;
  }

  private WebEngine getWebEngine() {
    assert null != _webEngine;
    return _webEngine;
  }

  private Runnable _requestWorkerManager;

  @Inject
  @Singleton
  private WebEngine _webEngine;

  @Inject
  @Singleton
  private ExecutorService _executorService;

  public WebEngineManager() {
  }

  private static final long serialVersionUID = 1L;
}