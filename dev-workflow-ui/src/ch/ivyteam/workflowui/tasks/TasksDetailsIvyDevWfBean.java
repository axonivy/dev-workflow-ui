package ch.ivyteam.workflowui.tasks;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.menu.MenuModel;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.IWorkflowSession;
import ch.ivyteam.workflowui.casemap.SidestepUtil;
import ch.ivyteam.workflowui.util.RoleUtil;
import ch.ivyteam.workflowui.util.TaskUtil;
import ch.ivyteam.workflowui.util.UrlUtil;
import ch.ivyteam.workflowui.util.UserUtil;

@ManagedBean
@ViewScoped
public class TasksDetailsIvyDevWfBean {

  private String selectedTaskId;
  private TaskModel selectedTask;
  private boolean showUsers = true;
  private String delegateMember;

  public String getSelectedTaskId() {
    return selectedTaskId;
  }

  public TaskModel getSelectedTask() {
    return selectedTask;
  }

  public void setSelectedTaskId(String selectedTaskId) {
    this.selectedTaskId = selectedTaskId;
    this.selectedTask = new TaskModel(getTaskById(Long.parseLong(selectedTaskId)));
  }

  public ITask getTaskById(long id) {
    return TaskUtil.getTaskById(id);
  }

  public String getExecuteTaskLink() {
    return UrlUtil.generateStartFrameUrl(selectedTask.getStartLink());
  }

  public MenuModel getSidestepsMenuModel() {
    return SidestepUtil.createMenuModel(selectedTask.getSidesteps());
  }
  public void reset() {
    var itask = IWorkflowContext.current().findTask(selectedTask.getId());
    itask.reset();
    selectedTask = new TaskModel(itask);
  }

  public void park() {
    var itask = IWorkflowContext.current().findTask(selectedTask.getId());
    IWorkflowSession.current().parkTask(itask);
    selectedTask = new TaskModel(itask);
  }

  public void destroy() {
    var itask = IWorkflowContext.current().findTask(selectedTask.getId());
    itask.destroy();
    selectedTask = new TaskModel(itask);
  }

  public void expireTask() {
    var itask = IWorkflowContext.current().findTask(selectedTask.getId());
    itask.setExpiryTimestamp(new Date());
    selectedTask = new TaskModel(itask);
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
    var itask = IWorkflowContext.current().findTask(selectedTask.getId());
    var member = Ivy.security().members().find(delegateMember);
    if (member != null) {
      itask.setActivator(member);
      selectedTask = new TaskModel(itask);
    }
  }

}
