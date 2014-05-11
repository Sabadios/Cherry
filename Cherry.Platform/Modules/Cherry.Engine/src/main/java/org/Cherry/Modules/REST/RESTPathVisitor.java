package org.Cherry.Modules.REST;

import static org.Cherry.Modules.Web.WebConstants.ROOT_URI;
import static org.Cherry.Utils.Utils.failIfEmpty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;

import javax.ws.rs.Path;

import org.Cherry.Core.ServiceTemplate;
import org.Cherry.Modules.JSON.Agents.JSONAgentDefinition;
import org.Cherry.Modules.JSON.Agents.JSONAgentsDictionary;
import org.glassfish.jersey.server.model.AnnotatedMethod;
import org.glassfish.jersey.server.model.MethodList;

public final class RESTPathVisitor extends ServiceTemplate {
  public boolean onAccept(final String name) {
    failIfEmpty(name);
    info("Scanning name [{}]", name);

    if (!supported(name))
      return false;

    Class<?> jsonAgentType = null;
    final String typeName = getTypeName(name);

    try {
      jsonAgentType = Class.forName(typeName);
    } catch (final ClassNotFoundException e) {
      throw new IllegalArgumentException(e);
    }

    final Annotation annotation = jsonAgentType.getAnnotation(Path.class);

    if (annotation instanceof Path) {
      final Path path = (Path) annotation;

      final String restRootPath = path.value();

      final JSONAgentDefinition jsonAgentDef = new JSONAgentDefinition(restRootPath, jsonAgentType);

      return process(jsonAgentDef);
    }

    return false;
  }

  private String getTypeName(final String name) {
    return new StringBuilder(getNamespace()).append(".").append(process(name)).toString();
  }

  private boolean supported(final String name) {
    return !name.contains(ignore);
  }

  private String process(final String name) {
    String hdlr;

    if (name.contains(ROOT_URI))
      hdlr = name.substring(name.lastIndexOf(ROOT_URI) + 1, name.length());
    else hdlr = name;

    if (hdlr.endsWith(suffix))
      return hdlr.substring(0, hdlr.indexOf(suffix));

    return name;
  }

  private Boolean process(final JSONAgentDefinition jsonAgentDef) {
    getJSONAgentsDictionary().add(jsonAgentDef);

    final Class<?> type = jsonAgentDef.getJSONAgentType();
    final MethodList typeMethodList = new MethodList(type, true);
    final MethodList pathMethods = typeMethodList.withAnnotation(Path.class);
    final Iterator<AnnotatedMethod> pathsIter = pathMethods.iterator();

    if (pathsIter.hasNext()) {
      AnnotatedMethod pathHdlr;
      Method method;
      Path path;
      String pathValue;

      while (pathsIter.hasNext()) {
        pathHdlr = pathsIter.next();

        method = pathHdlr.getMethod();
        path = pathHdlr.getAnnotation(Path.class);

        assert null != path;

        pathValue = path.value();

        new RESTMethodDefinition(pathValue, method, jsonAgentDef);
      }

      return true;
    }

    return false;
  }

  private String getNamespace() {
    return _namespace;
  }

  private JSONAgentsDictionary getJSONAgentsDictionary() {
    return _jsonAgentsDictionary;
  }

  private final String _namespace;
  private final JSONAgentsDictionary _jsonAgentsDictionary;

  public RESTPathVisitor(final JSONAgentsDictionary jsonAgentsDictionary, final String namespace) {
    assert null != jsonAgentsDictionary;
    _jsonAgentsDictionary = jsonAgentsDictionary;

    failIfEmpty(namespace);
    _namespace = namespace;
  }

  static final String ignore = "$", suffix = ".class";

  private static final long serialVersionUID = 1L;
}