package org.Cherry.Modules.Security.Crypto;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Configuration.ConfigurationService;
import org.Cherry.Core.ServiceTemplate;
import org.jasypt.digest.PooledStringDigester;
import org.jasypt.digest.StringDigester;

@Singleton
public class EncryptorService extends ServiceTemplate {
  public String digest(final String message) {
    return getStringDigester().digest(message);
  }

  public boolean matches(final String message, final String digest) {
    return getStringDigester().matches(message, digest);
  }

  private StringDigester getStringDigester() {
    if (null == _digester) {
      _digester = new PooledStringDigester();
      _digester.setAlgorithm(getConfigurationService().getDigest().getAlgorithm());
      _digester.setSaltSizeBytes(getConfigurationService().getDigest().getSaltSizeBytes());
      _digester.setIterations(getConfigurationService().getDigest().getIterations());
      _digester.setPoolSize(getConfigurationService().getDigest().getPoolSize());
    }
    return _digester;
  }

  private ConfigurationService getConfigurationService() {
    assert null != _configurationService;
    return _configurationService;
  }

  private PooledStringDigester _digester;

  @Inject
  @Singleton
  private ConfigurationService _configurationService;

  public EncryptorService() {
  }

  private static final long serialVersionUID = 1L;
}
