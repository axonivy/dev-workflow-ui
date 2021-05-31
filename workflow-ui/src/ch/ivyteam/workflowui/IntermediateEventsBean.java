package ch.ivyteam.workflowui;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.workflowui.util.UserUtil;

@ManagedBean
public class IntermediateEventsBean
{

  public List<CustomIntermediateEventModel> getIntermediateEvents()
  {
    return CustomIntermediateEventModel.create();
  }

  public void onRowSelect(SelectEvent event)
  {
    Object object = event.getObject();
    if (UserUtil.isAdmin())
    {
      if (object instanceof CustomIntermediateEventModel)
      {
        ((CustomIntermediateEventModel) object).redirectToElement();
      }
    }
    else
    {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Access denied",
          "You need to be an admin user to access this function"));
    }
  }
}
