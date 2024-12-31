package ch.ivyteam.workflowui.tasks;

import java.util.Date;
import java.util.EnumSet;
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
import ch.ivyteam.ivy.workflow.TaskState;
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
    return TaskUtil.canResume(selectedTask.getUuid());
  }

  private final EnumSet<TaskState> activeTaskStates = EnumSet.of(
      TaskState.DONE,
      TaskState.READY_FOR_JOIN,
      TaskState.JOINING,
      TaskState.JOIN_FAILED,
      TaskState.CREATED,
      TaskState.RESUMED,
      TaskState.PARKED,
      TaskState.DESTROYED);

  public boolean showInfoBanner() {
    return isActivator() && activeTaskStates.contains(selectedTask.getState());
  }

  public String getInfoBannerSeverity() {
    boolean validState = selectedTask.getState() == TaskState.RESUMED
        || selectedTask.getState() == TaskState.CREATED;
    boolean notCurrentSession = selectedTask.getWorkerSession() != ISession.current();
    return validState && notCurrentSession && isActivator() && currentIsWorkerUser() ? "warn" : "info";
  }

  public String getInfoBannerMessage() {
    return switch (selectedTask.getState()) {
      case CREATED, RESUMED, PARKED -> {
        if (currentIsWorkerUser()) {
          yield "You are currently working on the task in a different session. You may reset the task and start it again if you have closed your browser.";
        } else {
          yield "You cannot work on the task because user '%s' is currently working on it.".formatted(selectedTask.getWorkerUser().getName());
        }
      }
      case DONE, READY_FOR_JOIN, JOINING, JOIN_FAILED -> {
        if (currentIsWorkerUser()) {
          yield "You already have completed the task";
        } else {
          yield "Task has already been completed by user '%s'"
              .formatted(selectedTask.getWorkerUser().getName());
        }
      }
      case DESTROYED -> "You cannot work on the task because it was destroyed";
      default -> "invalid state";
    };
  }

  private boolean isActivator() {
    return selectedTask.getActivator().isMember(ISession.current());
  }

  private boolean currentIsWorkerUser() {
    return selectedTask.getWorkerUser().equals(ISession.current().getSessionUser());
  }

  public String getViewerLink() {
    return viewerLink;
  }

  public void setViewerLink() {
    this.viewerLink = selectedTask.getViewerLink();
  }
}
