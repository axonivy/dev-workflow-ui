package ch.ivyteam.workflowui.intermediateEvents;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.ivy.workflow.IIntermediateEvent;

@ManagedBean
@ViewScoped
public class IntermediateEventDetailsBean {
  private String processId;
  private IntermediateEventElementModel selectedIntermediateElement;

  public String getProcessId() {
    return processId;
  }

  public void setProcessId(String processId) {
    this.processId = processId;
    this.selectedIntermediateElement = IntermediateEventElementModel.byProcessElementId(processId);
  }

  public String getSelectedIntermediateElementId() {
    return selectedIntermediateElement.getProcessElementId();
  }

  public IntermediateEventElementModel getSelectedIntermediateElement() {
    return selectedIntermediateElement;
  }

  public String getEventStateIconCss(IIntermediateEvent event) {
    switch (event.getState()) {
      case CANCELED:
        return "si si-mood-warning";
      case PENDING:
      case WAITING:
        return "si si-hourglass";
      case PROCESSED:
        return "si si-check-1";
      case TIMEOUTED:
        return "si si-alarm-bell";
      case TIMEOUTED_AND_PROCESSED:
        return "si si-alarm-bell-timer";
      default:
        return "si si-question-circle";
    }
  }
}
