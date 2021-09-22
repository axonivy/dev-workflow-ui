package ch.ivyteam.workflowui.util;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.workflowui.tasks.TaskModel;

public class TaskUtil {

  public static String getName(ITask task) {
    if (StringUtils.isBlank(task.getName())) {
      return "[Task: " + task.getId() + "]";
    }
    return task.getName();
  }

  public static List<TaskModel> toTaskModelList(List<ITask> tasks) {
    return tasks.stream().map(TaskModel::new).collect(Collectors.toList());
  }

  public static String getStateIcon(ITask task) {
    switch (task.getState()) {
      case DELAYED:
        return "alarm-bell-timer task-state-delayed";
      case DONE:
        return "check-circle-1 task-state-done";
      case FAILED:
      case JOIN_FAILED:
        return "mood-warning task-state-failed";
      case PARKED:
        return "touch-finger_1 task-state-reserved";
      case CREATED:
      case RESUMED:
        return "hourglass task-state-in-progress";
      case SUSPENDED:
        return "controls-play task-state-open";
      case WAITING_FOR_INTERMEDIATE_EVENT:
        return "synchronize-arrow-clock task-state-waiting";
      case DESTROYED:
      case ZOMBIE:
        return "alert-circle task-state-zombie-destroyed";
      default:
        return "synchronize-arrows task-state-system";
    }
  }

  public static String getPriorityIcon(ITask task) {
    switch (task.getPriority()) {
      case EXCEPTION:
        return "alert-circle";
      case HIGH:
        return "arrow-up-1";
      case LOW:
        return "arrow-down-1";
      case NORMAL:
        return "subtract";
      default:
        return "subtract";
    }
  }

  public static ITask getTaskById(long id) {
    return IWorkflowContext.current().findTask(id);
  }

  public static void displayTaskRow(SelectEvent event) {
    Object object = event.getObject();
    if (object instanceof TaskModel) {
      redirectToTaskDetails(((TaskModel) object).getId());
    }
  }

  public static void executeTaskRow(SelectEvent event) {
    Object object = event.getObject();
    if (object instanceof TaskModel) {
      executeTask(((TaskModel) object).getId());
    }
  }

  public static void redirectToTaskDetails(long id) {
    RedirectUtil.redirect("taskDetails.xhtml?task=" + id);
  }

  public static void executeTask(long id) {
    ITask task = IWorkflowContext.current().findTask(id);
    RedirectUtil.redirect(UrlUtil.generateStartFrameUrl(task.getStartLink()));
  }
}
