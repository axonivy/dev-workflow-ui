package ch.ivyteam.workflowui.signals;

import java.util.Date;
import java.util.List;

import ch.ivyteam.ivy.process.model.value.SignalCode;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.signal.ISignalEvent;

public class SignalEventModel {

  private SignalCode signalCode;
  private Date sentTimestamp;
  private IUser sentByUser;
  private List<ITask> createdTasks;
  private List<ITask> destroyedTasks;

  public SignalEventModel() {}

  SignalEventModel(ISignalEvent signal) {
    this.signalCode = signal.getSignalCode();
    this.sentTimestamp = signal.getSentTimestamp();
    this.sentByUser = signal.getSentByUser();
    this.createdTasks = signal.getCreatedTasks();
    this.destroyedTasks = signal.getDestroyedTasks();
  }

  public SignalCode getSignalCode() {
    return signalCode;
  }

  public Date getSentTimestamp() {
    return sentTimestamp;
  }

  public IUser getSentByUser() {
    return sentByUser;
  }

  public List<ITask> getCreatedTasks() {
    return createdTasks;
  }

  public List<ITask> getDestroyedTasks() {
    return destroyedTasks;
  }

}
