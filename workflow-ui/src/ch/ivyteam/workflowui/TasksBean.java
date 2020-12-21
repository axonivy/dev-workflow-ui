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
      case CREATED:
        return "note_add";
      case DELAYED:
        return "timer";
      case DESTROYED:
        return "clear";
      case DONE:
        return "done";
      case FAILED:
        return "cancel";
      case JOINING:
        return "flight_land";
      case JOIN_FAILED:
        return "airplanemode_inactive";
      case PARKED:
        return "local_parking";
      case READY_FOR_JOIN:
        return "flight_takeoff";
      case RESUMED:
        return "settings";
      case SUSPENDED:
        return "pause";
      case WAITING_FOR_INTERMEDIATE_EVENT:
        return "event";
      case ZOMBIE:
        return "coronavirus";
      case UNASSIGNED:
      default:
        return "help";
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
        return "keyboard_capslock";
      case HIGH:
        return "keyboard_arrow_up";
      case LOW:
        return "keyboard_arrow_down";
      case NORMAL:
        return "remove";
      default:
        return "remove";
    }
  }

}
