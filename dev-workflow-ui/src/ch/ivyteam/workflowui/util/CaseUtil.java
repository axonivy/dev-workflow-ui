package ch.ivyteam.workflowui.util;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.casemap.runtime.model.ICaseMap;
import ch.ivyteam.ivy.casemap.runtime.repo.restricted.ICaseMapBusinessCase;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.internal.Task;
import ch.ivyteam.workflowui.starts.StartableModel;

@SuppressWarnings("restriction")
public class CaseUtil {

  public static List<ITask> filterTasksOfCase(List<ITask> tasks, boolean showSystemTasks) {
    if (showSystemTasks) {
      return tasks;
    }
    return tasks.stream()
        .filter(task -> !((Task) task).responsibles().system())
        .collect(Collectors.toList());
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

  public static void rerunCaseProcess(ICase caze) {
    var startable = new StartableModel(caze.getBusinessCase().getStartedFrom());
    LastSessionStarts.current().add(startable);
    startable.execute();
  }
}
