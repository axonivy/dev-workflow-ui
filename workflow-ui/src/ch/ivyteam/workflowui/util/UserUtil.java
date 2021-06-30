package ch.ivyteam.workflowui.util;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

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

  public static void redirectIfNotLoggedIn()
  {
    if (!isLoggedIn())
    {
      if (EngineModeUtil.isDemoOrDesigner())
      {
        LoginUtil.redirectToTable();
      }
      else
      {
        LoginUtil.redirect();
      }
    }
  }

  public static boolean checkIfPersonalTasks()
  {
    return checkCurrentPage("tasks");
  }

  public static boolean isLoggedIn()
  {
    return ISession.current().getSessionUser() != null;
  }

  private static boolean checkCurrentPage(String page)
  {
    String currentUrl = FacesContext.getCurrentInstance().getViewRoot().getViewId();
    String currentPage = StringUtils.substringAfterLast(currentUrl, "/");
    return currentPage.equals(page + ".xhtml");
  }

  public static boolean isAdmin()
  {
    return hasPermission(IPermission.TASK_READ_ALL) & hasPermission(IPermission.CASE_READ_ALL);
  }

  private static boolean hasPermission(IPermission... permissions)
  {
    return Arrays.stream(permissions).anyMatch(
            p -> ISession.current().hasPermission(IApplication.current().getSecurityDescriptor(), p));
  }

}
