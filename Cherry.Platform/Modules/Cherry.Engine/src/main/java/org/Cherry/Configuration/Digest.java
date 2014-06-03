package org.Cherry.Configuration;

import java.io.Serializable;

public class Digest implements Serializable {
  public String getAlgorithm() {
    return _algorithm;
  }

  public void setAlgorithm(final String algorithm) {
    _algorithm = algorithm;
  }

  public Integer getSaltSizeBytes() {
    return _saltSizeBytes;
  }

  public void setSaltSizeBytes(final Integer saltSizeBytes) {
    _saltSizeBytes = saltSizeBytes;
  }

  public Integer getIterations() {
    return _iterations;
  }

  public void setIterations(final Integer iterations) {
    _iterations = iterations;
  }

  public Integer getPoolSize() {
    return _poolSize;
  }

  public void setPoolSize(final Integer poolSize) {
    _poolSize = poolSize;
  }

  private String _algorithm;
  private Integer _saltSizeBytes, _iterations, _poolSize;

  public Digest() {
  }

  private static final long serialVersionUID = 1L;
}
