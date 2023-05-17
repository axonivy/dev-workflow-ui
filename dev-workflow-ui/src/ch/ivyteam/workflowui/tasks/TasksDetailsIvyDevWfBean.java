package ch.ivyteam.workflowui.tasks;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.menu.MenuModel;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.IWorkflowSession;
import ch.ivyteam.workflowui.casemap.SidestepUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.ResponseHelper;
import ch.ivyteam.workflowui.util.RoleUtil;
import ch.ivyteam.workflowui.util.TaskUtil;
import ch.ivyteam.workflowui.util.UserUtil;

@ManagedBean
@ViewScoped
public class TasksDetailsIvyDevWfBean {

  private String selectedTaskId;
  private TaskModel selectedTask;
  private boolean showUsers = true;
  private String delegateMember;
  private String viewerLink;

  public String getSelectedTaskId() {
    return selectedTaskId;
  }

  public TaskModel getSelectedTask() {
    return selectedTask;
  }

  public void setSelectedTaskId(String selectedTaskId) {
    this.selectedTaskId = selectedTaskId;
    var task = TaskUtil.getTaskById(selectedTaskId);
    if (task == null) {
      ResponseHelper.notFound("Task " + task + " does not exist");
      return;
    }
    this.selectedTask = new TaskModel(task);
  }

  public String getExecuteTaskLink() {
    return TaskUtil.createTaskUrl(selectedTask);
  }

  public MenuModel getSidestepsMenuModel() {
    return SidestepUtil.createMenuModel(selectedTask.getSidesteps());
  }

  public void reset() {
    var itask = loadTask();
    itask.reset();
    selectedTask = new TaskModel(itask);
  }

  public void park() {
    var itask = loadTask();
    IWorkflowSession.current().parkTask(itask);
    selectedTask = new TaskModel(itask);
  }

  public void destroy() {
    var itask = loadTask();
    itask.destroy();
    selectedTask = new TaskModel(itask);
  }

  public void expireTask() {
    var itask = loadTask();
    itask.setExpiryTimestamp(new Date());
    selectedTask = new TaskModel(itask);
  }

  public void clearDelay() {
    var itask = loadTask();
    itask.setDelayTimestamp(null);
    selectedTask = new TaskModel(itask);
  }

  private ITask loadTask() {
    return IWorkflowContext.current().findTask(selectedTask.getUuid());
  }

  public String getDelegateMember() {
    return delegateMember;
  }

  public void setDelegateMember(String delegateMember) {
    this.delegateMember = delegateMember;
  }

  public void setShowUsers(boolean showUsers) {
    this.showUsers = showUsers;
  }

  public boolean isShowUsers() {
    return showUsers;
  }

  public List<IUser> getAllUsers() {
    return UserUtil.getUsers();
  }

  public List<IRole> getAllRoles() {
    return RoleUtil.getRoles();
  }

  public void delegateTask() {
    var itask = loadTask();
    var member = Ivy.security().members().find(delegateMember);
    if (member != null) {
      itask.setActivator(member);
      selectedTask = new TaskModel(itask);
    }
  }

  public void redirectIfCantResume() {
    if (!canResume()) {
      RedirectUtil.redirect();
    }
  }

  private boolean canResume() {
    if (selectedTaskId == null) {
      return false;
    }
    var session = ISession.current();
    if (session == null) {
      return false;
    }
    var user = session.getSessionUser();
    if (user == null) {
      return false;
    }
    var task = loadTask();
    if (task == null) {
      return false;
    }
    if (UserUtil.isAdmin()) {
      return true;
    }
    var isActivator = task.activator().isMember(user.getUserToken());
    if (task.getWorkerUser() == null) {
      return isActivator;
    }
    var isWorker = task.getWorkerUser().isMember(user.getUserToken(), false);
    return isActivator || isWorker;
  }

  public String getViewerLink() {
    return viewerLink;
  }

  public void setViewerLink() {
    this.viewerLink = selectedTask.getViewerLink();
  }
}
