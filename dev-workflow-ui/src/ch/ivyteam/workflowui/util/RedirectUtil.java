package ch.ivyteam.workflowui.util;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

public class RedirectUtil {
  private static final Pattern SCHEME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+.-]*:");

  private static RedirectHandler handler = new DefaultHandler();

  public static void redirect() {
    redirect("home.xhtml");
  }

  public static void redirect(String url) {
    handler.redirect(url);
  }

  public static void redirectRelative(String url) {
    var trimmed = url.trim();
    if (trimmed.isEmpty() || trimmed.startsWith("//") || trimmed.contains("\\")
        || SCHEME_PATTERN.matcher(trimmed).find()) {
      throw new RuntimeException(
          "Redirecting to external websites is not allowed. Tried to redirect to: " + url);
    }
    redirect(url);
  }

  public static interface RedirectHandler {
    void redirect(String url);
  }

  public static void setHandler(RedirectHandler handler) {
    RedirectUtil.handler = handler;
  }

  private static final class DefaultHandler implements RedirectHandler {
    @Override
    public void redirect(String url) {
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
  }
}
