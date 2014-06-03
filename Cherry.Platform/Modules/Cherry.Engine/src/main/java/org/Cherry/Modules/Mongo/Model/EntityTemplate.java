/**
 *
 */
package org.Cherry.Modules.Mongo.Model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Version;

/**
 * @author Cristian.Malinescu
 * 
 */
public abstract class EntityTemplate implements Serializable {
  public ObjectId getId() {
    return id;
  }

  public void setId(final ObjectId _id) {
    assert null != _id;
    id = _id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(final Long _version) {
    assert null != version;
    version = _version;
  }

  @Id
  protected ObjectId id;

  @Version
  private Long version;

  protected EntityTemplate() {
  }

  private static final long serialVersionUID = 1L;
}
