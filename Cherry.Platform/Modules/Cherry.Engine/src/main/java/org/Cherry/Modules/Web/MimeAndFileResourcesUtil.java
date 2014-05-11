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
package org.Cherry.Modules.Web;

import static org.Cherry.Modules.Web.Engine.WebResources.CSS;
import static org.Cherry.Modules.Web.Engine.WebResources.DOT;
import static org.Cherry.Modules.Web.Engine.WebResources.GIF;
import static org.Cherry.Modules.Web.Engine.WebResources.HTML;
import static org.Cherry.Modules.Web.Engine.WebResources.ICO;
import static org.Cherry.Modules.Web.Engine.WebResources.JPEG;
import static org.Cherry.Modules.Web.Engine.WebResources.JPG;
import static org.Cherry.Modules.Web.Engine.WebResources.JS;
import static org.Cherry.Modules.Web.Engine.WebResources.JSON;
import static org.Cherry.Modules.Web.Engine.WebResources.PNG;
import static org.Cherry.Utils.Utils.isNotEmpty;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;

import org.apache.http.HttpRequest;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.RequestLine;
import org.apache.http.entity.ContentType;

/**
 * @author Cristian.Malinescu
 * 
 */
abstract public class MimeAndFileResourcesUtil {
  static public String supported(final HttpRequest request) throws MethodNotSupportedException {
    assert null != request;

    final RequestLine requestLine = request.getRequestLine();
    final String method = requestLine.getMethod().toUpperCase(Locale.ENGLISH);

    switch (method) {
      case "GET":
      case "HEAD":
      case "POST":
        return requestLine.getUri();
    }

    throw new MethodNotSupportedException(method + " method not supported");
  }

  static public Boolean isFileSig(final String uri) {
    if (isMimeSig(uri)) {
      final String fileSig = uri.substring(uri.lastIndexOf(DOT), uri.length());

      switch (fileSig) {
        case JS:
        case CSS:
        case HTML:
        case JPG:
        case JPEG:
        case ICO:
        case JSON:
        case PNG:
        case GIF:
          return true;
      }
    }

    return false;
  }

  static public Boolean isJsonSig(final String uri) {
    return JSON.equalsIgnoreCase(getMimeSig(uri));
  }

  static public Boolean isJavascriptSig(final String uri) {
    return JS.equalsIgnoreCase(getMimeSig(uri));
  }

  static public Boolean isCssSig(final String uri) {
    return CSS.equalsIgnoreCase(getMimeSig(uri));
  }

  static public Boolean isIcoSig(final String uri) {
    return ICO.equalsIgnoreCase(getMimeSig(uri));
  }

  static public Boolean isGifSig(final String uri) {
    return GIF.equalsIgnoreCase(getMimeSig(uri));
  }

  static public Boolean isPngSig(final String uri) {
    return PNG.equalsIgnoreCase(getMimeSig(uri));
  }

  static public Boolean isJpegSig(final String uri) {
    return JPEG.equalsIgnoreCase(getMimeSig(uri)) || JPG.equalsIgnoreCase(getMimeSig(uri));
  }

  static public ContentType getFor(final String uri) {
    if (isCssSig(uri))
      return APPLICATION_CSS_CONTENT_TYPE;

    if (isJavascriptSig(uri))
      return APPLICATION_JAVASCRIPT_CONTENT_TYPE;

    if (isIcoSig(uri))
      return IMAGE_ICO_CONTENT_TYPE;

    if (isPngSig(uri))
      return IMAGE_PNG_CONTENT_TYPE;

    if (isGifSig(uri))
      return IMAGE_GIF_CONTENT_TYPE;

    if (isJpegSig(uri))
      return IMAGE_JPEG_CONTENT_TYPE;

    return APPLICATION_HTML_CONTENT_TYPE;
  }

  static public Boolean isMimeSig(final String uri) {
    return isNotEmpty(uri) && uri.contains(DOT);
  }

  static public String getMimeSig(final String uri) {
    if (isMimeSig(uri))
      return uri.substring(uri.lastIndexOf(DOT), uri.length());
    return null;
  }

  static public File getFileResource(final String docRoot, final String uri) throws UnsupportedEncodingException {
    if (isNotEmpty(docRoot) && isNotEmpty(uri)) {
      final File file = new File(docRoot, URLDecoder.decode(uri, "UTF-8"));

      if (!file.exists() || file.isDirectory())
        throw new IllegalArgumentException("File [" + file.getPath() + "] is missing or it is a directory!");

      return file;
    }

    return null;
  }

  static public final Object[] EMPTY_VAR_ARGS = new Object[] {};
  static public final String EMPTY_JSON_OBJECT = "{}";

  static public final String DEFAULT_CHARSET = "UTF-8";

  static public final ContentType APPLICATION_JSON_CONTENT_TYPE = ContentType.create("application/json", DEFAULT_CHARSET);
  static public final ContentType APPLICATION_JAVASCRIPT_CONTENT_TYPE = ContentType.create("application/javascript", DEFAULT_CHARSET);
  static public final ContentType APPLICATION_HTML_CONTENT_TYPE = ContentType.create("text/html", DEFAULT_CHARSET);
  static public final ContentType APPLICATION_CSS_CONTENT_TYPE = ContentType.create("text/css", DEFAULT_CHARSET);
  static public final ContentType IMAGE_GIF_CONTENT_TYPE = ContentType.create("image/gif", DEFAULT_CHARSET);
  static public final ContentType IMAGE_JPEG_CONTENT_TYPE = ContentType.create("image/jpeg", DEFAULT_CHARSET);
  static public final ContentType IMAGE_PNG_CONTENT_TYPE = ContentType.create("image/png", DEFAULT_CHARSET);
  static public final ContentType IMAGE_ICO_CONTENT_TYPE = ContentType.create("image/x-icon", DEFAULT_CHARSET);

  private MimeAndFileResourcesUtil() {
    throw new IllegalStateException();
  }
}
