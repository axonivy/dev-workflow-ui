package ch.ivyteam.workflowui.login;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.restricted.IUserInternal;
import ch.ivyteam.licence.RuntimeLicenceException;
import ch.ivyteam.workflowui.util.PermissionsUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.UrlUtil;
import ch.ivyteam.workflowui.util.UserUtil;
import ch.ivyteam.workflowui.util.url.Page;

public class LoginUtil {

  public static void login(String username, String password, String origin, String originalUrl) {
    try {
      if (!attemptLogin(username, password)) {
        sendUnauthorizedStatusCode();
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", "Login failed"));
        return;
      }
      redirectAfterLogin(origin, originalUrl);
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
    if (user != null && ((IUserInternal) user).isTestUser()) {
      if (attemptLogin(username, username)) {
        redirectAfterLogin(origin, null);
        return;
      }
    }
    redirectToLoginForm();
  }

  private static boolean attemptLogin(String username, String password) {
    return ISession.current().loginSessionUser(username, password);
  }

  static void redirectAfterLogin(String origin, String originalUrl) {
    if (origin != null && !origin.isBlank()) {
      RedirectUtil.redirect(Page.fromString(origin));
    } else if (originalUrl != null && !originalUrl.isBlank()) {
      RedirectUtil.redirectRelative(originalUrl);
    } else {
      RedirectUtil.redirect();
    }
  }

  public static void logout() {
    ISession.current().logoutSessionUser();
    UserUtil.redirectIfNotLoggedIn();
  }

  public static void redirectToLoginForm() {
    redirectToLogin(Page.LOGIN);
  }

  public static void redirectToLoginTable() {
    redirectToLogin(Page.SWITCH_USER);
  }

  private static void redirectToLogin(Page loginPage) {
    RedirectUtil.redirect(loginPage, Map.of("origin", UrlUtil.evalOriginPage()));
  }
}
