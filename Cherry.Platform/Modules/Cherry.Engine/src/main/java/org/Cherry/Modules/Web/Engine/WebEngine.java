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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Configuration.ConfigurationService;
import org.Cherry.Core.ServiceTemplate;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;

@Singleton
class WebEngine extends ServiceTemplate {
  protected Integer getPort() {
    return getWebEngineConfig().getHttpPort();
  }

  private HttpProcessor getHttpProcessor() {
    if (null == _httpProcessor) {
      final HttpProcessorBuilder httpProcessorBuilder = HttpProcessorBuilder.create();

      httpProcessorBuilder.add(new ResponseDate());
      httpProcessorBuilder.add(new ResponseServer(getOriginServer()));
      httpProcessorBuilder.add(new ResponseContent());
      httpProcessorBuilder.add(new ResponseConnControl());
      httpProcessorBuilder.add(getRequestInterceptorService());
      httpProcessorBuilder.add(getResponseInterceptorService());

      _httpProcessor = httpProcessorBuilder.build();
    }

    return _httpProcessor;
  }

  private String getOriginServer() {
    return "Cherry Web Engine 1.0.0";
  }

  private UriHttpRequestHandlerMapper getRequestHandlerRegistry() {
    if (null == _registry) {
      _registry = new UriHttpRequestHandlerMapper();
      _registry.register("*", _requestDispatcher);
    }

    return _registry;
  }

  public HttpService getHttpService() {
    if (null == _httpService)
      _httpService = new HttpService(getHttpProcessor(), getRequestHandlerRegistry());

    return _httpService;
  }

  public HttpConnectionFactory<DefaultBHttpServerConnection> getHttpConnectionFactory() {
    if (null == _connectionFactory) _connectionFactory = DefaultBHttpServerConnectionFactory.INSTANCE;

    return _connectionFactory;
  }

  public ServerSocket getServerSocket() {
    if (null == _serverSocket) {
      try {
        _serverSocket = new ServerSocket(getPort());
      } catch (final IOException e) {
        throw new IllegalStateException(e);
      }

      info("Listening on port [{}]", _serverSocket.getLocalPort());
    }

    return _serverSocket;
  }

  private RequestInterceptor getRequestInterceptorService() {
    assert null != _requestInterceptor;
    return _requestInterceptor;
  }

  private ResponseInterceptor getResponseInterceptorService() {
    assert null != _responseInterceptor;
    return _responseInterceptor;
  }

  private ConfigurationService getWebEngineConfig() {
    assert null != _configurationService;
    return _configurationService;
  }

  @Inject
  @Singleton
  private ConfigurationService _configurationService;

  @Inject
  @Singleton
  private RequestDispatcher _requestDispatcher;

  @Inject
  @Singleton
  private RequestInterceptor _requestInterceptor;

  @Inject
  @Singleton
  private ResponseInterceptor _responseInterceptor;

  private HttpProcessor _httpProcessor;
  private HttpService _httpService;
  private UriHttpRequestHandlerMapper _registry;
  private HttpConnectionFactory<DefaultBHttpServerConnection> _connectionFactory;
  protected ServerSocket _serverSocket;

  @PostConstruct
  public void postConstruct() {
  }

  @PreDestroy
  public void preDestroy() {
  }

  protected WebEngine() {
  }

  /**
   *
   */
  private static final long serialVersionUID = 1L;
}
