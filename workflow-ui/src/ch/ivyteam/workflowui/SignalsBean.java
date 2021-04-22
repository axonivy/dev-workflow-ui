package ch.ivyteam.workflowui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.process.model.value.SignalCode;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.IWorkflowSession;
import ch.ivyteam.ivy.workflow.signal.IBpmSignalService;
import ch.ivyteam.ivy.workflow.signal.ISignalEvent;
import ch.ivyteam.ivy.workflow.signal.IStartSignalEventElement;

@ManagedBean
@ViewScoped
public class SignalsBean
{
  private String code;
  private String payload;

  public void sendSignal()
  {
    IBpmSignalService signals = IWorkflowSession.current().getWorkflowContext().signals();
    SignalCode signalCode = new SignalCode(code);

    if (StringUtils.isBlank(payload))
    {
      signals.send(signalCode);
    }
    else
    {
      signals.send(signalCode, payload);
    }
    showMessage("Signal " + code + " sent");
  }

  private void showMessage(String msg)
  {
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg));
  }

  public List<ISignalEvent> getFiredSignals()
  {
    return new ArrayList<>(IWorkflowContext.current().signals().history().createSignalEventQuery().orderBy()
        .sentTimestamp().descending().executor().results());
  }

  public List<String> signalsComplete(String query)
  {
    return IWorkflowContext.current().signals().receivers().all().stream()
        .map(IStartSignalEventElement::getName)
        .filter(s -> s.toLowerCase().contains(query.toLowerCase()))
        .collect(Collectors.toList());
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public String getPayload()
  {
    return payload;
  }

  public void setPayload(String payload)
  {
    this.payload = payload;
  }
}
