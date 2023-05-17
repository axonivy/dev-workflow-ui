package ch.ivyteam.workflowui.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.casemap.runtime.model.ICaseMap;
import ch.ivyteam.ivy.casemap.runtime.repo.restricted.ICaseMapBusinessCase;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;

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
}
