package ch.ivyteam.workflowui.util;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.workflowui.login.LoginUtil;

public class UserUtil {

  public static List<IUser> getUsers() {
    var users = Ivy.security().users().paged().stream().collect(toList());
    Collections.sort(users, (user1, user2) -> user1.getName().compareToIgnoreCase(user2.getName()));
    return users;
  }

  public static String getRoles(IUser user) {
    return user.getRoles().stream().map(IRole::getDisplayName).collect(Collectors.joining(", "));
  }

  public static void redirectIfNotLoggedIn() {
    if (!isLoggedIn()) {
      if (PermissionsUtil.isDemoOrDevMode()) {
        LoginUtil.redirectToLoginTable();
      } else {
        LoginUtil.redirectToLoginForm();
      }
    }
  }

  public static void redirectIfNotAdmin() {
    if (!PermissionsUtil.isAdmin()) {
      RedirectUtil.redirect();
    }
  }

  public static boolean isLoggedIn() {
    return ISession.current().getSessionUser() != null;
  }
}
