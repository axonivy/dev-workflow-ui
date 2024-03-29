package ch.ivyteam.workflowui.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.util.IAttributeStore;
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

  public Set<StartableModel> storedStarts() {
    return session.getAttribute(ATTRIBUTE_NAME);
  }

  private Set<StartableModel> getStarts() {
    var starts = storedStarts();
    if (starts == null) {
      return Set.of();
    }
    var deployedStartables = ProcessModelsUtil.getDeployedStartables();
    if (deployedStartables.isEmpty()) {
      return starts;
    }
    try {
      return starts.stream().filter(deployedStartables::contains).collect(Collectors.toSet());
    } catch (ClassCastException ex) {
      return Set.of();
    }
  }
}
