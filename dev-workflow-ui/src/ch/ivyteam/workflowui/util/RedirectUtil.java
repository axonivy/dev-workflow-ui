package ch.ivyteam.workflowui.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;

import ch.ivyteam.util.uri.UriChecker;
import ch.ivyteam.workflowui.util.url.Page;

public final class RedirectUtil {

  public static void redirect() {
    redirect(Page.HOME);
  }

  public static void redirect(Page page) {
    redirect(page, Collections.emptyMap());
  }

  public static void redirect(Page page, Map<String, String> parameters) {
    Objects.requireNonNull(page, "page");
    var url = page.getView();
    if (parameters != null && !parameters.isEmpty()) {
      url = url + "?" + buildQueryString(parameters);
    }
    redirectUnsafe(url);
  }

  public static void redirectRelative(String url) {
    try {
      UriChecker.checkUrl(url);
    } catch (RuntimeException e) {
      throw new RuntimeException("Redirecting to external websites is not allowed. Tried to redirect to: " + url, e);
    }
    redirectUnsafe(url);
  }

  // This should be only used if you are 100% sure the url is safe
  public static void redirectUnsafe(String url) {
    try {
      FacesContext context = FacesContext.getCurrentInstance();
      if (context == null) {
        return;
      }
      context.getExternalContext().redirect(url);
    } catch (IOException e) {
      throw new RuntimeException("Could not send redirect", e);
    }
  }

  private static String buildQueryString(Map<String, String> parameters) {
    return parameters.entrySet().stream()
        .filter(e -> e.getKey() != null && e.getValue() != null)
        .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)
            + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&"));
  }
}
