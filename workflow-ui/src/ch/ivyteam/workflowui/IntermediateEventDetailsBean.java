package ch.ivyteam.workflowui;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.workflow.IIntermediateEvent;

@ManagedBean
public class IntermediateEventDetailsBean
{
  private String processId;
  private CustomIntermediateEventModel selectedIntermediateElement;

  public String getProcessId()
  {
    return processId;
  }

  public void setProcessId(String processId)
  {
    this.processId = processId;
    this.selectedIntermediateElement = CustomIntermediateEventModel.byProcessElementId(processId);
  }

  public String getSelectedIntermediateElementId()
  {
    return selectedIntermediateElement.getProcessElementId();
  }

  public CustomIntermediateEventModel getSelectedIntermediateElement()
  {
    return selectedIntermediateElement;
  }

  public String getEventStateIconCss(IIntermediateEvent event)
  {
    switch (event.getState())
    {
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
