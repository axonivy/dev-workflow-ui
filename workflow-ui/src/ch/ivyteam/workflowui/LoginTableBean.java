package ch.ivyteam.workflowui;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.workflowui.util.UserUtil;

@ManagedBean
@ViewScoped
public class LoginTableBean {
  private final List<User> users;

  public LoginTableBean() {
    users = UserUtil.getUsers().stream().map(LoginTableBean::toUser).collect(toList());
  }

  public List<User> getUsers() {
    return users;
  }

  public User getCurrentUser() {
    return toUser(ISession.current().getSessionUser());
  }

  private static User toUser(IUser user) {
    if (user != null) {
      String roles = UserUtil.getRoles(user);
      return new User(user.getName(), user.getFullName(), roles);
    }
    return null;
  }

  public static class User {
    private final String name;
    private final String fullname;
    private final String roles;

    private User(String name, String fullname, String roles) {
      this.name = name;
      this.fullname = fullname;
      this.roles = roles;
    }

    public String getName() {
      return name;
    }

    public String getFullname() {
      return fullname;
    }

    public String getRoles() {
      return roles;
    }
  }
}
