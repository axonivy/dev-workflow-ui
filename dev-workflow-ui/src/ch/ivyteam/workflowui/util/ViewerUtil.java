package ch.ivyteam.workflowui.util;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.starts.CaseMapStartableModel;
import ch.ivyteam.workflowui.starts.StartableModel;

public class ViewerUtil {

  public static String getViewerLink(ICase caze) {
    return caseToStartable(caze).getViewerLink().toString();
  }

  public static String getViewerDialogTitle(ICase caze) {
    return getViewerDialogTitle(caseToStartable(caze));
  }

  public static String getViewerDialogTitle(StartableModel startable) {
    if (startable == null) {
      return "";
    }
    if (startable.isProcessStart()) {
      return "Process of: " + startable.getDisplayName();
    }
    return "Case Map for: " + startable.getDisplayName();
  }

  private static StartableModel caseToStartable(ICase caze) {
    var pmv = caze.getProcessModelVersion();
    IWebStartable startable = caze.getBusinessCase().getStartedFrom();
    return startable.getType().equals("casemap") ? new CaseMapStartableModel(startable, pmv) : new StartableModel(startable);
  }
}
