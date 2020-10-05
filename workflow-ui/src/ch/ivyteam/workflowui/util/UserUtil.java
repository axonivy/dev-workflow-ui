package ch.ivyteam.workflowui.util;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.security.IPermission;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.IUser;

public class UserUtil
{
  public static List<IUser> getUsers()
  {
    var users = IApplication.current().getSecurityContext()
            .users().paged().stream().collect(toList());
    Collections.sort(users, (user1, user2) -> user1.getName().compareToIgnoreCase(user2.getName()));
    return users;
  }

  public static String getRoles(IUser user)
  {
    return user.getRoles().stream().map(IRole::getDisplayName).collect(Collectors.joining(", "));
  }

  public static void makeAdmin()
  {
    IUser developerTestUser = IApplication.current().getSecurityContext().users().find("DeveloperTest");
    IApplication.current().getSecurityDescriptor().grantPermission(IPermission.TASK_READ_ALL,
            developerTestUser);
    IApplication.current().getSecurityDescriptor().grantPermission(IPermission.CASE_READ_ALL,
            developerTestUser);
  }

  public static void redirectIfNotLoggedIn()
  {
    if (!isLoggedIn())
    {
      RedirectUtil.redirect("loginTable.xhtml");
    }
  }

  public static boolean isLoggedIn()
  {
    if (ISession.current().getSessionUser() == null)
    {
      return false;
    }
    return true;
  }

}
