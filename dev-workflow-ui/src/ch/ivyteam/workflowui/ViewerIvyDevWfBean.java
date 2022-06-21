package ch.ivyteam.workflowui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.workflowui.starts.StartableModel;
import ch.ivyteam.workflowui.util.ViewerUtil;

@ViewScoped
@ManagedBean
public class ViewerIvyDevWfBean {
  private StartableModel selectedStartable;

  public String getViewerDialogTitle() {
    return ViewerUtil.getViewerDialogTitle(selectedStartable);
  }

  public void setStartable(StartableModel startable) {
    this.selectedStartable = startable;
  }

  public String getViewerLink() {
    if (selectedStartable == null) {
      return "";
    }
    return selectedStartable.getViewerLink().getRelativeEncoded().replace("$", "%24");
  }
}
