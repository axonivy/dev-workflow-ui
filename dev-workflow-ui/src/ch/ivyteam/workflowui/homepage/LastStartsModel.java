package ch.ivyteam.workflowui.homepage;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.application.IProcessModel;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.persistence.PersistentObjectDeletedException;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.IProcessStart;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.ivy.workflow.IWorkflowSession;
import ch.ivyteam.ivy.workflow.query.CaseQuery;
import ch.ivyteam.workflowui.starts.StartableModel;

public class LastStartsModel {
  private List<StartableModel> starts;

  public LastStartsModel() {
    var lastRelativStartLinks = getLastStartedCasesOfUser()
            .map(ICase::getProcessStart).filter(Objects::nonNull)
            .map(IProcessStart::getLink).map(WebLink::getRelative)
            .distinct().collect(toList());

    starts = getAllStarts().stream()
            .filter(start -> lastRelativStartLinks.contains(start.getLink().getRelative())).limit(10)
            .collect(toList());
  }

  private List<StartableModel> getAllStarts() {
    return IApplication.current().getProcessModels().stream()
            .map(IProcessModel::getReleasedProcessModelVersion)
            .map(IWorkflowProcessModelVersion::of).filter(Objects::nonNull)
            .flatMap(pmv -> pmv.getStartables(ISession.current()).stream())
            .map(StartableModel::new)
            .collect(toList());
  }

  private static Stream<ICase> getLastStartedCasesOfUser() {
    var casequery = CaseQuery.create().where().isBusinessCase()
            .and(CaseQuery.create().where().currentUserHasStarted().or().currentUserIsOwner())
            .orderBy().startTimestamp().descending();
    try {
      var transientCases = IWorkflowSession.current().findCreatedAndResumedWorkTasks(0, 50).stream()
              .map(ITask::getCase).map(ICase::getBusinessCase);
      return Stream.concat(transientCases, casequery.executor().results(0, 500).stream());
    } catch (PersistentObjectDeletedException ex) {
      Ivy.log().error("Could not get transient cases", ex);
      return casequery.executor().results(0, 500).stream();
    }
  }

  public List<StartableModel> getStarts() {
    return starts;
  }
}
