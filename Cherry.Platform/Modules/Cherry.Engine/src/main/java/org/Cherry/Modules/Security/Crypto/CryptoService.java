package org.Cherry.Modules.Security.Crypto;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Core.ServiceTemplate;

@Singleton
public class CryptoService extends ServiceTemplate {
  /**
   * Encrypts (digests) a password.
   * 
   * @param password
   *          the password to be encrypted.
   * @return the resulting digest.
   */
  public String encryptParole(final String password) {
    return getEncryptor().digest(password);
  }

  /**
   * Checks an unencrypted (plain) password against an encrypted one (a digest) to see if they match.
   * 
   * @param plainPassword
   *          the plain password to check.
   * @param encryptedPassword
   *          the digest against which to check the password.
   * @return true if passwords match, false if not.
   */
  public boolean checkParole(final String plainPassword, final String encryptedPassword) {
    return getEncryptor().matches(plainPassword, encryptedPassword);
  }

  public EncryptorService getEncryptor() {
    assert null != _encryptor;
    return _encryptor;
  }

  @Inject
  @Singleton
  private EncryptorService _encryptor;

  public CryptoService() {
  }

  private static final long serialVersionUID = 1L;
}
