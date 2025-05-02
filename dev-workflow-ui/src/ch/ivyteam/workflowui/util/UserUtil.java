package ch.ivyteam.workflowui.util;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.workflowui.login.LoginUtil;

public class UserUtil {

  public static List<IUser> getUsers() {
    return Ivy.security().users().paged().stream()
        .sorted(Comparator.comparing(IUser::getName))
        .collect(Collectors.toList());
  }

  public static String getRoles(IUser user) {
    return user.getRoles().stream()
        .map(IRole::getDisplayName)
        .collect(Collectors.joining(", "));
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
