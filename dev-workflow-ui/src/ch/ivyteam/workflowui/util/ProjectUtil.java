package ch.ivyteam.workflowui.util;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.ivyteam.ivy.application.app.Application;
import ch.ivyteam.ivy.application.app.ApplicationRepository;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.starts.CaseMapStartableModel;
import ch.ivyteam.workflowui.starts.StartableModel;

public class ProjectUtil {

  public static Stream<IWorkflowProcessModelVersion> getActiveWorkflowPMVs() {
    return workflowPMVs(app -> app.state().active());
  }

  public static Stream<IWorkflowProcessModelVersion> getRunningWorkflowPMVs() {
    return workflowPMVs(app -> app.state().running());
  }

  private static Stream<IWorkflowProcessModelVersion> workflowPMVs(Predicate<Application> filter) {
    return ApplicationRepository.of(ISecurityContext.current()).all()
        .filter(filter)
        .flatMap(app -> app.projects().all())
        .map(IWorkflowProcessModelVersion::of);
  }

  public static List<StartableModel> getDeployedStartables() {
    return getRunningWorkflowPMVs()
        .flatMap(pmv -> pmv.getStartables(ISession.current()).stream())
        .map(ProjectUtil::createCaseMapOrProcessStartable)
        .sorted(Comparator.comparing(StartableModel::getProjectName)
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
