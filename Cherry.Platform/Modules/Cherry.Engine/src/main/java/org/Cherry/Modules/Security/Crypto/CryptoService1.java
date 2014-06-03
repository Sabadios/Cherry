/**
 *
 */
package org.Cherry.Modules.Security.Crypto;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class CryptoService1 {
  public SecretKey getSecretKey() {
    if (null == key)
      try {
        key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(getKeySpec());
      } catch (InvalidKeySpecException | NoSuchAlgorithmException err) {
        throw new IllegalStateException(err);
      }

    return key;
  }

  public Cipher getECipher() {
    if (null == _ncrptCipher)
      try {
        _ncrptCipher = Cipher.getInstance(getSecretKey().getAlgorithm());
        _ncrptCipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), getAlgorithmParameterSpec());
      } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException err) {
        throw new IllegalStateException(err);
      }

    return _ncrptCipher;
  }

  public Cipher getDCipher() {
    if (null == _dcrptCipher)
      try {
        _dcrptCipher = Cipher.getInstance(getSecretKey().getAlgorithm());
        _dcrptCipher.init(Cipher.DECRYPT_MODE, getSecretKey(), getAlgorithmParameterSpec());
      } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException err) {
        throw new IllegalStateException(err);
      }

    return _dcrptCipher;
  }

  public KeySpec getKeySpec() {
    return new PBEKeySpec(getPassPhrase().toCharArray(), getSalt(), getiterationCount());
  }

  public AlgorithmParameterSpec getAlgorithmParameterSpec() {
    if (null == _paramSpec)
      _paramSpec = new PBEParameterSpec(getSalt(), getiterationCount());
    return _paramSpec;
  }

  public String getPassPhrase() {
    return _passPhrase;
  }

  protected String encrypt(final String str) {
    byte[] bits;

    try {
      bits = str.getBytes(getCharset());
    } catch (final UnsupportedEncodingException err) {
      throw new IllegalStateException(err);
    }

    try {
      bits = getECipher().doFinal(bits);
    } catch (IllegalBlockSizeException | BadPaddingException err) {
      throw new IllegalStateException(err);
    }

    return new sun.misc.BASE64Encoder().encode(bits);
  }

  protected String decrypt(final String str) {
    byte[] bits;

    try {
      bits = new sun.misc.BASE64Decoder().decodeBuffer(str);
    } catch (final IOException err) {
      throw new IllegalStateException(err);
    }

    try {
      bits = getDCipher().doFinal(bits);
    } catch (IllegalBlockSizeException | BadPaddingException err) {
      throw new IllegalStateException(err);
    }

    try {
      return new String(bits, getCharset());
    } catch (final UnsupportedEncodingException err) {
      throw new IllegalStateException(err);
    }
  }

  public byte[] getSalt() {
    return _salt;
  }

  public Integer getiterationCount() {
    return _iterationCount;
  }

  private String getCharset() {
    return _charsetName;
  }

  private Cipher _dcrptCipher, _ncrptCipher;
  private final byte[] _salt;// { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3,
                             // (byte) 0x03 };
  private final Integer _iterationCount;
  private SecretKey key;
  private final String _passPhrase;
  private AlgorithmParameterSpec _paramSpec;
  private final String _charsetName;

  CryptoService1(final String passPhrase, final byte[] salt, final Integer iterationCount, final String charsetName) {
    _passPhrase = passPhrase;
    _salt = salt;
    _iterationCount = iterationCount;
    _charsetName = charsetName;
  }

  /**
   * @param args
   */
  public static void main(final String[] args) {
    final byte[] salt = new byte[8], base = UUID.randomUUID().toString().getBytes();

    for (int i = 0, j = 0; i < base.length; i++)
      if (0 == i % 5 && j < 8)
        salt[j++] = base[i];

    final String passPhrase = UUID.randomUUID().toString();
    final CryptoService1 crypto = new CryptoService1(passPhrase, salt, 19, "UTF-8");

    final String encrypted = crypto.encrypt("_P@$$W0rD+");

    System.out.println("encrypted String:" + encrypted);

    final String decrypted = crypto.decrypt(encrypted);

    System.out.println("decrypted String:" + decrypted);
  }
}
