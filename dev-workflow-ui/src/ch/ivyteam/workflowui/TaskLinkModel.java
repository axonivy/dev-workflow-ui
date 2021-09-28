package ch.ivyteam.workflowui;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.workflowui.util.RedirectUtil;

public class TaskLinkModel {
  private final String name;
  private final long id;
  private final TaskState state;

  public TaskLinkModel(ITask task) {
    this.name = StringUtils.isBlank(task.getName()) ? "[Task: " + task.getId() + "]" : task.getName();
    this.id = task.getId();
    this.state = task.getState();
  }

  public String getName() {
    return name;
  }

  public long getId() {
    return id;
  }

  public TaskState getState() {
    return state;
  }

  public void redirectToTask() {
    RedirectUtil.redirect("taskDetails.xhtml?task=" + this.id);
  }

  @SuppressWarnings("removal")
  public String getStateIcon() {
    switch (state) {
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

}
