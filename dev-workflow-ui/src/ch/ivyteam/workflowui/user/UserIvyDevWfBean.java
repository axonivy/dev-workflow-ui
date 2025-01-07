package ch.ivyteam.workflowui.user;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.security.ISession;

@ManagedBean
public class UserIvyDevWfBean {

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
