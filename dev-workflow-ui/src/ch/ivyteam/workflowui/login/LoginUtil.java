package ch.ivyteam.workflowui.login;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.licence.RuntimeLicenceException;
import ch.ivyteam.workflowui.util.PermissionsUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.UrlUtil;

public class LoginUtil {

  public static void login(String username, String password, String origin) {
    try {
      if (!checkLoginAndRedirect(username, password, origin)) {
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

  public static void switchUser(String username, String origin) {
    if (!PermissionsUtil.isDemoOrDevMode()) {
      return;
    }
    var user = ISecurityContext.current().users().find(username);
    if (user != null && ((ch.ivyteam.ivy.security.internal.user.User) user).isTestUser()) {
      if (checkLoginAndRedirect(username, username, origin)) {
        return;
      }
    }
    redirectToLoginForm();
  }

  private static boolean checkLoginAndRedirect(String username, String password, String origin) {
    if (ISession.current().loginSessionUser(username, password)) {
      RedirectUtil.redirect(StringUtils.isNotBlank(origin) ? origin : "home");
      return true;
    }
    return false;
  }

  public static void logout() {
    ISession.current().logoutSessionUser();
    RedirectUtil.redirect();
  }

  public static void redirectToLoginForm() {
    redirectToLogin("login.xhtml");
  }

  public static void redirectToLoginTable() {
    redirectToLogin("switch-user.xhtml");
  }

  private static void redirectToLogin(String page) {
    String origin = URLEncoder.encode(UrlUtil.evalOriginPage(), StandardCharsets.UTF_8);
    RedirectUtil.redirect(page + "?origin=" + origin);
  }
}
