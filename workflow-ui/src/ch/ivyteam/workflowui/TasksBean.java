package ch.ivyteam.workflowui;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.ivy.workflow.WorkflowPriority;
import ch.ivyteam.workflowui.util.DateUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.UserUtil;

@ManagedBean
@ViewScoped
public class TasksBean
{
  private TasksDataModel tasksDataModel;

  public TasksBean()
  {
    tasksDataModel = new TasksDataModel();
    tasksDataModel.setFilter("");
  }

  public TasksDataModel getTasksDataModel()
  {
    return tasksDataModel;
  }

  public String getFilter()
  {
    return tasksDataModel.getFilter();
  }

  public void setFilter(String filter)
  {
    this.tasksDataModel.setFilter(filter);
  }

  @SuppressWarnings("removal")
  public String getStateIcon(TaskState taskState)
  {
    switch (taskState)
    {
      case DELAYED:
        return "alarm-bell-timer";
      case DONE:
        return "check-circle-1";
      case FAILED:
      case JOIN_FAILED:
        return "mood-warning";
      case PARKED:
        return "touch-finger_1";
      case CREATED:
      case RESUMED:
        return "hourglass";
      case SUSPENDED:
        return "controls-play";
      case WAITING_FOR_INTERMEDIATE_EVENT:
        return "synchronize-arrow-clock";
      case DESTROYED:
      case ZOMBIE:
        return "alert-circle";
      case UNASSIGNED:
      default:
        return "synchronize-arrows";
    }
  }

  public void checkIfLoggedIn()
  {
    UserUtil.redirectIfNotLoggedIn();
  }

  public boolean checkIfPersonalTasks()
  {
    return UserUtil.checkIfPersonalTask();
  }

  public String getPrettyTime(Date date)
  {
    return DateUtil.getPrettyTime(date);
  }

  public String getDateTime(Date date)
  {
    return DateUtil.getDateAndTime(date);
  }

  public void executeTask(long taskId)
  {
    ITask task = Ivy.wf().findTask(taskId);
    RedirectUtil.redirect("frame.xhtml?taskUrl=" + task.getStartLink().getRelative());
  }

  public String getPriorityIcon(WorkflowPriority priority)
  {
    switch (priority)
    {
      case EXCEPTION:
        return "alert-circle";
      case HIGH:
        return "arrow-up-1";
      case LOW:
        return "arrow-down-1";
      case NORMAL:
        return "subtract";
      default:
        return "subtract";
    }
  }

}
