package ch.ivyteam.workflowui.util;

import java.io.IOException;

import javax.faces.context.FacesContext;

public class RedirectUtil
{
  private static RedirectHandler handler = new DefaultHandler();

  public static void redirect()
  {
    redirect("home.xhtml");
  }

  public static void redirect(String url)
  {
    handler.redirect(url);
  }

  public static interface RedirectHandler
  {
    void redirect(String url);
  }

  public static void setHandler(RedirectHandler handler)
  {
    RedirectUtil.handler = handler;
  }

  private static final class DefaultHandler implements RedirectHandler
  {

    public void redirect(String url)
    {
      try
      {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null)
        {
          return;
        }
        context.getExternalContext().redirect(url);
      }
      catch (IOException e)
      {
        throw new RuntimeException("Could not send redirect", e);
      }
    }
  }
}
