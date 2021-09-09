package ch.ivyteam.workflowui.util;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.TaskState;

public class TaskDetailUtil {

  public static String getName(ITask task) {
    if (StringUtils.isBlank(task.getName())) {
      return "[Task: " + task.getId() + "]";
    }
    return task.getName();
  }

  public static boolean isDone(ITask task) {
    return TaskState.END_STATES.contains(task.getState());
  }
}
