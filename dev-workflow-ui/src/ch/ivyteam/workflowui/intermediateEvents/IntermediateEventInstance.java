package ch.ivyteam.workflowui.intermediateEvents;

import java.util.Date;

import ch.ivyteam.ivy.workflow.IIntermediateEvent;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IntermediateEventState;
import ch.ivyteam.ivy.workflow.IntermediateEventTimeoutAction;

public class IntermediateEventInstance {

  private long id;
  private IntermediateEventState state;
  private ITask task;
  private String eventIdentifier;
  private Date eventTimestamp;
  private Date timeoutTimestamp;
  private IntermediateEventTimeoutAction timeoutAction;
  private Object resultObject;

  public IntermediateEventInstance() {}

  IntermediateEventInstance(IIntermediateEvent intermediateEvent) {
    this.id = intermediateEvent.getId();
    this.state = intermediateEvent.getState();
    this.task = intermediateEvent.getTask();
    this.eventIdentifier = intermediateEvent.getEventIdentifier();
    this.eventTimestamp = intermediateEvent.getEventTimestamp();
    this.timeoutTimestamp = intermediateEvent.getTimeoutTimestamp();
    this.timeoutAction = intermediateEvent.getTimeoutAction();
    this.resultObject = intermediateEvent.getResultObject();
  }

  public long getId() {
    return id;
  }

  public IntermediateEventState getState() {
    return state;
  }

  public ITask getTask() {
    return task;
  }

  public String getEventIdentifier() {
    return eventIdentifier;
  }

  public Date getEventTimestamp() {
    return eventTimestamp;
  }

  public Date getTimeoutTimestamp() {
    return timeoutTimestamp;
  }

  public IntermediateEventTimeoutAction getTimeoutAction() {
    return timeoutAction;
  }

  public Object getResultObject() {
    return resultObject;
  }
}
