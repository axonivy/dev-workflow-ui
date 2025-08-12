package ch.ivyteam.workflowui.util;

import java.io.IOException;
import java.util.Map;

import javax.faces.context.FacesContext;

import ch.ivyteam.ivy.jsf.bean.wf.RedirectBean;

public class RedirectUtil {

  private static final Map<String, String> ORIGIN_TO_PAGE = Map.ofEntries(
      Map.entry("api-browser", "api-browser.xhtml"),
      Map.entry("case", "case.xhtml"),
      Map.entry("cases", "cases.xhtml"),
      Map.entry("cleanup", "cleanup.xhtml"),
      Map.entry("end", "end.xhtml"),
      Map.entry("frame", "frame.xhtml"),
      Map.entry("home", "home.xhtml"),
      Map.entry("intermediate-event", "intermediate-event.xhtml"),
      Map.entry("intermediate-events", "intermediate-events.xhtml"),
      Map.entry("login", "login.xhtml"),
      Map.entry("profile", "profile.xhtml"),
      Map.entry("signals", "signals.xhtml"),
      Map.entry("starts", "starts.xhtml"),
      Map.entry("statistics", "statistics.xhtml"),
      Map.entry("switch-user", "switch-user.xhtml"),
      Map.entry("task", "task.xhtml"),
      Map.entry("tasks", "tasks.xhtml"),
      Map.entry("webservices", "webservices.xhtml"));

  public static void redirect() {
    redirect("home");
  }

  public static void redirect(String page) {
    var pageName = page.contains("?") ? page.substring(0, page.indexOf("?")) : page;
    var queryParams = page.contains("?") ? page.substring(page.indexOf("?")) : "";

    var url = ORIGIN_TO_PAGE.get(pageName);
    if (url == null) {
      throw new IllegalArgumentException("Page '" + pageName + "' is not whitelisted for redirect");
    }
    redirectUnsafe(url + queryParams);
  }

  public static void redirectRelative(String url) {
    try {
      RedirectBean.checkUrl(url);
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
}
