package ch.ivyteam.workflowui.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.casemap.runtime.model.ICaseMap;
import ch.ivyteam.ivy.casemap.runtime.repo.restricted.ICaseMapBusinessCase;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.internal.caze.Case;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.workflowui.cases.CaseModel;
import ch.ivyteam.workflowui.starts.StartableModel;

@SuppressWarnings("restriction")
public class CaseUtil {

  public static List<ITask> filterTasksOfCase(List<ITask> tasks, boolean showSystemTasks) {
    if (!showSystemTasks) {
      tasks = new ArrayList<>(tasks);
      CollectionUtils.filterInverse(tasks, task -> "#SYSTEM".equals(task.getActivatorName()));
    }
    return tasks;
  }

  public static String getPrettyName(ICase caze) {
    if (StringUtils.isBlank(caze.getName())) {
      return "[Case: " + caze.uuid() + "]";
    }
    return caze.getName();
  }

  public static ICaseMap getCaseMap(ICase caze) {
    var caseMapBusinessCase = ICaseMapBusinessCase.of(caze.getBusinessCase());
    if (caseMapBusinessCase != null) {
      return caseMapBusinessCase.getCaseMap();
    }
    return null;
  }

  public static ICase getCaseById(String uuid) {
    return IWorkflowContext.current().findCase(uuid);
  }

  public static void rerunCaseProcess(CaseModel caze) {
    var businessCase = caze.getCase().getBusinessCase();
    var startable = new StartableModel(businessCase.getStartedFrom());
    LastSessionStarts.current().add(startable);
    startable.execute();
  }

  public static boolean canAccess(ICase caze) {
    Ivy.log().info("caze: " + caze);
    if (caze == null) {
      return false;
    }
    var session = ISession.current();
    Ivy.log().info("session: " + session);
    if (session == null) {
      return false;
    }
    Ivy.log().info("session user: " + session.getSessionUser());
    if (session.getSessionUser() == null) {
      return false;
    }
    if (caze instanceof Case) {
      Ivy.log().info("caze is instanceof Case");
//      return ((Case) caze).involved().members().stream().anyMatch(u -> u.isMember(session, true));
      var allMembers = ((Case) caze).involved().members();
      Ivy.log().info("allMembers:" + allMembers);
      allMembers.stream().forEach(u -> {
        Ivy.log().info("Involved member " + u);
        if (u.isMember(session, true)) {
          Ivy.log().info("Involved member is session user");
        }
      });
      
      return allMembers.stream().anyMatch(u -> u.isMember(session, true));

//      var members = ((Case) caze).involved().members();
//      Stream.of().anyMatch(u -> u.isMember(session, true));
    } else {
      Ivy.log().info("caze is NOT instanceof Case");
      // TODO: Should this case even be handled? If yes with an Exception?
      return false;
    }
  }
}
