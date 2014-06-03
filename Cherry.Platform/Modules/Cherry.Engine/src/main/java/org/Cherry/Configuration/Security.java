package org.Cherry.Configuration;

import java.io.Serializable;

public class Security implements Serializable {
  public Crypto getCrypto() {
    return _crypto;
  }

  public void setCrypto(final Crypto crypto) {
    _crypto = crypto;
  }

  private Crypto _crypto;

  public Security() {
  }

  private static final long serialVersionUID = 1L;
}
