package ch.ivyteam.workflowui;

import java.util.Date;
import java.util.EnumSet;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowEvent;
import ch.ivyteam.ivy.workflow.IWorkflowSession;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.ivy.workflow.WorkflowNavigationUtil;

@ManagedBean
@ViewScoped
public class TasksDetailsBean
{

  private String selectedTaskId;
  private ITask selectedTask;

  public String getSelectedTaskId()
  {
    return selectedTaskId;
  }

  public ITask getSelectedTask()
  {
    return selectedTask;
  }

  public void setSelectedTaskId(String selectedTaskId)
  {
    this.selectedTaskId = selectedTaskId;
    this.selectedTask = getTaskById(Long.parseLong(selectedTaskId));
  }

  public ITask getTaskById(long id)
  {
    return WorkflowNavigationUtil.getWorkflowContext(IApplication.current()).findTask(id);
  }

  public void expireTask()
  {
    selectedTask.setExpiryTimestamp(new Date());
  }

  public String getCaseName()
  {
    ICase technicalCase = selectedTask.getCase();
    if (technicalCase != null)
    {
      if (StringUtils.isNotBlank(technicalCase.getName()))
      {
        return technicalCase.getName();
      }
      return String.valueOf(technicalCase.getId());
    }
    return StringUtils.EMPTY;
  }

  public String getBusinessCaseName()
  {
    return "#" + getBusinessCaseId() + " "
            + selectedTask.getCase().getBusinessCase().getName();
  }

  public String getBusinessCaseId()
  {
    return Long.toString(selectedTask.getCase().getBusinessCase().getId());
  }

  public String getDescription()
  {
    return StringUtils.isEmpty(selectedTask.getDescription()) ? "No description"
            : selectedTask.getDescription();
  }

  public String getBusinessCaseTooltip()
  {
    return getBusinessCaseName() + " (" + selectedTask.getCase().getBusinessCase().getState() + ")";
  }

  public String getTechnicalCaseTooltip()
  {
    return "#" + Long.toString(selectedTask.getCase().getId()) + " (" + selectedTask.getCase().getState()
            + ")";
  }

  public String getWorkflowInfo(IWorkflowEvent event)
  {
    return event.getAdditionalInfo().stream().collect(Collectors.joining(", "));
  }

  public void park()
  {
    IWorkflowSession.current().parkTask(selectedTask);
  }

  public boolean canReset()
  {
    return EnumSet.of(TaskState.CREATED, TaskState.RESUMED, TaskState.PARKED, TaskState.READY_FOR_JOIN,
            TaskState.FAILED).contains(selectedTask.getState());
  }

  public boolean canPark()
  {
    return TaskState.WORKING_OR_SUSPENDED_STATES.contains(selectedTask.getState());
  }

  public String getName(ITask task)
  {
    if (StringUtils.isBlank(task.getName()))
    {
      return "[Task: " + task.getId() + "]";
    }
    return task.getName();
  }

  public Boolean isDone()
  {
    return TaskState.END_STATES.contains(selectedTask.getState());
  }
}
