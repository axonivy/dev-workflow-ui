package ch.ivyteam.workflowui.util;

import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.util.IAttributeStore;
import ch.ivyteam.workflowui.starts.StartableModel;

public class LastSessionStarts {

  private final static String ATTRIBUTE_NAME = "lastStarts";

  private final IAttributeStore<List<StartableModel>> session;

  @SuppressWarnings("unchecked")
  public static LastSessionStarts current() {
    @SuppressWarnings("rawtypes")
    IAttributeStore store = ISession.current();
    return new LastSessionStarts(store);
  }

  public LastSessionStarts(IAttributeStore<List<StartableModel>> session) {
    API.checkParameterNotNull(session, "session");
    this.session = session;
  }

  public void add(StartableModel startable) {
    List<StartableModel> list = getEmptyIfNull();
    if (list.contains(startable)) {
      return;
    }
    list.add(startable);
    session.setAttribute(ATTRIBUTE_NAME, list);
  }

  public List<StartableModel> getAll() {
    return getEmptyIfNull();
  }

  private List<StartableModel> getEmptyIfNull() {
    var starts = session.getAttribute(ATTRIBUTE_NAME);
    if (starts == null) {
      return new ArrayList<>();
    }
    return starts;
  }
}
