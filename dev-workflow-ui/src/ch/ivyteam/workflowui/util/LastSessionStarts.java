package ch.ivyteam.workflowui.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.application.IProcessModel;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.util.IAttributeStore;
import ch.ivyteam.workflowui.starts.CaseMapStartableModel;
import ch.ivyteam.workflowui.starts.StartableModel;

public class LastSessionStarts {

  private final static String ATTRIBUTE_NAME = "lastStarts";

  private final IAttributeStore<Set<StartableModel>> session;

  @SuppressWarnings("unchecked")
  public static LastSessionStarts current() {
    @SuppressWarnings("rawtypes")
    IAttributeStore store = ISession.current();
    return new LastSessionStarts(store);
  }

  public LastSessionStarts(IAttributeStore<Set<StartableModel>> session) {
    API.checkParameterNotNull(session, "session");
    this.session = session;
  }

  public void add(StartableModel startable) {
    var startsSet = new HashSet<>(getStarts());
    startsSet.add(startable);
    session.setAttribute(ATTRIBUTE_NAME, startsSet);
  }

  public Set<StartableModel> getAll() {
    return getStarts();
  }

  private Set<StartableModel> getStarts() {
    var starts = session.getAttribute(ATTRIBUTE_NAME);
    if (starts == null) {
      return Set.of();
    }
    var deployedStartables = getDeployedStartables();
    if (deployedStartables == null) {
      return starts;
    }
    return starts.stream().filter(s -> deployedStartables.contains(s)).collect(Collectors.toSet());
  }

  private List<StartableModel> getDeployedStartables() {
    var application = IApplication.current();
    if (application == null) {
      return null;
    }
    var pmvs = application.getProcessModels().stream()
            .map(IProcessModel::getReleasedProcessModelVersion)
            .map(IWorkflowProcessModelVersion::of).collect(Collectors.toList());

    List<StartableModel> starts = new ArrayList<>();

    for (IWorkflowProcessModelVersion pmv : pmvs) {
      var listOfStartables = pmv.getStartables(ISession.current());
      for (IWebStartable startable : listOfStartables) {
        starts.add(createCaseMapOrProcessStartable(startable, pmv.getVersionName()));
      }
    }
    return starts;
  }

  private StartableModel createCaseMapOrProcessStartable(IWebStartable startable, String pmvVersionName) {
    if (startable.getType().equals("casemap")) {
      return new CaseMapStartableModel(startable, pmvVersionName);
    }
    return new StartableModel(startable);
  }
}
