package ch.ivyteam.workflowui.login;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.licence.RuntimeLicenceException;
import ch.ivyteam.workflowui.util.EngineModeUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.UrlUtil;

public class LoginUtil {

  public static void login(String username, String password, String originalUrl) {
    try {
      if (!checkLoginAndRedirect(username, password, originalUrl)) {
        sendUnauthorizedStatusCode();
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", "Login failed"));
      }
    } catch (RuntimeLicenceException ex) {
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage()));
    }
  }

  private static void sendUnauthorizedStatusCode() {
    var response = FacesContext.getCurrentInstance().getExternalContext().getResponse();
    if (response instanceof HttpServletResponse httpResponse) {
      httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  public static void tableLogin(String username, String originalUrl) {
    if (!EngineModeUtil.isDemoOrDesigner()) {
      return;
    }
    if (!checkLoginAndRedirect(username, "", originalUrl)) {
      if (!checkLoginAndRedirect(username, username, originalUrl)) {
        redirectToLoginForm();
      }
    }
  }

  private static boolean checkLoginAndRedirect(String username, String password, String originalUrl) {
    if (ISession.current().loginSessionUser(username, password)) {
      RedirectUtil.redirect(StringUtils.isNotBlank(originalUrl) ? originalUrl : "home.xhtml");
      return true;
    }
    return false;
  }

  public static void logout() {
    ISession.current().logoutSessionUser();
    RedirectUtil.redirect();
  }

  public static void redirectToLoginForm() {
    String origin = URLEncoder.encode(UrlUtil.evalOriginalPage(), StandardCharsets.UTF_8);
    RedirectUtil.redirect("login.xhtml?originalUrl=" + origin);
  }

  public static void redirectToLoginTable() {
    String origin = URLEncoder.encode(UrlUtil.evalOriginalPage(), StandardCharsets.UTF_8);
    RedirectUtil.redirect("loginTable.xhtml?originalUrl=" + origin);
  }
}
