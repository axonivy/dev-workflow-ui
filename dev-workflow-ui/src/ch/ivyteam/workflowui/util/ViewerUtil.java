package ch.ivyteam.workflowui.util;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.starts.CaseMapStartableModel;
import ch.ivyteam.workflowui.starts.StartableModel;

public class ViewerUtil {

  public static String getViewerLink(ICase caze) {
    return getStartableModelViewerLink(caseToStartable(caze));
  }

  public static String getStartableModelViewerLink(StartableModel startable) {
    return startable.getViewerLink().toString();
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
    String pmvVersionName = caze.getProcessModelVersion().getVersionName();
    IWebStartable startable = caze.getBusinessCase().getStartedFrom();
    return startable.getType().equals("casemap") ? new CaseMapStartableModel(startable, pmvVersionName) : new StartableModel(startable);
  }
}
