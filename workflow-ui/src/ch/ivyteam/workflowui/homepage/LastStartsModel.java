package ch.ivyteam.workflowui.homepage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.application.IProcessModel;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.ivy.workflow.query.CaseQuery;
import ch.ivyteam.workflowui.starts.StartableModel;

public class LastStartsModel {
  private List<StartableModel> starts;

  public LastStartsModel() {
    var lastRelativStartLinks = getLastStartedCasesOfUser().stream()
            .map(c -> c.getProcessStart().getLink().getRelative()).distinct().collect(Collectors.toList());

    starts = getAllStarts().stream()
            .filter(start -> lastRelativStartLinks.contains(start.getLink().getRelative())).limit(5)
            .collect(Collectors.toList());
  }

  private List<StartableModel> getAllStarts() {
    return IApplication.current().getProcessModels().stream()
            .map(IProcessModel::getReleasedProcessModelVersion)
            .map(IWorkflowProcessModelVersion::of).filter(Objects::nonNull)
            .flatMap(pmv -> pmv.getStartables(ISession.current()).stream())
            .map(StartableModel::new)
            .collect(Collectors.toList());
  }

  private static List<ICase> getLastStartedCasesOfUser() {
    var casequery = CaseQuery.create().where().isBusinessCase()
            .and(CaseQuery.create().where().currentUserHasStarted().or().currentUserIsOwner())
            .orderBy().startTimestamp().descending();
    return casequery.executor().results(0, 500);
  }

  public List<StartableModel> getStarts() {
    return starts;
  }
}
