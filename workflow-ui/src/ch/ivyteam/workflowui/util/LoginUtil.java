package ch.ivyteam.workflowui.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.security.ISession;

public class LoginUtil
{
  public static void login(String username, String password, String originalUrl)
  {
    if (!checkLoginAndRedirect(username, password, originalUrl))
    {
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", "Login failed"));
    }
  }

  public static void tableLogin(String username,  String originalUrl)
  {
    if (!checkLoginAndRedirect(username, "", originalUrl))
    {
      if (!checkLoginAndRedirect(username, username, originalUrl))
      {
        redirectToLoginForm();
      }
    }
  }

  private static boolean checkLoginAndRedirect(String username, String password, String originalUrl)
  {
    if (ISession.current().loginSessionUser(username, password))
    {
      RedirectUtil.redirect(StringUtils.isNotBlank(originalUrl) ? originalUrl : "home.xhtml");
      return true;
    }
    return false;
  }

  public static void logout()
  {
    ISession.current().logoutSessionUser();
    RedirectUtil.redirect();
  }

  public static void redirectToLoginForm()
  {
    RedirectUtil.redirect("login.xhtml");
  }

  public static void redirectToTable()
  {
    RedirectUtil.redirect("loginTable.xhtml");
  }
}
