package ch.ivyteam.workflowui.tasks;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.menu.MenuModel;

import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.IWorkflowEvent;
import ch.ivyteam.ivy.workflow.IWorkflowSession;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.workflowui.casemap.SidestepModel;
import ch.ivyteam.workflowui.casemap.SidestepUtil;
import ch.ivyteam.workflowui.util.TaskDetailUtil;
import ch.ivyteam.workflowui.util.UrlUtil;

@ManagedBean
@ViewScoped
public class TasksDetailsIvyDevWfBean {

  private String selectedTaskId;
  private ITask selectedTask;
  private List<SidestepModel> sidesteps;

  public String getSelectedTaskId() {
    return selectedTaskId;
  }

  public ITask getSelectedTask() {
    return selectedTask;
  }

  public void setSelectedTaskId(String selectedTaskId) {
    this.selectedTaskId = selectedTaskId;
    this.selectedTask = getTaskById(Long.parseLong(selectedTaskId));
    this.sidesteps = SidestepUtil.getSidesteps(selectedTask.getCase());
  }

  public ITask getTaskById(long id) {
    return IWorkflowContext.current().findTask(id);
  }

  public String getExecuteTaskLink() {
    return UrlUtil.generateStartFrameUrl(selectedTask.getStartLink());
  }

  public void expireTask() {
    selectedTask.setExpiryTimestamp(new Date());
  }

  public String getCaseName() {
    ICase technicalCase = selectedTask.getCase();
    if (technicalCase != null) {
      if (StringUtils.isNotBlank(technicalCase.getName())) {
        return technicalCase.getName();
      }
      return String.valueOf(technicalCase.getId());
    }
    return StringUtils.EMPTY;
  }

  public String getBusinessCaseName() {
    return "#" + getBusinessCaseId() + " "
            + selectedTask.getCase().getBusinessCase().getName();
  }

  public String getBusinessCaseId() {
    return Long.toString(selectedTask.getCase().getBusinessCase().getId());
  }

  public String getDescription() {
    return StringUtils.isEmpty(selectedTask.getDescription()) ? "No description"
            : selectedTask.getDescription();
  }

  public String getBusinessCaseTooltip() {
    return getBusinessCaseName() + " (" + selectedTask.getCase().getBusinessCase().getState() + ")";
  }

  public String getTechnicalCaseTooltip() {
    return "#" + Long.toString(selectedTask.getCase().getId()) + " (" + selectedTask.getCase().getState()
            + ")";
  }

  public String getWorkflowInfo(IWorkflowEvent event) {
    return event.getAdditionalInfo().stream().collect(Collectors.joining(", "));
  }

  public void park() {
    IWorkflowSession.current().parkTask(selectedTask);
  }

  public boolean canReset() {
    return EnumSet.of(TaskState.CREATED, TaskState.RESUMED, TaskState.PARKED, TaskState.READY_FOR_JOIN,
            TaskState.FAILED).contains(selectedTask.getState());
  }

  public boolean canPark() {
    return TaskState.WORKING_OR_SUSPENDED_STATES.contains(selectedTask.getState());
  }

  public String getName(ITask task) {
    return TaskDetailUtil.getName(task);
  }

  public boolean isDone() {
    return TaskDetailUtil.isDone(selectedTask);
  }

  public boolean canBeStarted() {
    return selectedTask.canUserResumeTask(ISession.current().getSessionUser().getUserToken()).wasSuccessful();
  }

  public boolean renderSidestepBtn() {
    return (!isDone() && !sidesteps.isEmpty());
  }

  public MenuModel getSidestepsMenuModel() {
    return SidestepUtil.createMenuModel(getSidesteps());
  }

  public List<SidestepModel> getSidesteps() {
    return sidesteps;
  }
}
