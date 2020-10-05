package ch.ivyteam.workflowui;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.persistence.IQueryResult;
import ch.ivyteam.ivy.persistence.OrderDirection;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.PropertyOrder;
import ch.ivyteam.ivy.workflow.TaskProperty;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.ivy.workflow.WorkflowNavigationUtil;
import ch.ivyteam.ivy.workflow.WorkflowPriority;
import ch.ivyteam.workflowui.util.DateUtil;
import ch.ivyteam.workflowui.util.UserUtil;

@ManagedBean
@SessionScoped
public class TasksBean
{

  public List<ITask> getTasks()
  {
    OrderDirection direction = OrderDirection.DESCENDING;
    IQueryResult<ITask> queryResult = WorkflowNavigationUtil.getWorkflowContext(IApplication.current())
            .findTasks(null, PropertyOrder.create(TaskProperty.ID, direction), 0, 100, true);
    return queryResult.getResultList();
  }

  public List<ITask> getPersonalTasks()
  {
    IQueryResult<ITask> queryResult = WorkflowNavigationUtil.getWorkflowContext(IApplication.current())
            .findWorkTasks(ISession.current().getSessionUser(), 0, 100);
    return queryResult.getResultList();
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

  public String getPrettyTime(Date date)
  {
    return DateUtil.getPrettyTime(date);
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
