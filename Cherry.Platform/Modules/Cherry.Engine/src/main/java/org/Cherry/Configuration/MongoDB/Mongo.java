package org.Cherry.Configuration.MongoDB;

import java.util.LinkedList;
import java.util.List;

public class Mongo {
  public List<ServerAddress> getServerAddresses() {
    if (null == _serverAddresses)
      _serverAddresses = new LinkedList<ServerAddress>();
    return _serverAddresses;
  }

  public void setServerAddresses(final List<ServerAddress> serverAddresses) {
    _serverAddresses = serverAddresses;
  }

  public String getDbName() {
    return _dbName;
  }

  public void setDbName(final String dbName) {
    _dbName = dbName;
  }

  private List<ServerAddress> _serverAddresses;
  private String _dbName;

  public Mongo() {
  }
}
