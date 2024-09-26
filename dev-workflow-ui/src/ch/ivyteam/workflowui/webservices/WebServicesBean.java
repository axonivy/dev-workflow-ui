package ch.ivyteam.workflowui.webservices;

import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.workflowui.util.ProcessModelsUtil;

@ManagedBean
@ViewScoped
public class WebServicesBean {

  private List<WebServiceProcess> webServices;
  private String filter;

  @SuppressWarnings("removal")
  public WebServicesBean() {
    webServices = ProcessModelsUtil.getReleasedWorkflowPMVs().
            flatMap(pmv -> pmv.getWebServiceProcesses().stream())
            .map(WebServiceProcess::new)
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
  }

  public void executeRow(SelectEvent<WebServiceProcess> event) {
    event.getObject().execute();
  }
}
