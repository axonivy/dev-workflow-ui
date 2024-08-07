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
  private List<WebServiceProcess> filteredWebServices;
  private String filter;

  public WebServicesBean() {
    webServices = ProcessModelsUtil.getReleasedWorkflowPMVs().
            flatMap(pmv -> pmv.getWebServiceProcesses().stream())
            .map(WebServiceProcess::new)
            .collect(Collectors.toList());
  }

  public List<WebServiceProcess> getWebServices() {
    if (filter != null && !filter.isEmpty()) {
      return filteredWebServices;
    }
    return webServices;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
    filteredWebServices = webServices.stream()
            .filter(ws -> ws.getName().toLowerCase().contains(filter)
                    || ws.getProcessName().toLowerCase().contains(filter))
            .collect(Collectors.toList());
  }

  public void executeRow(SelectEvent<WebServiceProcess> event) {
    event.getObject().execute();
  }
}
