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
package org.Cherry.Modules.Execution.Middleware;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Configuration.ConfigurationService;
import org.Cherry.Core.ServiceTemplate;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
public class DefaultExecutorService extends ServiceTemplate implements ExecutorService {
  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
   */
  @Override
  public void execute(final Runnable command) {
    getExecutorService().execute(command);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ExecutorService#shutdown()
   */
  @Override
  public void shutdown() {
    warn("{}", "Caller tried to invoke shutdown, will be ignored.");
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ExecutorService#shutdownNow()
   */
  @Override
  public List<Runnable> shutdownNow() {
    warn("{}", "Caller tried to invoke shutdown, will be ignored.");
    return Collections.emptyList();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ExecutorService#isShutdown()
   */
  @Override
  public boolean isShutdown() {
    return getExecutorService().isShutdown();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ExecutorService#isTerminated()
   */
  @Override
  public boolean isTerminated() {
    return getExecutorService().isTerminated();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ExecutorService#awaitTermination(long, java.util.concurrent.TimeUnit)
   */
  @Override
  public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
    warn("{}", "Caller tried to invoke shutdown, will be ignored.");
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ExecutorService#submit(java.util.concurrent.Callable)
   */
  @Override
  public <T> Future<T> submit(final Callable<T> task) {
    return getExecutorService().submit(task);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable, java.lang.Object)
   */
  @Override
  public <T> Future<T> submit(final Runnable task, final T result) {
    return getExecutorService().submit(task, result);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable)
   */
  @Override
  public Future<?> submit(final Runnable task) {
    return getExecutorService().submit(task);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection)
   */
  @Override
  public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
    return getExecutorService().invokeAll(tasks);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection, long, java.util.concurrent.TimeUnit)
   */
  @Override
  public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
    return getExecutorService().invokeAll(tasks, timeout, unit);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection)
   */
  @Override
  public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
    return getExecutorService().invokeAny(tasks);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection, long, java.util.concurrent.TimeUnit)
   */
  @Override
  public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException,
      ExecutionException,
      TimeoutException {
    return getExecutorService().invokeAny(tasks, timeout, unit);
  }

  private ExecutorService getExecutorService() {
    if (null == _executorService)
      _executorService = new ThreadPoolExecutor(getCorePoolSize(), getMaximumPoolSize(), getKeepAliveTime(), getUnit(), getWorkQueue());

    return _executorService;
  }

  private Integer getCorePoolSize() {
    return getConfigurationService().getCorePoolSize();
  }

  private Integer getMaximumPoolSize() {
    return getConfigurationService().getMaximumPoolSize();
  }

  private Long getKeepAliveTime() {
    return getConfigurationService().getKeepAliveTime();
  }

  private BlockingQueue<Runnable> getWorkQueue() {
    return new SynchronousQueue<Runnable>();
  }

  private TimeUnit getUnit() {
    return TimeUnit.SECONDS;
  }

  private ConfigurationService getConfigurationService() {
    assert null != _configurationService;
    return _configurationService;
  }

  @Inject
  @Singleton
  private ConfigurationService _configurationService;
  private ExecutorService _executorService;

  @PostConstruct
  protected void postConstruct() {
  }

  @PreDestroy
  protected void preDestroy() {
    if (!getExecutorService().isShutdown())
      getExecutorService().shutdown();
  }

  public DefaultExecutorService() {
  }

  private static final long serialVersionUID = 1L;
}
