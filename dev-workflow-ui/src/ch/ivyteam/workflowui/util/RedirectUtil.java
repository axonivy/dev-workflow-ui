package ch.ivyteam.workflowui.util;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

public class RedirectUtil {
  private static RedirectHandler handler = new DefaultHandler();

  private static final Map<String, String> ORIGIN_TO_PAGE = Collections.unmodifiableMap(new HashMap<String, String>(){
    {
      put("home", "home.xhtml");
      put("starts", "starts.xhtml");
      put("task", "task.xhtml");
      put("tasks", "tasks.xhtml");
      put("case", "case.xhtml");
      put("cases", "cases.xhtml");
      put("login", "login.xhtml");
      put("switch-user", "switch-user.xhtml");
      put("end", "end.xhtml");
    }
  });

  public static void redirect() {
    redirect("home.xhtml");
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
