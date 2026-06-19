package ch.ivyteam.workflowui.user;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import ch.ivyteam.ivy.security.ISession;

@Named
@RequestScoped
public class UserBean {

  public String getCurrentUserName() {
    return getSession().getSessionUserName();
  }

  public boolean isSessionUserUnknown() {
    return getSession().isSessionUserUnknown();
  }

  public ISession getSession() {
    return ISession.current();
  }
}
