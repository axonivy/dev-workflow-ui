package ch.ivyteam.workflowui.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

import ch.ivyteam.ivy.workflow.ITask;

public class CaseDetailUtil {
  public static List<ITask> filterTasksOfCase(List<ITask> tasks, boolean showSystemTasks) {
    if (!showSystemTasks) {
      tasks = new ArrayList<>(tasks);
      CollectionUtils.filterInverse(tasks, task -> "#SYSTEM".equals(task.getActivatorName()));
    }
    return tasks;
  }
}
