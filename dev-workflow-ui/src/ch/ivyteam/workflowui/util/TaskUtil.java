package ch.ivyteam.workflowui.util;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
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
    RedirectUtil.redirect(createTaskUrl(model));
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
      return "true".equals(caseEmbedField.getOrDefault("true"));
    }
    return "true".equals(taskEmbedField.getValue());
  }

  public static boolean canResume(String taskId) {
    if (taskId == null) {
      return false;
    }
    var session = ISession.current();
    if (session == null) {
      return false;
    }
    var user = session.getSessionUser();
    if (user == null) {
      return false;
    }
    var task = IWorkflowContext.current().findTask(taskId);
    if (task == null) {
      return false;
    }
    if (PermissionsUtil.isAdmin()) {
      return true;
    }
    var isResponsible = task.responsibles().all().stream().anyMatch(r -> r.get().isMember(user.getUserToken(), false));
    if (task.getWorkerUser() == null) {
      return isResponsible;
    }
    var isWorker = task.getWorkerUser().isMember(user.getUserToken(), false);
    return isResponsible || isWorker;
  }
}
