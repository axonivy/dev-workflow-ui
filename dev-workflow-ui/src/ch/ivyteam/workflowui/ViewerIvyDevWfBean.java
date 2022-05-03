package ch.ivyteam.workflowui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.workflowui.starts.StartableModel;
import ch.ivyteam.workflowui.util.ViewerUtil;

@ViewScoped
@ManagedBean
public class ViewerIvyDevWfBean {
  private StartableModel selectedStartable;

  public String getViewerDialogTitle() {
    if (selectedStartable == null) {
      return "";
    }
    else if (selectedStartable.isProcessStart()) {
      return "Process Viewer for: " + selectedStartable.getDisplayName();
    }
    return "CaseMap for: " + selectedStartable.getDisplayName();
  }

  public void setStartable(StartableModel startable) {
    this.selectedStartable = startable;
  }

  public String getViewerLink() {
    if (selectedStartable == null) {
      return "";
    }
    return ViewerUtil.getStartableModelViewerLink(selectedStartable);
  }

  public String getViewerLink(ICase caze) {
    return ViewerUtil.getViewerLink(caze.getBusinessCase().getStartedFrom());
  }
}
