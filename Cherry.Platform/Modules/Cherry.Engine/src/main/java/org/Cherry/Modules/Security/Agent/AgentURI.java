/**
 *
 */
package org.Cherry.Modules.Security.Agent;

import static org.Cherry.Modules.Web.WebConstants.ROOT_URI;
import static org.Cherry.Modules.Web.WebConstants.URI_TOKEN;

/**
 * @author Cristian.Malinescu
 * 
 */
public abstract class AgentURI {
  static public final class Index extends AgentURI {
    static public final String BASE_URI = ROOT_URI;
  }

  static public final class About extends AgentURI {
    static public final String BASE_URI = Index.BASE_URI + "about";
  }

  static public final class Contact extends AgentURI {
    static public final String BASE_URI = Index.BASE_URI + "contact";
  }

  static public final class User extends AgentURI {
    static public final String BASE_URI = Index.BASE_URI + "user",
        SAVE_URI = URI_TOKEN + "save",
        USERS_URI = URI_TOKEN + "users";
  }

  static public final class GeteKeeper extends AgentURI {
    static public final String BASE_URI = Index.BASE_URI + "gate",
        OPEN_URI = URI_TOKEN + "open",
        CLOSE_URI = URI_TOKEN + "close";
  }

  static public final class Upload extends AgentURI {
    static public final String BASE_URI = Index.BASE_URI + "upload";
  }
}
