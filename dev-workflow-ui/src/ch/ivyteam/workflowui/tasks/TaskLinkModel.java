package ch.ivyteam.workflowui.tasks;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.TaskUtil;

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

  public String getStateIcon() {
    return TaskUtil.getStateIcon(state);
  }
}
