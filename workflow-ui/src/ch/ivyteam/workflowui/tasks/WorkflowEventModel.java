package ch.ivyteam.workflowui.tasks;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.IWorkflowEvent;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.ivy.workflow.WorkflowEventKind;

public class WorkflowEventModel {

  private final Date eventTimestamp;
  private final WorkflowEventKind eventType;
  private final TaskState taskState;
  private final IUser author;
  private final String additionalInfo;

  public WorkflowEventModel(IWorkflowEvent workflowEvent) {
    this.eventTimestamp = workflowEvent.getEventTimestamp();
    this.eventType = workflowEvent.getEventKind();
    this.taskState = workflowEvent.getTaskState();
    this.author = workflowEvent.getUser();
    this.additionalInfo = workflowEvent.getAdditionalInfo().stream().collect(Collectors.joining(", "));
  }

  public static List<WorkflowEventModel> toList(List<IWorkflowEvent> workflowEvents) {
    return workflowEvents.stream().map(WorkflowEventModel::new).collect(Collectors.toList());
  }

  public Date getEventTimestamp() {
    return eventTimestamp;
  }

  public WorkflowEventKind getEventType() {
    return eventType;
  }

  public TaskState getTaskState() {
    return taskState;
  }

  public IUser getAuthor() {
    return author;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

}
