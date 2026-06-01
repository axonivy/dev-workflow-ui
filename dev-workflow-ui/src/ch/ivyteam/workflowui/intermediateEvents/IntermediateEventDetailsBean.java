package ch.ivyteam.workflowui.intermediateEvents;

import jakarta.inject.Named;

import java.io.Serializable;

import jakarta.faces.view.ViewScoped;

@Named
@ViewScoped
public class IntermediateEventDetailsBean implements Serializable {

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
