package ch.ivyteam.workflowui.util;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.casemap.runtime.ICaseMapService;
import ch.ivyteam.ivy.casemap.runtime.model.ICaseMap;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.restricted.caze.CaseInternal;
import ch.ivyteam.workflowui.cases.CaseModel;
import ch.ivyteam.workflowui.starts.StartableModel;

public class CaseUtil {

  public static List<ITask> filterTasksOfCase(List<ITask> tasks, boolean showSystemTasks) {
    if (showSystemTasks) {
      return tasks;
    }
    return tasks.stream()
        .filter(task -> !task.responsibles().isSystem())
        .collect(Collectors.toList());
  }

  public static String getPrettyName(ICase caze) {
    if (StringUtils.isBlank(caze.getName())) {
      return "[Case: " + caze.uuid() + "]";
    }
    return caze.getName();
  }

  public static ICaseMap getCaseMap(ICase caze) {
    return ICaseMapService.current().find().byBusinessCase(caze.getBusinessCase());
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
    if (caze == null) {
      return false;
    }
    var session = ISession.current();
    if (session == null) {
      return false;
    }
    if (session.getSessionUser() == null) {
      return false;
    }
    if (PermissionsUtil.isAdmin()) {
      return true;
    }
    if (caze instanceof CaseInternal) {
      return ((CaseInternal) caze).involved().members().stream().anyMatch(u -> u.isMember(session));
    }
    return false;
  }
}
