package ch.ivyteam.workflowui.webservices;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.workflowui.util.ProcessModelsUtil;

@Named
@ViewScoped
public class WebServicesBean implements Serializable {

  private List<WebServiceProcess> webServices;
  private String filter;

  public WebServicesBean() {
    webServices = setServices();
  }

  @SuppressWarnings("deprecation")
  private List<WebServiceProcess> setServices() {
    return ProcessModelsUtil.getReleasedWorkflowPMVs()
        .flatMap(pmv -> pmv.getWebServiceProcesses().stream())
        .map(WebServiceProcess::new)
        .filter(ws -> filter == null || (ws.getName().toLowerCase().contains(filter) || ws.getProcessName().toLowerCase().contains(filter)))
        .collect(Collectors.toList());
  }

  public List<WebServiceProcess> getWebServices() {
    return webServices;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
    this.webServices = setServices();
  }

  public void executeRow(SelectEvent<WebServiceProcess> event) {
    event.getObject().execute();
  }
}
