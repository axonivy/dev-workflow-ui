package ch.ivyteam.workflowui.user;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.workflowui.util.PermissionsUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.ResponseHelper;
import ch.ivyteam.workflowui.util.TaskUtil;
import ch.ivyteam.workflowui.util.url.Page;

@ManagedBean
@ViewScoped
public class UserDetailBean {
  private final ISecurityContext securityContext = ISecurityContext.current();
  private IUser user;
  private String userId;
  private final UserPersonalTasksDataModel userTasksDataModel = new UserPersonalTasksDataModel();
  private final UserStartedCasesDataModel userCasesDataModel = new UserStartedCasesDataModel();

  public UserStartedCasesDataModel getUserCasesDataModel() {
    return userCasesDataModel;
  }

  public void redirectToCaseRow(SelectEvent<ICase> event) {
    var object = event.getObject();
    if (object instanceof ICase caze) {
      RedirectUtil.redirect(Page.CASE, Map.of("id", caze.uuid()));
    }
  }

  public UserPersonalTasksDataModel getTasksDataModel() {
    return userTasksDataModel;
  }

  public void displayTaskRow(SelectEvent<?> event) {
    TaskUtil.displayTaskRow(event);
  }

  public void setUserId(String userId) {
    if (!PermissionsUtil.isAdmin()) {
      ResponseHelper.forbidden("You have no permission to read detailpage");
      return;
    }
    if (userId != null) {
      this.userId = userId;
      setUser(userId);
    }
  }

  public String getUserId() {
    return userId;
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
