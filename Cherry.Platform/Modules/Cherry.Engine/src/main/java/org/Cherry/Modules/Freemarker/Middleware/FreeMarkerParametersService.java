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
package org.Cherry.Modules.Freemarker.Middleware;

import static org.Cherry.Modules.Freemarker.FreemarkerConstants.TEMPLATE_EXCEPTION_HANDLER_DEBUG;
import static org.Cherry.Utils.Utils.failIfEmpty;
import static org.Cherry.Utils.Utils.failIfNegative;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Configuration.ConfigurationService;
import org.Cherry.Core.ServiceTemplate;

@Singleton
@NotThreadSafe
public class FreeMarkerParametersService extends ServiceTemplate {
  public String getTemplatePath() {
    return _templatePath + _config.getDocRoot();
  }

  public void setTemplatePath(final String templatePath) {
    failIfEmpty(templatePath);
    _templatePath = templatePath;
  }

  public String getObjectWrapper() {
    return _objectWrapper;
  }

  public void setObjectWrapper(final String objectWrapper) {
    failIfEmpty(objectWrapper);
    _objectWrapper = objectWrapper;
  }

  public Boolean isDebug() {
    return _debug;
  }

  public void setDebug(final Boolean debug) {
    assert null != debug;
    _debug = debug;
  }

  public String getDefaultEncoding() {
    return _defaultEncoding;
  }

  public void setDefaultEncoding(final String defaultEncoding) {
    failIfEmpty(defaultEncoding);
    _defaultEncoding = defaultEncoding;
  }

  public Integer getTemplateUpdateDelay() {
    return _templateUpdateDelay;
  }

  public void setTemplateUpdateDelay(final Integer templateUpdateDelay) {
    failIfNegative(templateUpdateDelay);
    _templateUpdateDelay = templateUpdateDelay;
  }

  public String getTemplateExceptionHandler() {
    return _templateExceptionHandler;
  }

  public void setTemplateExceptionHandler(final String templateExceptionHandler) {
    failIfEmpty(templateExceptionHandler);
    _templateExceptionHandler = templateExceptionHandler;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    final FreeMarkerParametersService freeMarkerParametersService = (FreeMarkerParametersService) super.clone();
    return freeMarkerParametersService;
  }

  @Inject
  @Singleton
  private ConfigurationService _config;

  private String _templatePath = FILE_LOADER_PROTOCOL,
      _objectWrapper;

  private String _defaultEncoding = "UTF-8";

  private String _templateExceptionHandler = TEMPLATE_EXCEPTION_HANDLER_DEBUG;
  private Integer _templateUpdateDelay = 1;
  private Boolean _debug = Boolean.FALSE;

  @PostConstruct
  protected void postConstruct() {
  }

  @PreDestroy
  protected void preDestroy() {
  }

  public FreeMarkerParametersService() {
  }

  static final String CLASS_LOADER_PROTOCOL = "class://";
  static public final String FILE_LOADER_PROTOCOL = "file://";

  private static final long serialVersionUID = 1L;
}
