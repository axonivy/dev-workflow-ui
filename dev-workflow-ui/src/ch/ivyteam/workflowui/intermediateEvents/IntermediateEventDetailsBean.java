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
      case CANCELED -> "ti ti-alert-octagon";
      case PENDING, WAITING -> "ti ti-hourglass-low";
      case PROCESSED -> "ti ti-check";
      case TIMEOUTED -> "ti ti-bell";
      case TIMEOUTED_AND_PROCESSED -> "ti ti-bell-timer";
      default -> "ti ti-help-circle";
    };
  }
}
