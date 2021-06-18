package ch.ivyteam.workflowui;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.security.ISecurityConstants;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.workflowui.LoginTableBean.User;
import ch.ivyteam.workflowui.util.LoginUtil;
import ch.ivyteam.workflowui.util.UserUtil;

@ManagedBean
@SessionScoped
public class LoginBean
{
  private String username;
  private String password;
  private String originalUrl;

  public void loginFromTable(SelectEvent event)
  {
    Object object = event.getObject();
    if (object instanceof User)
    {
      login((User) object);
    }
  }

  private void login(User user)
  {
    username = user.getName();
    password = "";
    if (ISecurityConstants.DEVELOPER_USER_NAME.equals(username))
    {
      password = username;
    }
    LoginUtil.login(username, password, getOriginalUrl());
  }

  public void customLogin()
  {
    LoginUtil.login(getUsername(), password, getOriginalUrl());
    password = "";
  }

  public void logout()
  {
    LoginUtil.logout();
  }

  public void checkIfLoggedIn()
  {
    evalOriginalUrl();
    UserUtil.redirectIfNotLoggedIn();
  }

  public void redirectToLoginPage()
  {
    evalOriginalUrl();
    LoginUtil.redirect();
  }

  public void resetOriginalUrlAndRedirect()
  {
    originalUrl = "";
    LoginUtil.redirectToTable();
  }

  private void evalOriginalUrl()
  {
    var request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    originalUrl = request.getRequestURI();
  }

  public String getRoles(IUser user)
  {
    return UserUtil.getRoles(user);
  }

  public List<IUser> getUsers()
  {
    return UserUtil.getUsers();
  }

  public String focus()
  {
    return StringUtils.isEmpty(username) ? "username" : "password";
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getOriginalUrl()
  {
    return originalUrl;
  }
}
