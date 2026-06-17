package ch.ivyteam.workflowui;

import java.net.URI;

public class UriChecker {

  public static boolean isRelative(String url) {
    var uri = URI.create(url);
    return checkIsPathValid(uri.getPath()) && checkIsPathValid(uri.toString());
  }

  private static boolean checkIsPathValid(String url) {
    if (!url.startsWith("/")) {
      return false;
    }

    if (url.length() > 1) {
      var secondChar = url.charAt(1);
      if (secondChar == '/') {
        return false;
      }
    }
    return true;
  }
}
