package ch.ivyteam.workflowui.util;

import java.io.IOException;
import java.net.URI;

import javax.faces.context.FacesContext;

import ch.ivyteam.util.uri.UriChecker;

public class RedirectUtil {
  private static RedirectHandler handler = new DefaultHandler();

  public static void redirect() {
    redirect("home.xhtml");
  }

  public static void redirect(String url) {
    handler.redirect(url);
  }

  public static void redirectRelative(String url) {
    if (!isSafeLocalRedirect(url)) {
      throw new RuntimeException("Redirecting to external websites is not allowed. Tried to redirect to: " + url);
    }
    redirect(url);
  }

  private static boolean isSafeLocalRedirect(String url) {
    if (url == null || url.isBlank()) {
      return false;
    }
    try {
      var uri = URI.create(url);
      if (uri.isAbsolute() || uri.getRawAuthority() != null) {
        return false;
      }
      var validationUrl = url.startsWith("/") ? url : "/" + url;
      return UriChecker.isSafeLocalRedirect(validationUrl);
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }

  public interface RedirectHandler {
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
