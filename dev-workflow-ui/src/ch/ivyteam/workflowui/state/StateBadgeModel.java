package ch.ivyteam.workflowui.state;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.workflowui.tasks.TaskModel;

public class StateBadgeModel {

  private final String nameCmsPath;
  private final String cssClass;
  private final String tooltipCmsPath;

  public StateBadgeModel(TaskModel task) {
    this("/enums/taskBusinessState/" + task.getBusinessState().toString().toLowerCase(), task.getBusinessState().toString(), "/enums/taskState/" + task.getState().toString().toLowerCase());
  }

  public StateBadgeModel(TaskState taskState) {
    this("/enums/taskState/" + taskState.toString().toLowerCase(), taskState.getBusinessState().toString(), "/enums/taskState/" + taskState.toString().toLowerCase());
  }

  public StateBadgeModel(ITask task) {
    this("/enums/taskBusinessState/" + task.getBusinessState().toString().toLowerCase(), task.getBusinessState().toString(), "/enums/taskState/" + task.getState().toString().toLowerCase());
  }

  public StateBadgeModel(ICase caze) {
    this("/enums/caseBusinessState/" + caze.getBusinessState().toString().toLowerCase(), caze.getBusinessState().toString(), "/enums/caseState/" + caze.getState().toString().toLowerCase());
  }

  private StateBadgeModel(String nameCmsPath, String businessState, String tooltipCmsPath) {
    this.nameCmsPath = nameCmsPath;
    this.cssClass = "state-" + businessState.toLowerCase();
    this.tooltipCmsPath = tooltipCmsPath;
  }

  public String getNameCmsPath() {
    return nameCmsPath;
  }

  public String getCssClass() {
    return cssClass;
  }

  public String getTooltipCmsPath() {
    return tooltipCmsPath;
  }
}
