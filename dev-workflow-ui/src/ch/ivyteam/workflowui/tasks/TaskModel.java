package ch.ivyteam.workflowui.tasks;

import java.util.Date;
import java.util.EnumSet;

import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.ivy.workflow.WorkflowPriority;
import ch.ivyteam.ivy.workflow.task.IActivator;
import ch.ivyteam.ivy.workflow.task.TaskBusinessState;
import ch.ivyteam.workflowui.util.TaskUtil;

public class TaskModel {

  private long id;
  private String uuid;
  private String name;
  private WorkflowPriority priority;
  private String priorityIcon;
  private TaskBusinessState businessState;
  private TaskState state;
  private String category;
  private IActivator activator;
  private String activatorName;
  private Date startTimestamp;
  private Date expiryTimestamp;
  private Date endTimestamp;
  private Date delayTimestamp;
  private WebLink startLink;

  public TaskModel() {}

  public TaskModel(long taskId) {
    this(IWorkflowContext.current().findTask(taskId));
  }

  public TaskModel(ITask task) {
    this.id = task.getId();
    this.uuid = task.uuid();
    this.name = TaskUtil.getName(task);
    this.priority = task.getPriority();
    this.priorityIcon = TaskUtil.getPriorityIcon(task);
    this.state = task.getState();
    this.businessState = task.getBusinessState();
    this.category = task.getCategory().getName();
    this.activator = task.activator();
    this.activatorName = task.getActivatorName();
    this.startTimestamp = task.getStartTimestamp();
    this.expiryTimestamp = task.getExpiryTimestamp();
    this.endTimestamp = task.getEndTimestamp();
    this.delayTimestamp = task.getDelayTimestamp();
    this.startLink = task.getStartLink();
  }

  public long getId() {
    return id;
  }

  public String getUuid() {
    return uuid;
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

  public TaskBusinessState getBusinessState() {
    return businessState;
  }

  public IActivator getActivator() {
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

  public WebLink getStartLink() {
    return startLink;
  }

  public String getPriorityIcon() {
    return priorityIcon;
  }

  public boolean canBeStarted() {
    var sessionUser = ISession.current().getSessionUser();
    if (sessionUser == null) {
      return false;
    }
    return IWorkflowContext.current().findTask(uuid)
        .canUserResumeTask(sessionUser.getUserToken()).wasSuccessful();
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

  public String getCategory() {
    return category;
  }

  public Date getDelayTimestamp() {
    return delayTimestamp;
  }

  public String getDetailUrl() {
    return toDetailUrl(uuid);
  }

  public static String toDetailUrl(String uuid) {
    return "task.xhtml?id=" + uuid;
  }
}
