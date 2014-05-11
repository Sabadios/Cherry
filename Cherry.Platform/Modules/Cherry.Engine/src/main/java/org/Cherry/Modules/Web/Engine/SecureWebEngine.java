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
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * @author Cristian.Malinescu
 * 
 */
@Alternative
@Specializes
final class SecureWebEngine extends WebEngine {
  @Override
  public ServerSocket getServerSocket() {
    if (null == _serverSocket) {
      try {
        _serverSocket = getSSLServerSocketFactory().createServerSocket(getPort());
      } catch (final IOException e) {
        throw new IllegalStateException(e);
      }

      info("Listening on port [{}]", _serverSocket.getLocalPort());
    }

    return _serverSocket;
  }

  private SSLServerSocketFactory getSSLServerSocketFactory() {
    return _sslSrvrScktFctry;
  }

  private final SSLServerSocketFactory _sslSrvrScktFctry;

  /**
   * @throws KeyStoreException
   * @throws NoSuchAlgorithmException
   * @throws IOException
   * @throws CertificateException
   * @throws UnrecoverableKeyException
   * @throws KeyManagementException
   * @throws KeyStoreNotFoundError
   * 
   */
  public SecureWebEngine() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException,
      KeyManagementException, KeyStoreNotFoundError {
    final ClassLoader cl = WebEngine.class.getClassLoader();
    final URL url = cl.getResource("my.keystore");

    if (url == null) throw new KeyStoreNotFoundError();

    final KeyStore keystore = KeyStore.getInstance("jks");
    keystore.load(url.openStream(), "secret".toCharArray());

    final KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmfactory.init(keystore, "secret".toCharArray());

    final KeyManager[] keymanagers = kmfactory.getKeyManagers();
    final SSLContext sslcontext = SSLContext.getInstance("TLS");

    sslcontext.init(keymanagers, null, null);
    _sslSrvrScktFctry = sslcontext.getServerSocketFactory();
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
}
