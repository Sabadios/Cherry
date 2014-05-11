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
package org.Cherry.Modules.Web.Engine;

import static org.Cherry.Modules.Web.MimeAndFileResourcesUtil.getFor;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.entity.FileEntity;

@Singleton
class FileRequestHandler extends RequestHandlerTemplate {
  @Override
  public void handle(final RequestCommand command) throws HttpException, IOException {
    final File file = getFile(command.getUri());

    if (!file.exists()) throw new IllegalArgumentException("File [" + file.getAbsolutePath() + "] not found, is missing or bad named!");
    if (!file.canRead() || file.isDirectory()) throw new IllegalArgumentException("File [" + file.getAbsolutePath() + "] can't be read or it is a directory!");

    debug("Serving file [{}]", file.getPath());

    command.getResponse().setStatusCode(HttpStatus.SC_OK);
    command.getResponse().setEntity(new FileEntity(file, getFor(command.getUri())));
  }

  @PostConstruct
  protected void postConstruct() {
  }

  @PreDestroy
  protected void preDestroy() {
  }

  FileRequestHandler() {
  }

  private static final long serialVersionUID = 1L;
}