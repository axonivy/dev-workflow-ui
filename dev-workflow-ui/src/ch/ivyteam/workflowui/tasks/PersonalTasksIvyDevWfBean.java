package ch.ivyteam.workflowui.tasks;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.workflowui.util.TaskUtil;

@ManagedBean
@ViewScoped
public class PersonalTasksIvyDevWfBean {

  private TasksDataModel tasksDataModel;

  public PersonalTasksIvyDevWfBean() {
    tasksDataModel = new PersonalTasksDataModel();
    tasksDataModel.setFilter("");
  }

  public TasksDataModel getTasksDataModel() {
    return tasksDataModel;
  }

  public void executeTaskRow(SelectEvent<?> event) {
    TaskUtil.executeTaskRow(event);
  }

  public void executeTask(TaskModel model) {
    TaskUtil.executeTask(model);
  }
}
