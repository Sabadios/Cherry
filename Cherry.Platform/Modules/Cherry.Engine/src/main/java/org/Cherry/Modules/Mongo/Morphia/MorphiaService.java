package org.Cherry.Modules.Mongo.Morphia;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Modules.Mongo.Middleware.MongoRepositoryService;
import org.Cherry.Modules.Security.Model.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.Morphia;

@Singleton
public class MorphiaService extends Morphia {
  public Datastore getDatastore() {
    if (null == _dataStore) {
      _dataStore = new DatastoreImpl(this, getMongoRepository().getMongo(), getMongoRepository().getDB().getName());
      _dataStore.ensureIndexes();
    }

    return _dataStore;
  }

  private MongoRepositoryService getMongoRepository() {
    assert null != _mongoRepository;
    return _mongoRepository;
  }

  @Inject
  @Singleton
  private MongoRepositoryService _mongoRepository;

  private Datastore _dataStore;

  public MorphiaService() {
    final Class<?>[] entityTypes = new Class<?>[] { User.class };
    map(entityTypes);
  }
}
