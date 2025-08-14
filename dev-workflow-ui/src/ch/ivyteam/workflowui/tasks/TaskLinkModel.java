package ch.ivyteam.workflowui.tasks;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.ivy.workflow.task.TaskBusinessState;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.url.Page;

public class TaskLinkModel {

  private final String uuid;
  private final String name;
  private final TaskBusinessState businessState;
  private final TaskState state;

  public TaskLinkModel(ITask task) {
    this.name = StringUtils.isBlank(task.getName()) ? "[Task: " + task.uuid() + "]" : task.getName();
    this.uuid = task.uuid();
    this.businessState = task.getBusinessState();
    this.state = task.getState();
  }

  public String getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public TaskBusinessState getBusinessState() {
    return businessState;
  }

  public TaskState getState() {
    return state;
  }

  public void redirectToTask() {
    RedirectUtil.redirect(Page.TASK, Map.of("id", uuid));
  }
}
