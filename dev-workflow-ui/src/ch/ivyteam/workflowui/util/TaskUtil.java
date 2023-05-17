package ch.ivyteam.workflowui.util;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.task.TaskBusinessState;
import ch.ivyteam.workflowui.starts.CustomFieldsHelper;
import ch.ivyteam.workflowui.tasks.TaskModel;

public class TaskUtil {

  public static String getName(ITask task) {
    if (StringUtils.isBlank(task.getName())) {
      return "[Task: " + task.uuid() + "]";
    }
    return task.getName();
  }

  public static List<TaskModel> toTaskModelList(List<ITask> tasks) {
    return tasks.stream().map(TaskModel::new).collect(Collectors.toList());
  }

  public static String getStateIcon(TaskBusinessState state) {
    return switch (state) {
      case OPEN -> "controls-play task-state-open";
      case IN_PROGRESS -> "hourglass task-state-in-progress";
      case DONE -> "check-circle-1 task-state-done";
      case DESTROYED -> "alert-circle task-state-destroyed";
      case DELAYED -> "alarm-bell-timer task-state-delayed";
      case ERROR -> "mood-warning task-state-failed";
      default -> "synchronize-arrows task-state-system";
    };
  }

  public static String getPriorityIcon(ITask task) {
    return switch (task.getPriority()) {
      case EXCEPTION -> "alert-circle";
      case HIGH -> "arrow-up-1";
      case LOW -> "arrow-down-1";
      case NORMAL -> "subtract";
      default -> "subtract";
    };
  }

  public static ITask getTaskById(String uuid) {
    return IWorkflowContext.current().findTask(uuid);
  }

  public static void displayTaskRow(SelectEvent<?> event) {
    var object = event.getObject();
    if (object instanceof TaskModel model) {
      redirectToTaskDetails(model);
    }
  }

  public static void executeTaskRow(SelectEvent<?> event) {
    var object = event.getObject();
    if (object instanceof TaskModel model) {
      executeTask(model);
    }
  }

  public static void redirectToTaskDetails(TaskModel model) {
    RedirectUtil.redirect(model.getDetailUrl());
  }

  public static void executeTask(TaskModel model) {
    var task = IWorkflowContext.current().findTask(model.getUuid());
    RedirectUtil.redirect(createTaskUrl(new TaskModel(task)));
  }

  public static String createTaskUrl(TaskModel task) {
    if (shouldOpenInFrame(task)) {
      return UrlUtil.generateStartFrameUrl(task.getStartLink());
    }
    return task.getStartLink().get();
  }

  private static boolean shouldOpenInFrame(TaskModel task) {
    var taskEmbedField = new CustomFieldsHelper(task).getEmbedInFrame();
    var caseEmbedField = task.getBusinessCase().customFields().stringField(CustomFieldsHelper.EMBED_IN_FRAME);
    if (taskEmbedField == null) {
      return caseEmbedField.getOrDefault("true").equals("true");
    }
    return taskEmbedField.getValue().equals("true");
  }
}
