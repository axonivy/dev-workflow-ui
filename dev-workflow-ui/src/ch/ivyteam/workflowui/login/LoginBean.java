package ch.ivyteam.workflowui.login;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.security.identity.core.auth.oauth2.OAuth2Url;
import ch.ivyteam.ivy.security.identity.spi.auth.oauth2.OAuth2Authenticator;
import ch.ivyteam.ivy.security.restricted.ISecurityContextInternal;
import ch.ivyteam.workflowui.login.SwitchUserBean.User;
import ch.ivyteam.workflowui.util.PermissionsUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.UrlUtil;
import ch.ivyteam.workflowui.util.UserUtil;

@ManagedBean
@ViewScoped
public class LoginBean {

  private String username;
  private String password;
  private String origin;
  private OAuthProvider oauthProvider;

  public LoginBean() {
    var ctx = ISecurityContext.current();
    var provider = ((ISecurityContextInternal) ctx).identityProvider();
    if (provider.authenticator() instanceof OAuth2Authenticator) {
      var initUri = OAuth2Url.initUri(ctx, provider);
      oauthProvider = new OAuthProvider(provider.displayName(), loadResource(provider.logo()), initUri);
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

  public String getOriginPage() {
    return URLEncoder.encode(UrlUtil.evalOriginPage(), StandardCharsets.UTF_8);
  }

  private void login(User user) {
    username = user.getName();
    LoginUtil.switchUser(username, getOrigin());
  }

  public void customLogin() {
    LoginUtil.login(getUsername(), password, getOrigin());
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

  public void redirectIfNotDevMode() {
    if (!PermissionsUtil.isDemoOrDevMode()) {
      RedirectUtil.redirect();
    }
  }

  public void redirectIfNotDevModeOrAdmin() {
    if (!PermissionsUtil.isDemoDevModeOrAdmin()) {
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

  private String loadResource(URI uri) {
    try {
      return IOUtils.toString(uri, StandardCharsets.UTF_8);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
