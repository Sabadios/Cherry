package org.Cherry.Configuration;

import java.io.Serializable;

public class Crypto implements Serializable {
  public Digest getDigest() {
    return _digest;
  }

  public void setDigest(final Digest digest) {
    _digest = digest;
  }

  private Digest _digest;

  public Crypto() {
  }

  private static final long serialVersionUID = 1L;
}
