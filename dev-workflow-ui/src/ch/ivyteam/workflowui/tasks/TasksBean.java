package ch.ivyteam.workflowui.tasks;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.workflowui.util.TaskUtil;

@ManagedBean
@ViewScoped
public class TasksBean {

  private final TasksDataModel tasksDataModel;

  public TasksBean() {
    tasksDataModel = new TasksDataModel();
    tasksDataModel.setFilter("");
  }

  public TasksDataModel getTasksDataModel() {
    return tasksDataModel;
  }

  public void displayTaskRow(SelectEvent<?> event) {
    TaskUtil.displayTaskRow(event);
  }

  public void redirectToTaskDetails(TaskModel model) {
    TaskUtil.redirectToTaskDetails(model);
  }

  public TaskLinkModel toTaskLinkModel(ITask task) {
    return new TaskLinkModel(task);
  }
}
