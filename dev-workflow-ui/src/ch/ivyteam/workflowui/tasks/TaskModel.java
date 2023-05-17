package ch.ivyteam.workflowui.tasks;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.process.model.value.PID;
import ch.ivyteam.ivy.process.viewer.api.ProcessViewer;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.ivy.workflow.WorkflowPriority;
import ch.ivyteam.ivy.workflow.businesscase.IBusinessCase;
import ch.ivyteam.ivy.workflow.task.IActivator;
import ch.ivyteam.ivy.workflow.task.TaskBusinessState;
import ch.ivyteam.workflowui.casemap.SidestepModel;
import ch.ivyteam.workflowui.casemap.SidestepUtil;
import ch.ivyteam.workflowui.customfield.CustomFieldModel;
import ch.ivyteam.workflowui.starts.CustomFieldsHelper;
import ch.ivyteam.workflowui.util.TaskUtil;

public class TaskModel {

  private final String uuid;
  private final String name;
  private final WorkflowPriority priority;
  private final String priorityIcon;
  private final TaskBusinessState businessState;
  private final TaskState state;
  private final String stateIcon;
  private final IBusinessCase businessCase;
  private final ICase technicalCase;
  private final IUser workerUser;
  private final String category;
  private final IActivator activator;
  private final String activatorName;
  private final Date startTimestamp;
  private final Date expiryTimestamp;
  private final Date endTimestamp;
  private final Date delayTimestamp;
  private final String description;
  private final String pmv;
  private final PID currentElement;
  private final String viewerLink;
  private final boolean viewerAllowed;

  private final WebLink startLink;
  private final List<WorkflowEventModel> workflowEvents;
  private List<SidestepModel> sidesteps;
  private final List<CustomFieldModel> customFields;

  public TaskModel(long taskId) {
    this(IWorkflowContext.current().findTask(taskId));
  }

  public TaskModel(ITask task) {
    this.uuid = task.uuid();
    this.name = TaskUtil.getName(task);
    this.priority = task.getPriority();
    this.priorityIcon = TaskUtil.getPriorityIcon(task);
    this.state = task.getState();
    this.businessState = task.getBusinessState();
    this.stateIcon = TaskUtil.getStateIcon(task.getBusinessState());
    this.businessCase = task.getCase().getBusinessCase();
    this.technicalCase = task.getCase();
    this.workerUser = task.getWorkerUser();
    this.category = task.getCategory().getName();
    this.activator = task.activator();
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
    this.pmv = task.getProcessModelVersion().getVersionName();
    this.currentElement = getCurrentElementId(task);
    this.viewerLink = buildViewerLink(task);
    this.viewerAllowed = isViewerAllowed(task);
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

  public IBusinessCase getBusinessCase() {
    return businessCase;
  }

  public ICase getCase() {
    return technicalCase;
  }

  public IUser getWorkerUser() {
    return workerUser;
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

  public String getDescription() {
    return StringUtils.isEmpty(description) ? "No description" : description;
  }

  public String getPmv() {
    return pmv;
  }

  public PID getCurrentElement() {
    return currentElement;
  }

  public String getViewerLink() {
    return viewerLink;
  }

  public boolean isViewerAllowed() {
    return viewerAllowed;
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

  public boolean renderSidestepBtn() {
    return (!isDone() && !sidesteps.isEmpty());
  }

  public List<WorkflowEventModel> getWorkflowEvents() {
    return workflowEvents;
  }

  public List<CustomFieldModel> getCustomFields() {
    return customFields;
  }

  public CustomFieldModel getCustomField(String customFieldName) {
    return new CustomFieldsHelper(this).find(customFieldName);
  }

  public String getCategory() {
    return category;
  }

  public Date getDelayTimestamp() {
    return delayTimestamp;
  }

  private static PID getCurrentElementId(ITask task) {
    return task.getStart().getProcessElementId();
  }

  private static String buildViewerLink(ITask task) {
    return ProcessViewer.of(task).url().toWebLink().get();
  }

  private static boolean isViewerAllowed(ITask task) {
    return ProcessViewer.of(task).isViewAllowed();
  }

  public String getDetailUrl() {
    return toDetailUrl(uuid);
  }

  public static String toDetailUrl(String uuid) {
    return "taskDetails.xhtml?task=" + uuid;
  }
}
