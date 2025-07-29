package ch.ivyteam.workflowui.util;

import java.io.IOException;
import java.util.Map;

import javax.faces.context.FacesContext;

public class RedirectUtil {
  private static RedirectHandler handler = new DefaultHandler();

  private static final Map<String, String> ORIGIN_TO_PAGE = Map.ofEntries(
      Map.entry("home", "home.xhtml"),
      Map.entry("starts", "starts.xhtml"),
      Map.entry("tasks", "tasks.xhtml"),
      Map.entry("task", "task.xhtml"),
      Map.entry("cases", "cases.xhtml"),
      Map.entry("case", "case.xhtml"),
      Map.entry("login", "login.xhtml"),
      Map.entry("switch-user", "switch-user.xhtml"),
      Map.entry("end", "end.xhtml"),
      Map.entry("signals", "signals.xhtml"),
      Map.entry("intermediate-events", "intermediate-events.xhtml"),
      Map.entry("api-browser", "api-browser.xhtml"),
      Map.entry("statistics", "statistics.xhtml"),
      Map.entry("webservices", "webservices.xhtml"));

  public static void redirect() {
    redirect("home");
  }

  public static void redirect(String page) {
    handler.redirect(page);
  }

  public interface RedirectHandler {
    void redirect(String url);
  }

  public static void setHandler(RedirectHandler handler) {
    RedirectUtil.handler = handler;
  }

  private static final class DefaultHandler implements RedirectHandler {

    @Override
    public void redirect(String page) {
      try {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
          return;
        }
        var url = ORIGIN_TO_PAGE.get(page);
        if (url != null) {
          page = url;
        }
        context.getExternalContext().redirect(page);
      } catch (IOException e) {
        throw new RuntimeException("Could not send redirect", e);
      }
    }
  }
}
