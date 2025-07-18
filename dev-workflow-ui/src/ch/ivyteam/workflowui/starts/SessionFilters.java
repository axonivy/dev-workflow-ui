package ch.ivyteam.workflowui.starts;

import java.util.Set;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.util.IAttributeStore;

public class SessionFilters {

  private final static String ATTRIBUTE_NAME = "sessionProjectFilters";

  private final IAttributeStore<Set<String>> session;

  @SuppressWarnings("unchecked")
  public static SessionFilters current() {
    @SuppressWarnings("rawtypes")
    IAttributeStore store = ISession.current();
    return new SessionFilters(store);
  }

  public SessionFilters(IAttributeStore<Set<String>> session) {
    API.checkParameterNotNull(session, "session");
    this.session = session;
  }

  public void save(Set<String> selectedProjects) {
    if (selectedProjects == null || selectedProjects.isEmpty()) {
      clear();
      return;
    }
    session.setAttribute(ATTRIBUTE_NAME, selectedProjects);
  }

  public Set<String> load() {
    try {
      return session.getAttribute(ATTRIBUTE_NAME);
    } catch (ClassCastException ex) {
      clear();
      return null;
    }
  }

  public void clear() {
    session.setAttribute(ATTRIBUTE_NAME, null);
  }

  public boolean hasStoredFilters() {
    try {
      Set<String> stored = session.getAttribute(ATTRIBUTE_NAME);
      return stored != null && !stored.isEmpty();
    } catch (ClassCastException ex) {
      clear();
      return false;
    }
  }
}
