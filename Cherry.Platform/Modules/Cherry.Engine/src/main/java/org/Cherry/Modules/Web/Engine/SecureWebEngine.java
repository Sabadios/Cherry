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
// @Singleton
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
    if (null == _sslSrvrScktFctry) {
      final URL url = load();

      KeyStore keyStore;

      try {
        keyStore = KeyStore.getInstance(_keyStoreType);
      } catch (final KeyStoreException err) {
        throw new IllegalStateException(err);
      }

      try {
        keyStore.load(url.openStream(), password);
      } catch (NoSuchAlgorithmException | CertificateException | IOException err) {
        throw new IllegalStateException(err);
      }

      KeyManagerFactory keyMngrFctry;

      try {
        keyMngrFctry = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      } catch (final NoSuchAlgorithmException err) {
        throw new IllegalStateException(err);
      }

      try {
        keyMngrFctry.init(keyStore, password);
      } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException err) {
        throw new IllegalStateException(err);
      }

      final KeyManager[] keyMngrs = keyMngrFctry.getKeyManagers();

      SSLContext sslCntxt;

      try {
        sslCntxt = SSLContext.getInstance(_sslProtocol);
      } catch (final NoSuchAlgorithmException err) {
        throw new IllegalStateException(err);
      }

      try {
        sslCntxt.init(keyMngrs, null, null);
      } catch (final KeyManagementException err) {
        throw new IllegalStateException(err);
      }

      _sslSrvrScktFctry = sslCntxt.getServerSocketFactory();
    }

    return _sslSrvrScktFctry;
  }

  private URL load() {
    final ClassLoader classLdr = this.getClass().getClassLoader();

    final URL url = classLdr.getResource("my.keystore");

    if (url == null) throw new IllegalStateException(new KeyStoreNotFoundError());

    return url;
  }

  private SSLServerSocketFactory _sslSrvrScktFctry;
  private final String _keyStoreType = "jks",
      _sslProtocol = "TLS";
  private final char[] password = "secret".toCharArray();

  public SecureWebEngine() {
  }

  /**
   *
   */
  private static final long serialVersionUID = 1L;
}
