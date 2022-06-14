package ch.ivyteam.workflowui.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.application.IProcessModel;
import ch.ivyteam.ivy.application.app.IApplicationRepository;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.starts.CaseMapStartableModel;
import ch.ivyteam.workflowui.starts.StartableModel;

public class ProcessModelsUtil {

  private static List<IProcessModel> getProcessModels() {
    var app = Optional.ofNullable(IApplication.current());
    return app.stream().map(IApplication::getSecurityContext)
      .flatMap(security -> IApplicationRepository.instance().allOf(security).stream())
      .flatMap(a -> a.getProcessModels().stream())
      .collect(Collectors.toList());
  }

  public static List<IWorkflowProcessModelVersion> getWorkflowPMVs() {
    return getProcessModels().stream()
      .flatMap(pm -> pm.getProcessModelVersions().stream())
      .map(IWorkflowProcessModelVersion::of)
      .collect(Collectors.toList());
  }

  public static List<IWorkflowProcessModelVersion> getReleasedWorkflowPMVs() {
    return getProcessModels().stream()
      .map(IProcessModel::getReleasedProcessModelVersion)
      .map(IWorkflowProcessModelVersion::of)
      .collect(Collectors.toList());
  }

  public static List<StartableModel> getDeployedStartables() {
    return getReleasedWorkflowPMVs().stream()
      .flatMap(pmv -> pmv.getStartables(ISession.current()).stream()
      .map(s -> createCaseMapOrProcessStartable(s)))
      .collect(Collectors.toList());
  }

  private static StartableModel createCaseMapOrProcessStartable(IWebStartable startable) {
    if (startable.getType().equals("casemap")) {
      return new CaseMapStartableModel(startable);
    }
    return new StartableModel(startable);
  }
}
