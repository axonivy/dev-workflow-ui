package ch.ivyteam.workflowui.homepage;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.workflowui.starts.StartableModel;
import ch.ivyteam.workflowui.tasks.TasksDataModel;

@ManagedBean
@ViewScoped
public class HomepageBean
{
  private LastStartsModel lastStartsModel;
  private TasksDataModel tasksDataModel;

  public HomepageBean()
  {
    lastStartsModel = new LastStartsModel();
    tasksDataModel = new TasksDataModel();
  }

  public TasksDataModel getTasksDataModel()
  {
    return tasksDataModel;
  }

  public List<StartableModel> getLastStarts()
  {
    return lastStartsModel.getStarts();
  }

  public int getTasksSize()
  {
    return tasksDataModel.getSize();
  }
}
