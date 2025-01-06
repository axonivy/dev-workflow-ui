package ch.ivyteam.workflowui.user;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.workflowui.util.TaskUtil;

@ManagedBean
@ViewScoped
public class UserIvyDevWfBean {
  private final ISecurityContext securityContext = ISecurityContext.current();
  private IUser user;
  private String userId;
  private String userName;
  private final UserPersonalTasksDataModel userTasksDataModel = new UserPersonalTasksDataModel();
  private final UserStartedCasesDataModel userCasesDataModel = new UserStartedCasesDataModel();

  public UserStartedCasesDataModel getUserCasesDataModel() {
    return userCasesDataModel;
  }

  public UserPersonalTasksDataModel getTasksDataModel() {
    return userTasksDataModel;
  }

  public void displayTaskRow(SelectEvent<?> event) {
    TaskUtil.displayTaskRow(event);
  }

  public String getCurrentUserName() {
    return getSession().getSessionUserName();
  }

  public boolean isSessionUserUnknown() {
    return getSession().isSessionUserUnknown();
  }

  public ISession getSession() {
    return ISession.current();
  }

  public String createUserLink(String memberId) {
    return "user.xhtml?userId=" + memberId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    if (userId != null) {
      this.userId = userId;
      setUser(userId);
    }
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setUser(String userId) {
    this.user = securityContext.users().findById(userId);
    userTasksDataModel.setSecurityMember(user);
    userCasesDataModel.setSecurityMember(user);
  }

  public IUser getUser() {
    return user;
  }

}
