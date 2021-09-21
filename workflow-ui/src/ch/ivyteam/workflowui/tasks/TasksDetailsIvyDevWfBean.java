package ch.ivyteam.workflowui.tasks;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.menu.MenuModel;

import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.IWorkflowEvent;
import ch.ivyteam.ivy.workflow.IWorkflowSession;
import ch.ivyteam.workflowui.casemap.SidestepUtil;
import ch.ivyteam.workflowui.util.TaskUtil;
import ch.ivyteam.workflowui.util.UrlUtil;

@ManagedBean
@ViewScoped
public class TasksDetailsIvyDevWfBean {

  private String selectedTaskId;
  private TaskModel selectedTask;

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

  public String getDescription() {
    return selectedTask.getDescription();
  }

  public String getWorkflowEventInfo(IWorkflowEvent event) {
    return TaskUtil.getWorkflowEventInfo(event);
  }

  public MenuModel getSidestepsMenuModel() {
    return SidestepUtil.createMenuModel(selectedTask.getSidesteps());
  }

  public boolean canBeStarted() {
    return selectedTask.canBeStarted();
  }

  public boolean canReset() {
    return selectedTask.canReset();
  }

  public void reset() {
    var itask = IWorkflowContext.current().findTask(selectedTask.getId());
    itask.reset();
    selectedTask = new TaskModel(itask);
  }

  public boolean canPark() {
    return selectedTask.canPark();
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

  public boolean isDone() {
    return selectedTask.isDone();
  }

  public boolean renderSidestepBtn() {
    return selectedTask.renderSidestepBtn();
  }

  public void expireTask() {
    var itask = IWorkflowContext.current().findTask(selectedTask.getId());
    itask.setExpiryTimestamp(new Date());
    selectedTask = new TaskModel(itask);
  }

}
