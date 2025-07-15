package ch.ivyteam.workflowui.signals;

import ch.ivyteam.ivy.process.model.value.SignalCode;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.signal.ITaskBoundarySignalEventReceiver;

public class BoundarySignalReceiverModel {

  private SignalCode signalPattern;
  private ITask waitingTask;

  public BoundarySignalReceiverModel() {}

  BoundarySignalReceiverModel(ITaskBoundarySignalEventReceiver receiver) {
    this.signalPattern = receiver.getSignalPattern();
    this.waitingTask = receiver.getWaitingTask();
  }

  public SignalCode getSignalPattern() {
    return signalPattern;
  }

  public ITask getWaitingTask() {
    return waitingTask;
  }

}
