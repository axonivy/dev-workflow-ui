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
    else if (startable.isProcessStart()) {
      return startable.getViewerLink().toString();
    }
    return startable.getCaseMapLink();
  }
}
