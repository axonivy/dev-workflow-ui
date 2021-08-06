package ch.ivyteam.workflowui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ch.ivyteam.ivy.bpm.error.BpmError;

@ManagedBean
@SessionScoped
public class FrameBean {
  public String getTaskUrl() {
    String taskUrl = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
            .get("taskUrl");
    if (taskUrl.startsWith("/")) {
      return taskUrl;
    } else {
      BpmError.create("frame:unsupported:url")
              .withMessage("Only relative urls are supported (security reasons)").throwError();
      return null;
    }
  }
}
