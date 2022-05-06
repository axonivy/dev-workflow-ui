package ch.ivyteam.workflowui.util;

import java.util.List;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.application.IApplicationRepository;
import ch.ivyteam.ivy.application.IProcessModel;
import ch.ivyteam.ivy.application.IProcessModelVersion;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.starts.CaseMapStartableModel;
import ch.ivyteam.workflowui.starts.StartableModel;

public class ProcessModelsUtil {

  public static List<IProcessModel> getProcessModels() {
    var apps = IApplicationRepository.instance().allOf(IApplication.current().getSecurityContext());
    return apps.stream().flatMap(app -> app.getProcessModels().stream()).collect(Collectors.toList());
  }

  public static List<IProcessModelVersion> getPMVs() {
    return getProcessModels().stream().flatMap(pm -> pm.getProcessModelVersions().stream())
            .collect(Collectors.toList());
  }

  public static List<IWorkflowProcessModelVersion> getWorkflowPMVs() {
    return getPMVs().stream().map(IWorkflowProcessModelVersion::of).collect(Collectors.toList());
  }

  public static List<IWorkflowProcessModelVersion> getReleasedWorkflowPMVs() {
    return getProcessModels().stream().map(IProcessModel::getReleasedProcessModelVersion)
            .map(IWorkflowProcessModelVersion::of).collect(Collectors.toList());
  }

  public static List<StartableModel> getStartables() {
    return getWorkflowPMVs().stream().flatMap(pmv -> pmv.getStartables(ISession.current()).stream()
            .map(s -> createCaseMapOrProcessStartable(s, pmv)))
            .collect(Collectors.toList());
  }

  private static StartableModel createCaseMapOrProcessStartable(IWebStartable startable, IProcessModelVersion pmv) {
    if (startable.getType().equals("casemap")) {
      return new CaseMapStartableModel(startable, pmv);
    }
    return new StartableModel(startable);
  }
}
