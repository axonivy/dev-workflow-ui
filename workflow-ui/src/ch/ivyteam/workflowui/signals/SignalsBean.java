package ch.ivyteam.workflowui.signals;

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
import ch.ivyteam.ivy.workflow.signal.IStartSignalEventElement;

@ManagedBean
@ViewScoped
public class SignalsBean {
  private String code;
  private String payload;
  private SignalsModel signalsModel;
  private BoundarySignalModel boundarySignalModel;

  public SignalsBean() {
    signalsModel = new SignalsModel();
    boundarySignalModel = new BoundarySignalModel();
  }

  public SignalsModel getSignalsModel() {
    return signalsModel;
  }

  public BoundarySignalModel getBoundarySignalModel() {
    return boundarySignalModel;
  }

  public void sendBoundarySignal(String signalCode) {
    this.code = signalCode;
    sendSignal();
  }

  public void sendSignal() {
    IBpmSignalService signals = IWorkflowSession.current().getWorkflowContext().signals();
    SignalCode signalCode = new SignalCode(code);

    if (StringUtils.isBlank(payload)) {
      signals.send(signalCode);
    } else {
      signals.send(signalCode, payload);
    }
    showMessage("Signal " + code + " sent");
    this.code = "";
  }

  private void showMessage(String msg) {
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg));
  }

  public List<String> signalsComplete(String query) {
    return IWorkflowContext.current().signals().receivers().all().stream()
            .map(IStartSignalEventElement::getName).filter(s -> s.toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }
}
