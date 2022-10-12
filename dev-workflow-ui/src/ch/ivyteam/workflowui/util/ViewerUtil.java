package ch.ivyteam.workflowui.util;

import java.util.Optional;

import javax.faces.context.FacesContext;

import ch.ivyteam.ivy.casemap.runtime.ICaseMapService;
import ch.ivyteam.ivy.casemap.viewer.api.CaseMapViewer;
import ch.ivyteam.ivy.jsf.primefaces.theme.IvyFreyaTheme;
import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.process.viewer.api.ProcessViewer;
import ch.ivyteam.ivy.process.viewer.api.ProcessViewerUrlBuilder.ThemeMode;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.businesscase.IBusinessCase;
import ch.ivyteam.ivy.workflow.start.ICaseMapWebStartable;
import ch.ivyteam.ivy.workflow.start.IProcessWebStartable;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.starts.CaseMapStartableModel;
import ch.ivyteam.workflowui.starts.StartableModel;

public class ViewerUtil {

  public static String getViewerLink(ICase caze) {
    var businessCase = caze.getBusinessCase();
    if (hasCaseMap(businessCase)) {
      return CaseMapViewer.of(businessCase).url().toWebLink().get();
    }
    return ProcessViewer.of(caze).url().toWebLink().get();
  }

  public static boolean isViewerAllowed(ICase caze) {
    var businessCase = caze.getBusinessCase();
    if (hasCaseMap(businessCase)) {
      return true;
    }
    return ProcessViewer.of(caze).isViewAllowed();
  }

  /**
   * @throws IllegalArgumentException
   */
  public static WebLink getViewerLink(IWebStartable startable) {
    if (startable instanceof IProcessWebStartable processStartable) {
      return ProcessViewer.of(processStartable).url().themeMode(readThemeMode()).toWebLink();
    }
    if (startable instanceof ICaseMapWebStartable caseMapStartable) {
      return CaseMapViewer.of(caseMapStartable).url().toWebLink();
    }
    throw new IllegalArgumentException("The provided IWebStartable does not support the generation of a viewer link.");
  }

  private static ThemeMode readThemeMode() {
    var context = FacesContext.getCurrentInstance();
    if (context == null) {
      return ThemeMode.LIGHT;
    }
    return Optional.ofNullable(context.getApplication().evaluateExpressionGet(context, "#{ivyFreyaTheme}", IvyFreyaTheme.class))
            .map(IvyFreyaTheme::getMode)
            .map(theme -> ThemeMode.of(theme))
            .orElse(ThemeMode.LIGHT);
  }

  public static boolean isViewerAllowed(IWebStartable startable) {
    if (startable instanceof IProcessWebStartable processStartable) {
      return ProcessViewer.of(processStartable).isViewAllowed();
    }
    return UserUtil.isLoggedIn();
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
    if (startable == null) {
      return null;
    }
    return hasCaseMap(businessCase) ? new CaseMapStartableModel(startable) : new StartableModel(startable);
  }

  private static boolean hasCaseMap(IBusinessCase businessCase) {
    return ICaseMapService.current().find().byBusinessCase(businessCase) != null;
  }

}
