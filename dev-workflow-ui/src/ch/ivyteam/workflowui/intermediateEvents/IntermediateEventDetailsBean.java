package ch.ivyteam.workflowui.intermediateEvents;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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

  public String getEventStateIconCss(IntermediateEventInstance event) {
    return switch (event.getState()) {
      case CANCELED -> "si si-mood-warning";
      case PENDING, WAITING -> "si si-hourglass";
      case PROCESSED -> "si si-check-1";
      case TIMEOUTED -> "si si-alarm-bell";
      case TIMEOUTED_AND_PROCESSED -> "si si-alarm-bell-timer";
      default -> "si si-question-circle";
    };
  }
}
