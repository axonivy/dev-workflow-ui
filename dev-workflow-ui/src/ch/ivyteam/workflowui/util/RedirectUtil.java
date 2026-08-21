package ch.ivyteam.workflowui.util;

import java.io.IOException;
import java.net.URI;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

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
    var redirectUrl = resolveRedirectUrl(url);
    if (!UriChecker.isSafeRootRelativeRedirect(redirectUrl)) {
      throw new RuntimeException("Redirecting to external websites is not allowed. Tried to redirect to: " + url);
    }
    redirect(redirectUrl);
  }

  private static String resolveRedirectUrl(String originalUrl) {
    var redirectUrl = originalUrl;
    if (redirectUrl != null && !redirectUrl.startsWith("/")) {
      try {
        if (URI.create(redirectUrl).isAbsolute()) {
          return null;
        }
        redirectUrl = "/" + redirectUrl;
      } catch (IllegalArgumentException ex) {
        return null;
      }
    }
    if (!UriChecker.isSafeRootRelativeRedirect(redirectUrl)) {
      return null;
    }
    var context = FacesContext.getCurrentInstance();
    if (context == null || !(context.getExternalContext().getRequest() instanceof HttpServletRequest request)) {
      return redirectUrl;
    }
    var requestUri = request.getRequestURI();
    var lastSlash = requestUri == null ? -1 : requestUri.lastIndexOf('/');
    if (lastSlash < 0) {
      return null;
    }
    return requestUri.substring(0, lastSlash) + redirectUrl;
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
