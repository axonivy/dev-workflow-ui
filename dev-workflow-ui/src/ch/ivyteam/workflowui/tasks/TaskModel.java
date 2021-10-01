package ch.ivyteam.workflowui.tasks;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.ivy.workflow.WorkflowPriority;
import ch.ivyteam.workflowui.casemap.SidestepModel;
import ch.ivyteam.workflowui.casemap.SidestepUtil;
import ch.ivyteam.workflowui.customfield.CustomFieldModel;
import ch.ivyteam.workflowui.util.TaskUtil;

public class TaskModel {

  private final long id;
  private final String name;
  private final WorkflowPriority priority;
  private final String priorityIcon;
  private final TaskState state;
  private final String stateIcon;
  private final ICase businessCase;
  private final ICase technicalCase;
  private final IUser workerUser;
  private final String category;
  private final ISecurityMember activator;
  private final String activatorName;
  private final Date startTimestamp;
  private final Date expiryTimestamp;
  private final Date endTimestamp;
  private final Date delayTimestamp;
  private final String description;

  private final WebLink startLink;
  private final List<WorkflowEventModel> workflowEvents;
  private List<SidestepModel> sidesteps;
  private final List<CustomFieldModel> customFields;

  public TaskModel(long taskId) {
    this(IWorkflowContext.current().findTask(taskId));
  }

  public TaskModel(ITask task) {
    this.id = task.getId();
    this.name = TaskUtil.getName(task);
    this.priority = task.getPriority();
    this.priorityIcon = TaskUtil.getPriorityIcon(task);
    this.state = task.getState();
    this.stateIcon = TaskUtil.getStateIcon(task);
    this.businessCase = task.getCase().getBusinessCase();
    this.technicalCase = task.getCase();
    this.workerUser = task.getWorkerUser();
    this.category = task.getCategory().getName();
    this.activator = task.getActivator();
    this.activatorName = task.getActivatorName();
    this.startTimestamp = task.getStartTimestamp();
    this.expiryTimestamp = task.getExpiryTimestamp();
    this.endTimestamp = task.getEndTimestamp();
    this.delayTimestamp = task.getDelayTimestamp();
    this.description = task.getDescription();
    this.startLink = task.getStartLink();
    this.workflowEvents = WorkflowEventModel.toList(task.getWorkflowEvents());
    this.sidesteps = SidestepUtil.getSidesteps(task.getCase());
    this.customFields = CustomFieldModel.create(task);
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public WorkflowPriority getPriority() {
    return priority;
  }

  public TaskState getState() {
    return state;
  }

  public ICase getBusinessCase() {
    return businessCase;
  }

  public ICase getCase() {
    return technicalCase;
  }

  public IUser getWorkerUser() {
    return workerUser;
  }

  public ISecurityMember getActivator() {
    return activator;
  }

  public String getActivatorName() {
    return activatorName;
  }

  public Date getStartTimestamp() {
    return startTimestamp;
  }

  public Date getExpiryTimestamp() {
    return expiryTimestamp;
  }

  public Date getEndTimestamp() {
    return endTimestamp;
  }

  public String getDescription() {
    return StringUtils.isEmpty(description) ? "No description" : description;
  }

  public WebLink getStartLink() {
    return startLink;
  }

  public List<SidestepModel> getSidesteps() {
    return sidesteps;
  }

  public String getStateIcon() {
    return stateIcon;
  }

  public String getPriorityIcon() {
    return priorityIcon;
  }

  public boolean canBeStarted() {
    return IWorkflowContext.current().findTask(id)
            .canUserResumeTask(ISession.current().getSessionUser().getUserToken()).wasSuccessful();
  }

  public boolean canReset() {
    return EnumSet.of(TaskState.CREATED, TaskState.RESUMED, TaskState.PARKED, TaskState.READY_FOR_JOIN,
            TaskState.FAILED).contains(state);
  }

  public boolean canPark() {
    return TaskState.WORKING_OR_SUSPENDED_STATES.contains(state);
  }

  public boolean isDone() {
    return TaskState.END_STATES.contains(state);
  }

  public boolean renderSidestepBtn() {
    return (!isDone() && !sidesteps.isEmpty());
  }

  public List<WorkflowEventModel> getWorkflowEvents() {
    return workflowEvents;
  }

  public List<CustomFieldModel> getCustomFields() {
    return customFields;
  }

  public String getCategory() {
    return category;
  }
  public Date getDelayTimestamp() {
    return delayTimestamp;
  }


}
