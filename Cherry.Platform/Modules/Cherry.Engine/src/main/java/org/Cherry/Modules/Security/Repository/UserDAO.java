/**
 *
 */
package org.Cherry.Modules.Security.Repository;

import org.Cherry.Modules.Security.Model.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.Mongo;

/**
 * @author Cristian.Malinescu
 * 
 */
public class UserDAO extends BasicDAO<User, ObjectId> {
  public UserDAO(final Mongo mongo, final Morphia morphia, final String dbName) {
    super(mongo, morphia, dbName);
  }
}
