/*******************************************************************************
 * Copyright (c) 2013-2014 Cherry Platform
 *
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL
 * 1.0 (the "Licenses"). You can select the license that you prefer but you may
 * not use this file except in compliance with one of these Licenses.
 *
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 *
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0
 *
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1
 *
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1
 *
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 *
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 *
 *
 * Cherry Platform is a registered trademark of Sabadios
 *
 * Contributors:
 * Cristian Malinescu - initial design, API and implementation
 *******************************************************************************/
package org.Cherry.Modules.Hazelcast.Middleware;

import static com.hazelcast.core.Hazelcast.newHazelcastInstance;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Configuration.ConfigurationService;
import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Error.DeprecatedError;

import com.hazelcast.config.Config;
import com.hazelcast.core.ClientService;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.DistributedObjectListener;
import com.hazelcast.core.Endpoint;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.IAtomicReference;
import com.hazelcast.core.ICountDownLatch;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IList;
import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ISemaphore;
import com.hazelcast.core.ISet;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.IdGenerator;
import com.hazelcast.core.LifecycleService;
import com.hazelcast.core.MultiMap;
import com.hazelcast.core.PartitionService;
import com.hazelcast.logging.LoggingService;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionException;
import com.hazelcast.transaction.TransactionOptions;
import com.hazelcast.transaction.TransactionalTask;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
public class HazelcastService extends ServiceTemplate implements HazelcastInstance {
  private static final long serialVersionUID = 1L;

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getName()
   */
  @Override
  public String getName() {
    return getHazelcastInstance().getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getQueue(java.lang.String)
   */
  @Override
  public <E> IQueue<E> getQueue(final String name) {
    return getHazelcastInstance().getQueue(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getTopic(java.lang.String)
   */
  @Override
  public <E> ITopic<E> getTopic(final String name) {
    return getHazelcastInstance().getTopic(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getSet(java.lang.String)
   */
  @Override
  public <E> ISet<E> getSet(final String name) {
    return getHazelcastInstance().getSet(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getList(java.lang.String)
   */
  @Override
  public <E> IList<E> getList(final String name) {
    return getHazelcastInstance().getList(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getMap(java.lang.String)
   */
  @Override
  public <K, V> IMap<K, V> getMap(final String name) {
    return getHazelcastInstance().getMap(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getMultiMap(java.lang.String)
   */
  @Override
  public <K, V> MultiMap<K, V> getMultiMap(final String name) {
    return getHazelcastInstance().getMultiMap(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getLock(java.lang.String)
   */
  @Override
  public ILock getLock(final String key) {
    return getHazelcastInstance().getLock(key);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getLock(java.lang.Object)
   */
  @Override
  public ILock getLock(final Object key) {
    throw new DeprecatedError();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getCluster()
   */
  @Override
  public Cluster getCluster() {
    return getHazelcastInstance().getCluster();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getExecutorService(java.lang.String)
   */
  @Override
  public IExecutorService getExecutorService(final String name) {
    return getHazelcastInstance().getExecutorService(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#executeTransaction(com.hazelcast.transaction.TransactionalTask)
   */
  @Override
  public <T> T executeTransaction(final TransactionalTask<T> task) throws TransactionException {
    return getHazelcastInstance().executeTransaction(task);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#executeTransaction(com.hazelcast.transaction.TransactionOptions, com.hazelcast.transaction.TransactionalTask)
   */
  @Override
  public <T> T executeTransaction(final TransactionOptions options, final TransactionalTask<T> task) throws TransactionException {
    return getHazelcastInstance().executeTransaction(options, task);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#newTransactionContext()
   */
  @Override
  public TransactionContext newTransactionContext() {
    return getHazelcastInstance().newTransactionContext();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#newTransactionContext(com.hazelcast.transaction.TransactionOptions)
   */
  @Override
  public TransactionContext newTransactionContext(final TransactionOptions options) {
    return getHazelcastInstance().newTransactionContext(options);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getIdGenerator(java.lang.String)
   */
  @Override
  public IdGenerator getIdGenerator(final String name) {
    return getHazelcastInstance().getIdGenerator(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getAtomicLong(java.lang.String)
   */
  @Override
  public IAtomicLong getAtomicLong(final String name) {
    return getHazelcastInstance().getAtomicLong(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getCountDownLatch(java.lang.String)
   */
  @Override
  public ICountDownLatch getCountDownLatch(final String name) {
    return getHazelcastInstance().getCountDownLatch(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getSemaphore(java.lang.String)
   */
  @Override
  public ISemaphore getSemaphore(final String name) {
    return getHazelcastInstance().getSemaphore(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getDistributedObjects()
   */
  @Override
  public Collection<DistributedObject> getDistributedObjects() {
    return getHazelcastInstance().getDistributedObjects();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#addDistributedObjectListener(com.hazelcast.core.DistributedObjectListener)
   */
  @Override
  public String addDistributedObjectListener(final DistributedObjectListener distributedObjectListener) {
    return getHazelcastInstance().addDistributedObjectListener(distributedObjectListener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#removeDistributedObjectListener(java.lang.String)
   */
  @Override
  public boolean removeDistributedObjectListener(final String registrationId) {
    return getHazelcastInstance().removeDistributedObjectListener(registrationId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getConfig()
   */
  @Override
  public Config getConfig() {
    return getHazelcastInstance().getConfig();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getPartitionService()
   */
  @Override
  public PartitionService getPartitionService() {
    return getHazelcastInstance().getPartitionService();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getClientService()
   */
  @Override
  public ClientService getClientService() {
    return getHazelcastInstance().getClientService();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getLoggingService()
   */
  @Override
  public LoggingService getLoggingService() {
    return getHazelcastInstance().getLoggingService();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getLifecycleService()
   */
  @Override
  public LifecycleService getLifecycleService() {
    return getHazelcastInstance().getLifecycleService();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getDistributedObject(java.lang.String, java.lang.Object)
   */
  @Override
  public <T extends DistributedObject> T getDistributedObject(final String serviceName, final Object id) {
    throw new DeprecatedError();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getDistributedObject(java.lang.String, java.lang.String)
   */
  @Override
  public <T extends DistributedObject> T getDistributedObject(final String serviceName, final String name) {
    return getHazelcastInstance().getDistributedObject(serviceName, name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#getUserContext()
   */
  @Override
  public ConcurrentMap<String, Object> getUserContext() {
    return getHazelcastInstance().getUserContext();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.hazelcast.core.HazelcastInstance#shutdown()
   */
  @Override
  public void shutdown() {
    getHazelcastInstance().shutdown();
  }

  private Config getConfiguration() {
    return getConfigurationService().getConfig();
  }

  private HazelcastInstance getHazelcastInstance() {
    if (null == _hazelcastInstance)
      _hazelcastInstance = newHazelcastInstance(getConfiguration());

    return _hazelcastInstance;
  }

  private ConfigurationService getConfigurationService() {
    assert null != _configurationService;
    return _configurationService;
  }

  public void setup() {
    assert null != getHazelcastInstance();
  }

  @Inject
  @Singleton
  private ConfigurationService _configurationService;

  private HazelcastInstance _hazelcastInstance;

  @PostConstruct
  void postConstruct() {
  }

  @PreDestroy
  void preDestroy() {

  }

  /**
   *
   */
  public HazelcastService() {
  }

  @Override
  public JobTracker getJobTracker(final String name) {
    return getHazelcastInstance().getJobTracker(name);
  }

  @Override
  public Endpoint getLocalEndpoint() {
    return getHazelcastInstance().getLocalEndpoint();
  }

  @Override
  public <E> IAtomicReference<E> getAtomicReference(final String name) {
    return getHazelcastInstance().getAtomicReference(name);
  }
}
