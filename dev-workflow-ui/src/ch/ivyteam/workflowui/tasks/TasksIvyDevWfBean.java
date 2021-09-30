package ch.ivyteam.workflowui.tasks;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.workflowui.TaskLinkModel;
import ch.ivyteam.workflowui.util.TaskUtil;

@ManagedBean
@ViewScoped
public class TasksIvyDevWfBean {
  private TasksDataModel tasksDataModel;

  public TasksIvyDevWfBean() {
    tasksDataModel = new TasksDataModel();
    tasksDataModel.setFilter("");
  }

  public TasksDataModel getTasksDataModel() {
    return tasksDataModel;
  }

  public void executeTaskRow(SelectEvent event) {
    TaskUtil.executeTaskRow(event);
  }

  public void displayTaskRow(SelectEvent event) {
    TaskUtil.displayTaskRow(event);
  }

  public void redirectToTaskDetails(long taskId) {
    TaskUtil.redirectToTaskDetails(taskId);
  }

  public void executeTask(long taskId) {
   TaskUtil.executeTask(taskId);
  }

  public TaskLinkModel toTaskLinkModel(ITask task) {
    return new TaskLinkModel(task);
  }
}