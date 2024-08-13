package ch.ivyteam.workflowui.state;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.workflowui.tasks.TaskModel;

public class StateBadgeModel {

  private final String name;
  private final String cssClass;
  private final String tooltip;

  public StateBadgeModel(TaskModel task) {
    this(task.getBusinessState().toString(), task.getState().toString());
  }

  public StateBadgeModel(ITask task) {
    this(task.getBusinessState().toString(), task.getState().toString());
  }

  public StateBadgeModel(ICase caze) {
    this(caze.getBusinessState().toString(), caze.getState().toString());
  }

  private StateBadgeModel(String businessState, String state) {
    this.name = businessState.toLowerCase().replace("_", " ");
    this.cssClass = "state-" + businessState.toLowerCase();
    this.tooltip = "Technical state: " + state;
  }

  public String getName() {
    return name;
  }

  public String getCssClass() {
    return cssClass;
  }

  public String getTooltip() {
    return tooltip;
  }
}
