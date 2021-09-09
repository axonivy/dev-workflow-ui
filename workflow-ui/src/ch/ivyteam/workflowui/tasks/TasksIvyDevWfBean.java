package ch.ivyteam.workflowui.tasks;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.ivy.workflow.WorkflowPriority;
import ch.ivyteam.workflowui.TaskLinkModel;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.UrlUtil;

@ManagedBean
@ViewScoped
public class TasksIvyDevWfBean {
  private TasksDataModel tasksDataModel;

  public TasksIvyDevWfBean() {
    tasksDataModel = new TasksDataModel();
    tasksDataModel.setFilter("");
  }

  public TasksDataModel getTasksDataModel() {
    return tasksDataModel;
  }

  @SuppressWarnings("removal")
  public String getStateIcon(TaskState taskState) {
    switch (taskState) {
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
      case UNASSIGNED:
      default:
        return "synchronize-arrows task-state-system";
    }
  }

  public void executeTaskRow(SelectEvent event) {
    Object object = event.getObject();
    if (object instanceof ITask) {
      executeTask(((ITask) object).getId());
    }
  }

  public void displayTaskRow(SelectEvent event) {
    Object object = event.getObject();
    if (object instanceof ITask) {
      redirectToTaskDetails(((ITask) object).getId());
    }
  }

  public void redirectToTaskDetails(long taskId) {
    RedirectUtil.redirect("taskDetails.xhtml?task=" + taskId);
  }

  public void executeTask(long taskId) {
    ITask task = IWorkflowContext.current().findTask(taskId);
    RedirectUtil.redirect(UrlUtil.generateStartFrameUrl(task.getStartLink()));
  }

  public String getPriorityIcon(WorkflowPriority priority) {
    switch (priority) {
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

  public TaskLinkModel toTaskLinkModel(ITask task) {
    return new TaskLinkModel(task);
  }
}
