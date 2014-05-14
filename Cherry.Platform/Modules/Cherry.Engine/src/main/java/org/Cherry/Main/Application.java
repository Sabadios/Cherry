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
package org.Cherry.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.Cherry.Cherry;
import org.Cherry.Configuration.Configuration;
import org.Cherry.Context.ApplicationContextService;
import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.JSON.Agents.JSONAgentDefinition;
import org.Cherry.Modules.JSON.Agents.JSONAgentsDictionary;
import org.Cherry.Modules.Jackson.Middleware.ObjectMapperService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jboss.weld.environment.se.events.ContainerInitialized;

public abstract class Application extends ServiceTemplate {
  void containerInitialized(@Observes final ContainerInitialized event) {
    info("CDI container initialialization completed - notification [{}] received.", event);

    process();
    scan();
    getApplicationContextService().start();
  }

  void process() {
    final Option httpPortOption = OptionBuilder.withArgName(CmdLineArg.httpPort.name()).hasArg().withDescription("port to listen on [8080]").create(CmdLineArg.httpPort.name()), configFileOption = OptionBuilder
        .withArgName(CmdLineArg.configFile.name()).hasArg().withDescription("configuration file to use").create(CmdLineArg.configFile.name()), controllerNamespaces = OptionBuilder
        .withArgName(CmdLineArg.controllerNamespaces.name()).hasArg().withDescription("Controller namespace declarations").create(CmdLineArg.controllerNamespaces.name());

    final Options options = new Options();

    options.addOption(configFileOption);
    options.addOption(httpPortOption);
    options.addOption(controllerNamespaces);

    final CommandLineParser parser = new GnuParser();
    CommandLine commandLine = null;

    try {
      commandLine = parser.parse(options, INSTANCE_ARGUMENTS);
    } catch (final ParseException e) {
      error(e, "");

      throw new IllegalArgumentException(e);
    }

    final Option[] processedOptions = commandLine.getOptions();

    if (null != processedOptions && 0 < processedOptions.length) {

      final Map<CmdLineArg, Object> optionMap = new HashMap<CmdLineArg, Object>(processedOptions.length);

      CmdLineArg cmdLineArg;
      Boolean configFileDeclared = false;

      for (final Option option : processedOptions) {
        cmdLineArg = CmdLineArg.valueOf(option.getArgName());

        switch (cmdLineArg) {
          case configFile:
            if (configFileDeclared)
              throw new IllegalArgumentException("Duplicated declaration for argument [" + cmdLineArg + "]");
            optionMap.put(cmdLineArg, option.getValue());
            configFileDeclared = true;
            break;

          case httpPort:
            if (configFileDeclared) {
              warn("{}", "Configuration file foumd, all other options - including [" + cmdLineArg + "] will be ignored!");
              continue;
            }
            if (optionMap.containsKey(cmdLineArg))
              throw new IllegalArgumentException("Duplicated declaration for argument [" + cmdLineArg + "]");
            optionMap.put(cmdLineArg, option.getValue());
            break;

          case controllerNamespaces:
            if (configFileDeclared) {
              warn("{}", "Configuration file foumd, all other options - including [" + cmdLineArg + "] will be ignored!");
              continue;
            }
            if (optionMap.containsKey(cmdLineArg))
              throw new IllegalArgumentException("Duplicated declaration for argument [" + cmdLineArg + "]");
            optionMap.put(cmdLineArg, option.getValue());
            break;

          default:
            warn("Will ignore [{}]=[{}]", cmdLineArg, option.getValue());
        }
      }

      final File cfgFile = new File((String) optionMap.get(CmdLineArg.configFile));

      if (!cfgFile.exists() || cfgFile.isDirectory())
        throw new IllegalArgumentException("Missing configuration file [" + cfgFile.getPath() + "] or it is a directory!");

      FileInputStream fis = null;
      Configuration configuration;

      try {
        fis = new FileInputStream(cfgFile);
        configuration = getObjectMapperService().readValue(fis, Configuration.class);
      } catch (final IOException e) {
        error(e, "");
        throw new IllegalArgumentException(e);
      } finally {
        try {
          fis.close();
        } catch (final IOException e) {
          error(e, "");
        }
      }

      debug("{}", configuration);

      getApplicationRepositoryService().put(ApplicationKey.Configuration, new ApplicationEntry<Configuration>(configuration));
    }
  }

