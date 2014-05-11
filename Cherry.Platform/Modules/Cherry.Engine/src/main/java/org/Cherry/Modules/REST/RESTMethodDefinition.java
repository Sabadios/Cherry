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
package org.Cherry.Modules.REST;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.MediaType.TEXT_XML;
import static org.Cherry.Modules.JSON.Agents.JsonRestUtils.JSONConsumer;
import static org.Cherry.Modules.JSON.Agents.JsonRestUtils.JSONProducer;
import static org.Cherry.Utils.Utils.failIfEmpty;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.Cherry.Modules.JSON.Agents.JSONAgentDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RESTMethodDefinition implements Serializable {
  public Object invoke(final Object... args) {
    assert null != getJSONAgentDefinition();
    assert null != getJSONAgentDefinition().getJSONAgent();

    try {
      return getRestMethod().invoke(getJSONAgentDefinition().getJSONAgent(), args);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
  }

  public String getRestPath() {
    return _restPath;
  }

  public void setRestPath(final String restPath) {
    failIfEmpty(restPath);
    _restPath = restPath;
  }

  public Method getRestMethod() {
    return _restMethod;
  }

  public Produces getProduces() {
    return _produces;
  }

  public void setProduces(final Produces produces) {
    if (null == produces)
      _produces = JSONProducer;
    else _produces = produces;
  }

  public Consumes getConsumes() {
    return _consumes;
  }

  public void setConsumes(final Consumes consumes) {
    if (null == consumes)
      _consumes = JSONConsumer;
    else _consumes = consumes;
  }

  public Class<?>[] getParameterTypes() {
    return getRestMethod().getParameterTypes();
  }

  public Class<?> getReturnType() {
    return getRestMethod().getReturnType();
  }

  public void setRestMethod(final Method restMethod) {
    assert null != restMethod;

    _restMethod = restMethod;

    setProduces(_restMethod.getAnnotation(Produces.class));
    setConsumes(_restMethod.getAnnotation(Consumes.class));

    final WeakReference<Class<?>[]> prmtrTypes = new WeakReference<Class<?>[]>(_restMethod.getParameterTypes());
    final WeakReference<Type[]> genPrmtrTypes = new WeakReference<Type[]>(_restMethod.getGenericParameterTypes());
    WeakReference<Type> type;
    WeakReference<ParameterizedType> paramType;
    if (null != prmtrTypes && 1 == prmtrTypes.get().length) {
      final WeakReference<Annotation[][]> prmtrAnnttnMtrx = new WeakReference<Annotation[][]>(_restMethod.getParameterAnnotations());

      if (null != prmtrAnnttnMtrx && 1 == prmtrAnnttnMtrx.get().length) {
        final WeakReference<Annotation[]> prmtrAnnttns = new WeakReference<Annotation[]>(prmtrAnnttnMtrx.get()[0]);

        if (1 == prmtrAnnttns.get().length)
          if (BeanParam.class.equals(prmtrAnnttns.get()[0].annotationType())) {
            if (null != genPrmtrTypes && 1 == genPrmtrTypes.get().length) {
              type = new WeakReference<Type>(genPrmtrTypes.get()[0]);
              if (type.get() instanceof ParameterizedType) {
                paramType = new WeakReference<ParameterizedType>((ParameterizedType) type.get());
                _beanParameterType = paramType.get().getClass();
              } else if (type.get() instanceof Class<?>)
                _beanParameterType = (Class<?>) genPrmtrTypes.get()[0];
            } else _beanParameterType = prmtrTypes.get()[0];

            log.info("Found @[{}] annotated parameter of type [{}] for [{}]", BeanParam.class, _beanParameterType, _restMethod);
          } else
          throw new IllegalArgumentException("Unsupported method signature - the parameter is missing expected annotation '@BeanParam'!");
      }
    } else if (1 < prmtrTypes.get().length)
      throw new IllegalArgumentException("Unsupported method signature - more than one parameter!");
  }

  public Class<?> getBeanParameterType() {
    return _beanParameterType;
  }

  public Boolean hasBeanParameter() {
    return null != getBeanParameterType();
  }

  public String getMediaType() {
    final String mediaType = _produces.value()[0];

    failIfEmpty(mediaType);

    switch (mediaType) {
      case APPLICATION_JSON:
      case APPLICATION_XML:
      case TEXT_HTML:
      case TEXT_PLAIN:
      case TEXT_XML:
        break;
      default:
        throw new IllegalArgumentException("Not supported yet content [" + mediaType + "]");
    }

    return mediaType;
  }

  public Boolean isJSONMediaType() {
    return APPLICATION_JSON.equals(getMediaType());
  }

  public Boolean isHTMLMediaType() {
    return TEXT_HTML.equals(getMediaType());
  }

  public JSONAgentDefinition getJSONAgentDefinition() {
    return _jsonAgentDef;
  }

  public void setJSONAgentDefinition(final JSONAgentDefinition jsonAgentDefinition) {
    assert null != jsonAgentDefinition;
    _jsonAgentDef = jsonAgentDefinition;
    getJSONAgentDefinition().put(getRestPath(), this);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(super.toString());

    sb.append(" # REST Method def [").append(getJSONAgentDefinition().getRESTRootPath()).append(getRestPath()).append("]");
    sb.append(" consumes [").append(getConsumes()).append("], produces [").append(getProduces()).append("]");
    sb.append(" instance [").append(getRestMethod()).append("].");

    return sb.toString();
  }

  @Override
  public int hashCode() {
    return getRestPath().hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other instanceof RESTMethodDefinition) {
      final RESTMethodDefinition that = (RESTMethodDefinition) other;

      if (getJSONAgentDefinition().equals(that.getJSONAgentDefinition()))
        return getRestPath().equals(that.getRestPath());
    }

    return false;
  }

  private String _restPath;
  private Method _restMethod;
  private Produces _produces;
  private Consumes _consumes;
  private Class<?> _beanParameterType;
  private JSONAgentDefinition _jsonAgentDef;

  public RESTMethodDefinition(final String restPath, final Method restMethod, final JSONAgentDefinition jsonAgentDefinition) {
    this(restPath, restMethod);
    setJSONAgentDefinition(jsonAgentDefinition);
  }

  public RESTMethodDefinition(final String restPath, final Method restMethod) {
    setRestPath(restPath);
    setRestMethod(restMethod);
  }

  public RESTMethodDefinition() {
  }

  static private Logger log = LoggerFactory.getLogger(RESTMethodDefinition.class);

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
}
