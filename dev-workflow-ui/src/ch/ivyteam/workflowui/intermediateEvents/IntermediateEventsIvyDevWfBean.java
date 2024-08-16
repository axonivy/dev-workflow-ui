package ch.ivyteam.workflowui.intermediateEvents;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.workflowui.util.PermissionsUtil;

@ManagedBean
@ViewScoped
public class IntermediateEventsIvyDevWfBean {
  private List<IntermediateEventElementModel> intermediateEvents;

  public IntermediateEventsIvyDevWfBean() {
    intermediateEvents = IntermediateEventElementModel.create();
  }

  public void onRowSelect(SelectEvent<?> event) {
    Object object = event.getObject();
    if (PermissionsUtil.isAdmin()) {
      if (object instanceof IntermediateEventElementModel model) {
        model.redirectToElement();
      }
    } else {
      FacesContext.getCurrentInstance().addMessage(null,
              new FacesMessage(FacesMessage.SEVERITY_INFO, "Access denied",
                      "You need to be an admin user to access this function"));
    }
  }

  public List<IntermediateEventElementModel> getIntermediateEvents() {
    return intermediateEvents;
  }
}
