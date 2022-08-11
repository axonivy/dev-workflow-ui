package ch.ivyteam.workflowui.login;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.security.identity.IdentityProvider;
import ch.ivyteam.ivy.security.identity.auth.oauth2.OAuth2Authenticator;
import ch.ivyteam.ivy.security.restricted.ISecurityContextInternal;
import ch.ivyteam.workflowui.login.LoginTableIvyDevWfBean.User;
import ch.ivyteam.workflowui.util.UserUtil;

@ManagedBean
@ViewScoped
public class LoginIvyDevWfBean {
  private String username;
  private String password;
  private String originalUrl;

  public void loginFromTable(SelectEvent<?> event) {
    Object object = event.getObject();
    if (object instanceof User) {
      login((User) object);
    }
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  private void login(User user) {
    username = user.getName();
    LoginUtil.tableLogin(username, getOriginalUrl());
  }

  public void customLogin() {
    LoginUtil.login(getUsername(), password, getOriginalUrl());
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

  public void redirectToLoginForm() {
    LoginUtil.redirectToLoginForm();
  }

  public void redirectToLoginTable() {
    LoginUtil.redirectToLoginTable();
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

  public List<OAuthProvider> getOAuthProviders() {
    var securityContext = (ISecurityContextInternal) ISecurityContext.current();
    return securityContext.identityProviders().stream()
            .filter(p -> p.authenticator(null) instanceof OAuth2Authenticator)
            .map(this::toOAuthPovider)
            .collect(Collectors.toList());
  }

  private OAuthProvider toOAuthPovider(IdentityProvider provider) {
    var link = ISecurityContext.current().contextPath() + "/oauth2/" + provider.id() + "/init";
    return new OAuthProvider(provider.name(), loadResource(provider.logo()), link);
  }

  private String loadResource(URI uri) {
    try {
      return IOUtils.toString(uri, StandardCharsets.UTF_8);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
