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
package org.Cherry.Configuration;

import static org.Cherry.Configuration.ConfigurationConstants.COOKIE_DOMAIN;
import static org.Cherry.Configuration.ConfigurationConstants.COOKIE_PATH;
import static org.Cherry.Configuration.ConfigurationConstants.COOKIE_TTL;
import static org.Cherry.Configuration.ConfigurationConstants.COOKIE_VERSION;
import static org.Cherry.Configuration.ConfigurationConstants.DB_NAME;
import static org.Cherry.Configuration.ConfigurationConstants.DOC_ROOT;
import static org.Cherry.Configuration.ConfigurationConstants.HTTP_PORT;
import static org.Cherry.Configuration.ConfigurationConstants.MAX_POST_SIZE;
import static org.Cherry.Configuration.ConfigurationConstants.SESSION_COOKIE;
import static org.Cherry.Configuration.ConfigurationConstants.WELCOME_DOC;
import static org.Cherry.Utils.Utils.isNotEmpty;
import static org.Cherry.Utils.Utils.isPositive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Configuration.Hazelcast.HazelCast;
import org.Cherry.Configuration.MongoDB.ServerAddress;
import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Main.ApplicationKey;
import org.Cherry.Main.ApplicationRepositoryService;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
public class ConfigurationService extends ServiceTemplate {
  public Integer getHttpPort() {
    if (null != getConfiguration().getWeb().getHttpPort())
      return getConfiguration().getWeb().getHttpPort();

    return _httpPort;
  }

  public String getDocRoot() {
    if (isNotEmpty(getConfiguration().getWeb().getDocRoot()))
      return getConfiguration().getWeb().getDocRoot();

    return DOC_ROOT;
  }

  public String getWelcomeDoc() {
    if (isNotEmpty(getConfiguration().getWeb().getWelcomeDoc()))
      return getConfiguration().getWeb().getWelcomeDoc();

    return WELCOME_DOC;
  }

  public String getSessionCookie() {
    if (isNotEmpty(getConfiguration().getWeb().getCookie().getName()))
      return getConfiguration().getWeb().getCookie().getName();

    return SESSION_COOKIE;
  }

  public String getCookieDomain() {
    if (isNotEmpty(getConfiguration().getWeb().getCookie().getDomain()))
      return getConfiguration().getWeb().getCookie().getDomain();

    return COOKIE_DOMAIN;
  }

  public Integer getCookieVersion() {
    if (isPositive(getConfiguration().getWeb().getCookie().getVersion()))
      return getConfiguration().getWeb().getCookie().getVersion();

    return COOKIE_VERSION;
  }

  public Long getCookieTimeToLive() {
    if (isPositive(getConfiguration().getWeb().getCookie().getTimeToLive()))
      return getConfiguration().getWeb().getCookie().getTimeToLive();

    return COOKIE_TTL;
  }

  public String getCookiePath() {
    if (isNotEmpty(getConfiguration().getWeb().getCookie().getPath()))
      return getConfiguration().getWeb().getCookie().getPath();

    return COOKIE_PATH;
  }

  public Integer getMaxPostSize() {
    if (isPositive(getConfiguration().getWeb().getMaxPostSize()))
      return getConfiguration().getWeb().getMaxPostSize();

    return MAX_POST_SIZE;
  }

  public Integer getCorePoolSize() {
    if (isPositive(getConfiguration().getConcurrency().getCorePoolSize()))
      return getConfiguration().getConcurrency().getCorePoolSize();

    return Runtime.getRuntime().availableProcessors();
  }

  public Integer getMaximumPoolSize() {
    if (isPositive(getConfiguration().getConcurrency().getMaximumPoolSize()))
      return getConfiguration().getConcurrency().getMaximumPoolSize();

    return Integer.MAX_VALUE;
  }

  public Long getKeepAliveTime() {// 60L
    if (isPositive(getConfiguration().getConcurrency().getKeepAliveTime()))
      return getConfiguration().getConcurrency().getKeepAliveTime();

    return 60L;
  }

  public Boolean getServerSessions() {
    if (null != getConfiguration().getWeb().getServerSessions())
      return getConfiguration().getWeb().getServerSessions();

    return false;
  }

  public List<ServerAddress> getServerAddresses() {
    if (null != getConfiguration().getMongo().getServerAddresses())
      return getConfiguration().getMongo().getServerAddresses();

    return Collections.emptyList();
  }

  public String getDBName() {
    if (isNotEmpty(getConfiguration().getMongo().getDbName()))
      return getConfiguration().getMongo().getDbName();

    return DB_NAME;
  }

  public Digest getDigest() {
    return getConfiguration().getSecurity().getCrypto().getDigest();
  }

  private HazelCast getHazelcast() {
    return getConfiguration().getHazelcast();
  }

  private InputStream getHazelcastConfig() {
    try {
      return new FileInputStream(new File(getHazelcast().getConfigFile()));
    } catch (final FileNotFoundException err) {
      throw new IllegalStateException(err);
    }
  }

  public Config getConfig() {
    try {
      return new XmlConfigBuilder(getHazelcastConfig()).build();
    } catch (final Throwable t) {
      error(t, t.getMessage());
    }

    return new XmlConfigBuilder().build();
  }

  private Configuration getConfiguration() {
    final Configuration configuration = (Configuration) getApplicationRepositoryService().get(ApplicationKey.Configuration).getValue();

    assert null != configuration;

    return configuration;
  }

  private ApplicationRepositoryService getApplicationRepositoryService() {
    assert null != _applicationRepositoryService;
    return _applicationRepositoryService;
  }

  @Inject
  @Singleton
  private ApplicationRepositoryService _applicationRepositoryService;

  protected Integer _httpPort = HTTP_PORT;

  @PostConstruct
  protected void postConstruct() {
  }

  @PreDestroy
  protected void preDestroy() {
  }

  /**
   *
   */
  public ConfigurationService() {
  }

  /**
   *
   */
  private static final long serialVersionUID = 1L;
}
