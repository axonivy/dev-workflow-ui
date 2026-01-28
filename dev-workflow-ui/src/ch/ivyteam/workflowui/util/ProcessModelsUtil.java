package ch.ivyteam.workflowui.util;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.application.ReleaseState;
import ch.ivyteam.ivy.application.app.IApplicationRepository;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.starts.CaseMapStartableModel;
import ch.ivyteam.workflowui.starts.StartableModel;

public class ProcessModelsUtil {

  public static List<IWorkflowProcessModelVersion> getWorkflowPMVs() {
    var securityContext = ISecurityContext.current();
    return IApplicationRepository.of(securityContext).all().stream()
        .flatMap(IApplication::getProcessModelVersions)
        .map(IWorkflowProcessModelVersion::of)
        .collect(Collectors.toList());
  }

  public static Stream<IWorkflowProcessModelVersion> getReleasedWorkflowPMVs() {
    var securityContext = ISecurityContext.current();
    return IApplicationRepository.of(securityContext).all().stream()
        .filter(app -> app.getReleaseState() == ReleaseState.RELEASED)
        .flatMap(IApplication::getProcessModelVersions)
        .map(IWorkflowProcessModelVersion::of);
  }

  public static List<StartableModel> getDeployedStartables() {
    return getReleasedWorkflowPMVs()
        .flatMap(pmv -> pmv.getStartables(ISession.current()).stream())
        .map(ProcessModelsUtil::createCaseMapOrProcessStartable)
        .sorted(Comparator.comparing((StartableModel s) -> s.getProjectName())
            .thenComparing(s -> s.getCategory().getName(),
                Comparator.nullsLast(Comparator.comparing(String::isEmpty))
                    .thenComparing(String::compareTo))
            .thenComparing(StartableModel::getDisplayName))
        .collect(Collectors.toList());
  }

  private static StartableModel createCaseMapOrProcessStartable(IWebStartable startable) {
    if ("casemap".equals(startable.getType())) {
      return new CaseMapStartableModel(startable);
    }
    return new StartableModel(startable);
  }
}
