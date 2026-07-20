package ch.ivyteam.workflowui.login;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.workflowui.login.SwitchUserBean.User;
import ch.ivyteam.workflowui.util.PermissionsUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.UrlUtil;
import ch.ivyteam.workflowui.util.UserUtil;

@Named
@ViewScoped
public class LoginBean implements Serializable {

  private String username;
  private String password;
  private String origin;
  private String originalUrl;
  private OAuthProvider oauthProvider;

  public LoginBean() {
    var authProviders = ISecurityContext.current().authProviders();
    if (authProviders.size() > 0) {
      var authProvider = authProviders.get(0);
      oauthProvider = new OAuthProvider(authProvider.displayName(), authProvider.logo(), authProvider.url());
    }
  }

  public void loginFromTable(SelectEvent<?> event) {
    var object = event.getObject();
    if (object instanceof User user) {
      login(user);
    }
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getOriginPage() {
    return URLEncoder.encode(UrlUtil.evalOriginPage(), StandardCharsets.UTF_8);
  }

  private void login(User user) {
    username = user.getName();
    LoginUtil.switchUser(username, getOrigin());
  }

  public void customLogin() {
    LoginUtil.login(getUsername(), password, getOrigin(), getOriginalUrl());
    password = "";
  }

  public void logout() {
    LoginUtil.logout();
  }

  public boolean isLoggedIn() {
    return UserUtil.isLoggedIn();
  }

  public void redirectIfNotLoggedIn() {
    UserUtil.redirectIfNotLoggedIn();
  }

  public void redirectIfNotAdmin() {
    UserUtil.redirectIfNotAdmin();
  }

  public void redirectIfNotDemoOrDevMode() {
    if (!PermissionsUtil.isDemoOrDevMode()) {
      RedirectUtil.redirect();
    }
  }

  public void redirectIfNotDemoDevModeOrAdmin() {
    if (!PermissionsUtil.isDemoDevModeOrAdmin()) {
      RedirectUtil.redirect();
    }
  }

  public void redirectIfNotDevModeAndAdmin() {
    if (!PermissionsUtil.isDevModeAndAdmin()) {
      RedirectUtil.redirect();
    }
  }

  public String getRoles(IUser user) {
    return UserUtil.getRoles(user);
  }

  public List<IUser> getUsers() {
    return UserUtil.getUsers();
  }

  public String focus() {
    return StringUtils.isEmpty(username) ? "userName" : "password";
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public OAuthProvider getOAuthProvider() {
    return oauthProvider;
  }

}