  public Configuration getConfiguration() {
    final ApplicationEntry<?> appEntry = getApplicationRepositoryService().get(ApplicationKey.Configuration);

    assert null != appEntry : "Missing required application component - 'Configuration'";

    return (Configuration) appEntry.getValue();
  }

  private Set<String> getJSONAgentsNamespaces() {
    if (null == _jsonAgentsNamespaces) {
      _jsonAgentsNamespaces = new HashSet<String>(_systemJSONAgents);
      _jsonAgentsNamespaces.addAll(getConfiguration().getWeb().getControllerNamespaces());
    }

    return _jsonAgentsNamespaces;
  }

  private void scan() {
    getJSONAgentsDictionary().setNamespaces(getJSONAgentsNamespaces()).scan();
  }

  protected void bind() {
    final Iterator<JSONAgentDefinition> jsonAgentsDefs = getJSONAgentsDictionary().getJSONAgents().values().iterator();
    JSONAgentDefinition jsonAgentDef;
    Object jsonAgent = null;

    while (jsonAgentsDefs.hasNext()) {
      jsonAgentDef = jsonAgentsDefs.next();

      jsonAgent = jsonAgentDef.getJSONAgent();

      if (null == jsonAgent) {
        jsonAgent = getBean(jsonAgentDef.getJSONAgentType());
        jsonAgentDef.setJSONAgent(jsonAgent);
      }
    }
  }

  public <T> T getBean(final Class<T> type) {
    return Cherry.instance().getBean(type);
  }

  private JSONAgentsDictionary getJSONAgentsDictionary() {
    ApplicationEntry<JSONAgentsDictionary> appEntry = (ApplicationEntry<JSONAgentsDictionary>) getApplicationRepositoryService().get(ApplicationKey.JSON_AGENTS_DICTIONARY);

    if (null == appEntry) {
      appEntry = new ApplicationEntry<JSONAgentsDictionary>(new JSONAgentsDictionary());
      getApplicationRepositoryService().put(ApplicationKey.JSON_AGENTS_DICTIONARY, appEntry);
    }

    return appEntry.getValue();
  }

  private ObjectMapperService getObjectMapperService() {
    assert null != _objectMapper;
    return _objectMapper;
  }

  private ApplicationRepositoryService getApplicationRepositoryService() {
    assert null != _applicationRepositoryService;
    return _applicationRepositoryService;
  }

  private ApplicationContextService getApplicationContextService() {
    assert null != _applicationContextService;
    return _applicationContextService;
  }

  @Inject
  @Singleton
  private ApplicationContextService _applicationContextService;

  @Inject
  @Singleton
  private ApplicationRepositoryService _applicationRepositoryService;

  @Inject
  @Singleton
  private ObjectMapperService _objectMapper;

  private final Set<String> _systemJSONAgents = new HashSet<String>();
  private Set<String> _jsonAgentsNamespaces;

  @PostConstruct
  protected void postConstruct() {
  }

  @PreDestroy
  protected void preDestroy() {
  }

  protected Application() {
    _systemJSONAgents.add("org.Cherry.Modules.Web.Agents.Standard");
  }

  static {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandlerService());
    Runtime.getRuntime().addShutdownHook(ShutdownDaemonService.getInstance());
  }

  static enum CmdLineArg {
    configFile, httpPort, controllerNamespaces
  }

  static public String[] INSTANCE_ARGUMENTS;

  /**
   *
   */
  static private final long serialVersionUID = 1L;
}
