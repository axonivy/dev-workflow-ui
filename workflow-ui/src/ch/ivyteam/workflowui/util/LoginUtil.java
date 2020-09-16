package ch.ivyteam.workflowui.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import ch.ivyteam.ivy.security.ISession;

public class LoginUtil
{
  public static void login(String username, String password)
  {
    if (ISession.current().loginSessionUser(username, password))
    {
      RedirectUtil.redirect();
      return;
    }
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", "Login failed"));
    RedirectUtil.redirect("login.xhtml");
  }

  public static void logout()
  {
    ISession.current().logoutSessionUser();
    RedirectUtil.redirect();
  }
}
