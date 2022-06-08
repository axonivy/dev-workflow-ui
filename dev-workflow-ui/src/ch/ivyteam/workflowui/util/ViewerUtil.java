package ch.ivyteam.workflowui.util;

import ch.ivyteam.ivy.casemap.runtime.ICaseMapService;
import ch.ivyteam.ivy.casemap.runtime.start.CaseMapViewerUrl;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.businesscase.IBusinessCase;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.ivy.workflow.start.ProcessViewerUrl;
import ch.ivyteam.workflowui.starts.CaseMapStartableModel;
import ch.ivyteam.workflowui.starts.StartableModel;

public class ViewerUtil {

  public static String getViewerLink(ICase caze) {
    var businessCase = caze.getBusinessCase();
    if(hasCaseMap(businessCase)) {
      return CaseMapViewerUrl.of(businessCase).toWebLink().toString();
    }
    return ProcessViewerUrl.of(caze).toWebLink().toString();
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
    return "Case Map Viewer";
  }

  private static StartableModel caseToStartable(ICase caze) {
    var businessCase = caze.getBusinessCase();
    IWebStartable startable = businessCase.getStartedFrom();
    return hasCaseMap(businessCase) ? new CaseMapStartableModel(startable) : new StartableModel(startable);
  }

  private static boolean hasCaseMap(IBusinessCase businessCase) {
    return ICaseMapService.current().find().byBusinessCase(businessCase) != null;
  }
}
