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

import static org.Cherry.Modules.Freemarker.FreemarkerConstants.TEMPLATE_EXCEPTION_HANDLER;
import static org.Cherry.Modules.Freemarker.FreemarkerConstants.TEMPLATE_EXCEPTION_HANDLER_DEBUG;
import static org.Cherry.Modules.Freemarker.FreemarkerConstants.TEMPLATE_EXCEPTION_HANDLER_HTML_DEBUG;
import static org.Cherry.Modules.Freemarker.FreemarkerConstants.TEMPLATE_EXCEPTION_HANDLER_IGNORE;
import static org.Cherry.Modules.Freemarker.FreemarkerConstants.TEMPLATE_EXCEPTION_HANDLER_RETHROW;
import static org.Cherry.Modules.Freemarker.FreemarkerConstants.WRAPPER_BEANS;
import static org.Cherry.Modules.Freemarker.FreemarkerConstants.WRAPPER_SIMPLE;
import static org.Cherry.Modules.Freemarker.Middleware.FreeMarkerParametersService.CLASS_LOADER_PROTOCOL;
import static org.Cherry.Modules.Freemarker.Middleware.FreeMarkerParametersService.FILE_LOADER_PROTOCOL;
import static org.Cherry.Utils.Utils.failIfEmpty;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.Freemarker.DictionaryModel;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModel;
import freemarker.template.utility.StringUtil;

/**
 * @author Cristian.Malinescu
 * 
 */
@Singleton
public final class FreeMarkerService extends ServiceTemplate {
  private ObjectWrapper getObjectWrapper() {
    if (null == _objectWrapper) {
      _objectWrapper = getConfiguration().getObjectWrapper();

      if (null == _objectWrapper) {
        final String wrapperId = getObjectWrapperId();

        if (null != wrapperId)
          switch (wrapperId) {
            case WRAPPER_BEANS:
              _objectWrapper = ObjectWrapper.BEANS_WRAPPER;
              break;

            case WRAPPER_SIMPLE:
              _objectWrapper = ObjectWrapper.SIMPLE_WRAPPER;
              break;

            default:
              _objectWrapper = ObjectWrapper.DEFAULT_WRAPPER;
          }
        else _objectWrapper = ObjectWrapper.DEFAULT_WRAPPER;

        assert null != _objectWrapper;

        debug("Using object wrapper [{}]", _objectWrapper);

        getConfiguration().setObjectWrapper(_objectWrapper);
      }
    }

    return _objectWrapper;
  }

  private Template getTemplate(final String name) throws IOException {
    return getConfiguration().getTemplate(name);
  }

  private Template getTemplate(final String name, final Locale locale) throws IOException {
    return getConfiguration().getTemplate(name, locale);
  }

  public void process(final String name, final TemplateModel model, final Locale locale, final Writer writer) throws IOException {
    debug("Requested template : [{}]", StringUtil.jQuoteNoXSS(name));

    final Template template = getTemplate(name, locale);

    process(template, model, writer);
  }

  public void process(final String name, final TemplateModel model, final Writer writer) throws IOException {
    debug("Requested template : [{}]", StringUtil.jQuoteNoXSS(name));

    final Template template = getTemplate(name);

    process(template, model, writer);
  }

  private void process(final Template template, final TemplateModel model, final Writer writer) throws IOException {
    try {
      template.process(model, writer);
    } catch (final TemplateException te) {
      if (getConfiguration().getTemplateExceptionHandler().getClass().getName().indexOf("Debug") != -1)
        error(te, "Error executing FreeMarker template");
      else throw new IllegalStateException("Error executing FreeMarker template", te);
    }
  }

  public DictionaryModel createModel() {
    try {
      return (DictionaryModel) getDictionaryModel().clone();
    } catch (final CloneNotSupportedException e) {
      error(e, "");
      throw new IllegalStateException(e);
    }
  }

  private DictionaryModel getDictionaryModel() {
    if (null == _modelTemplate)
      _modelTemplate = new DictionaryModel(getObjectWrapper());

    return _modelTemplate;
  }

  private TemplateLoader getTemplateLoader() throws IOException {
    final String templatePath = getTemplatePath();

    info("Templates directory path is [{}]", templatePath);

    // beginIndex(7) - reuse the last slash
    if (getTemplatePath().startsWith(CLASS_LOADER_PROTOCOL))
      return new ClassTemplateLoader(getClass(), templatePath.substring(7));

    if (getTemplatePath().startsWith(FILE_LOADER_PROTOCOL))
      return new FileTemplateLoader(new File(templatePath.substring(7)));

    throw new IllegalStateException(new IllegalArgumentException("Unsupported templatePath and loader protocol [" + templatePath + "]"));
  }

  protected Configuration getConfiguration() {
    if (null == _configuration)
      _configuration = new Configuration();

    return _configuration;
  }

  private String getTemplatePath() {
    final String templatePath = getParameters().getTemplatePath();

    failIfEmpty(templatePath);

    return templatePath;
  }

  private String getObjectWrapperId() {
    return getParameters().getObjectWrapper();
  }

  private Boolean isDebug() {
    return getParameters().isDebug();
  }

  private String getDefaultEncoding() {
    return getParameters().getDefaultEncoding();
  }

  private FreeMarkerParametersService getParameters() {
    assert null != _freemarkerParamsService;
    return _freemarkerParamsService;
  }

  private Integer getTemplateUpdateDelay() {
    return getParameters().getTemplateUpdateDelay();
  }

  private String getTemplateExceptionHandler() {
    return getParameters().getTemplateExceptionHandler();
  }

  public void setup() {
    getConfiguration().setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);

    getConfiguration().setObjectWrapper(getObjectWrapper());

    try {
      getConfiguration().setTemplateLoader(getTemplateLoader());
    } catch (final IOException e) {
      error(e, "");
      throw new IllegalStateException(e);
    }
    getConfiguration().setDefaultEncoding(getDefaultEncoding());
    getConfiguration().setTemplateUpdateDelay(getTemplateUpdateDelay());

    switch (getTemplateExceptionHandler()) {
      case TEMPLATE_EXCEPTION_HANDLER_RETHROW:
        getConfiguration().setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        break;
      case TEMPLATE_EXCEPTION_HANDLER_DEBUG:
        getConfiguration().setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        break;
      case TEMPLATE_EXCEPTION_HANDLER_HTML_DEBUG:
        getConfiguration().setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        break;
      case TEMPLATE_EXCEPTION_HANDLER_IGNORE:
        getConfiguration().setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        break;
      default:
        throw new IllegalStateException("Invalid value for servlet init-param " + TEMPLATE_EXCEPTION_HANDLER + ": "
            + getTemplateExceptionHandler());
    }
  }

  @Inject
  @Singleton
  private FreeMarkerParametersService _freemarkerParamsService;

  private Configuration _configuration;
  private DictionaryModel _modelTemplate;
  private ObjectWrapper _objectWrapper;

  @PostConstruct
  protected void postConstruct() {
  }

  @PreDestroy
  protected void preDestroy() {
  }

  /**
   * 
   */
  public FreeMarkerService() {
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
}
