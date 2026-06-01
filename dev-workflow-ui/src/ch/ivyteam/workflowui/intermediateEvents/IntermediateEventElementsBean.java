package ch.ivyteam.workflowui.intermediateEvents;

import java.io.Serializable;
import java.util.List;

import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.workflowui.util.PermissionsUtil;

@Named
@ViewScoped
public class IntermediateEventElementsBean implements Serializable {

  private final List<IntermediateEventElementModel> intermediateEventElements;

  public IntermediateEventElementsBean() {
    intermediateEventElements = IntermediateEventElementModel.create();
  }

  public void onRowSelect(SelectEvent<?> event) {
    var object = event.getObject();
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

  public List<IntermediateEventElementModel> getIntermediateEventElements() {
    return intermediateEventElements;
  }
}
