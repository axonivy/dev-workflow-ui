package ch.ivyteam.workflowui.util;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.ivyteam.ivy.application.IProcessModel;
import ch.ivyteam.ivy.application.app.IApplicationRepository;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.starts.CaseMapStartableModel;
import ch.ivyteam.workflowui.starts.StartableModel;

public class ProcessModelsUtil {

  private static List<IProcessModel> getProcessModels() {
    var securityContext = ISecurityContext.current();
    return IApplicationRepository.instance().allOf(securityContext).stream()
      .flatMap(a -> a.getProcessModels().stream())
      .collect(Collectors.toList());
  }

  public static List<IWorkflowProcessModelVersion> getWorkflowPMVs() {
    return getProcessModels().stream()
      .flatMap(pm -> pm.getProcessModelVersions().stream())
      .map(IWorkflowProcessModelVersion::of)
      .collect(Collectors.toList());
  }

  public static Stream<IWorkflowProcessModelVersion> getReleasedWorkflowPMVs() {
    return getProcessModels().stream()
      .map(IProcessModel::getReleasedProcessModelVersion)
      .filter(Objects::nonNull)
      .map(IWorkflowProcessModelVersion::of);
  }

  public static List<StartableModel> getDeployedStartables() {
    return getReleasedWorkflowPMVs()
      .flatMap(pmv -> pmv.getStartables(ISession.current()).stream())
      .map(s -> createCaseMapOrProcessStartable(s))
      .sorted(Comparator.comparing((StartableModel s) -> s.getCategory().getName(),
          Comparator.nullsLast(Comparator.comparing(String::isEmpty))
              .thenComparing(String::compareTo))
          .thenComparing(StartableModel::getDisplayName))
      .collect(Collectors.toList());
  }

  private static StartableModel createCaseMapOrProcessStartable(IWebStartable startable) {
    if (startable.getType().equals("casemap")) {
      return new CaseMapStartableModel(startable);
    }
    return new StartableModel(startable);
  }
}
