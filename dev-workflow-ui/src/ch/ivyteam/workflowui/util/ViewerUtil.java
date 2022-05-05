package ch.ivyteam.workflowui.util;

import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.starts.StartableModel;

public class ViewerUtil {

  public static String getViewerLink(IWebStartable startable) {
    return getStartableModelViewerLink(new StartableModel(startable));
  }

  public static String getStartableModelViewerLink(StartableModel startable) {
    if (startable == null) {
      return "";
    }
    return startable.getViewerLink().toString();
  }

  public static String getViewerDialogTitle(IWebStartable startable) {
    return getViewerDialogTitle(new StartableModel(startable));
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
}
