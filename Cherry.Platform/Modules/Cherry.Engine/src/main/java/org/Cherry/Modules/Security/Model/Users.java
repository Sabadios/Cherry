package org.Cherry.Modules.Security.Model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Users implements Serializable {
  public Set<User> getUsers() {
    if (null == _users)
      _users = new HashSet<User>();

    return _users;
  }

  public void setUsers(final Set<User> users) {
    _users = users;
  }

  private Set<User> _users;

  public Users(final Set<User> users) {
    getUsers().addAll(users);
  }

  public Users() {
  }

  private static final long serialVersionUID = 1L;
}
