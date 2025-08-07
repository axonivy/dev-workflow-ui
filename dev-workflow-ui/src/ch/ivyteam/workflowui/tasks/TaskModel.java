package ch.ivyteam.workflowui.tasks;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;

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
import ch.ivyteam.ivy.workflow.note.Note;
import ch.ivyteam.ivy.workflow.task.TaskBusinessState;
import ch.ivyteam.workflowui.casemap.SidestepModel;
import ch.ivyteam.workflowui.casemap.SidestepUtil;
import ch.ivyteam.workflowui.customfield.CustomFieldModel;
import ch.ivyteam.workflowui.starts.CustomFieldsHelper;
import ch.ivyteam.workflowui.util.TaskUtil;

public class TaskModel {

  private long id;
  private String uuid;
  private String name;
  private WorkflowPriority priority;
  private String priorityIcon;
  private TaskBusinessState businessState;
  private TaskState state;
  private IBusinessCase businessCase;
  private ICase technicalCase;
  private IUser workerUser;
  private String category;
  private List<ResponsibleModel> responsibles;
  private Date startTimestamp;
  private Date expiryTimestamp;
  private Date endTimestamp;
  private Date delayTimestamp;
  private String description;
  private String pmv;
  private PID currentElement;
  private String viewerLink;
  private boolean viewerAllowed;
  private ISession workerSession;

  private WebLink startLink;
  private List<WorkflowEventModel> workflowEvents;
  private List<SidestepModel> sidesteps;
  private List<CustomFieldModel> customFields;
  private List<Note> notes;

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
    this.businessCase = task.getCase().getBusinessCase();
    this.technicalCase = task.getCase();
    this.workerUser = task.getWorkerUser();
    this.category = task.getCategory().getName();
    this.responsibles = task.responsibles().all().stream()
        .map(ResponsibleModel::new)
        .toList();
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
    this.workerSession = task.getWorkerSession();
    this.notes = task.notes().all();
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

  public IBusinessCase getBusinessCase() {
    return businessCase;
  }

  public ICase getCase() {
    return technicalCase;
  }

  public IUser getWorkerUser() {
    return workerUser;
  }

  public List<ResponsibleModel> getResponsibles() {
    return responsibles;
  }

  public ResponsibleModel getFirstResponsible() {
    return responsibles.isEmpty() ? null : responsibles.getFirst();
  }

  public boolean hasResponsibles() {
    return !responsibles.isEmpty();
  }

  public List<ResponsibleModel> getOtherResponsibles() {
    return responsibles.size() > 1 ? responsibles.subList(1, responsibles.size()) : List.of();
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
    return description;
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
    return "task?id=" + uuid;
  }

  public ISession getWorkerSession() {
    return this.workerSession;
  }

  public List<Note> getNotes() {
    return notes;
  }
}
