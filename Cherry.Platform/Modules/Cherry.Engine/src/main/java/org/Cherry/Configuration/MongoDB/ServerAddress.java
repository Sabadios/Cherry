/**
 *
 */
package org.Cherry.Configuration.MongoDB;

import java.net.UnknownHostException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Cristian.Malinescu
 * 
 */
public final class ServerAddress extends com.mongodb.ServerAddress {
  public com.mongodb.ServerAddress asServerAddress() {
    try {
      return new com.mongodb.ServerAddress(getHost(), getPort());
    } catch (final UnknownHostException err) {
      throw new IllegalStateException(err);
    }
  }

  /**
   * @throws UnknownHostException
   * 
   */
  @JsonCreator
  public ServerAddress(@JsonProperty("host") final String host, @JsonProperty("port") final Integer port) throws UnknownHostException {
    super(host, port);
  }
}
