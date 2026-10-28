package ch.ivyteam.workflowui.tasks;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.process.model.value.PID;
import ch.ivyteam.ivy.process.viewer.api.ProcessViewer;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.businesscase.IBusinessCase;
import ch.ivyteam.ivy.workflow.note.Note;
import ch.ivyteam.workflowui.casemap.SidestepModel;
import ch.ivyteam.workflowui.casemap.SidestepUtil;
import ch.ivyteam.workflowui.customfield.CustomFieldModel;
import ch.ivyteam.workflowui.starts.CustomFieldsHelper;

public class TaskDetailModel extends TaskModel {

  private IBusinessCase businessCase;
  private ICase technicalCase;
  private IUser workerUser;
  private String description;
  private String pmv;
  private PID currentElement;
  private String viewerLink;
  private boolean viewerAllowed;
  private ISession workerSession;

  private List<WorkflowEventModel> workflowEvents;
  private List<SidestepModel> sidesteps;
  private List<CustomFieldModel> customFields;
  private List<Note> notes;

  public TaskDetailModel() {}

  public TaskDetailModel(long taskId) {
    this(IWorkflowContext.current().findTask(taskId));
  }

  public TaskDetailModel(ITask task) {
    super(task);
    this.businessCase = task.getCase().getBusinessCase();
    this.technicalCase = task.getCase();
    this.workerUser = task.getWorkerUser();
    this.description = task.getDescription();
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

  public IBusinessCase getBusinessCase() {
    return businessCase;
  }

  public ICase getCase() {
    return technicalCase;
  }

  public IUser getWorkerUser() {
    return workerUser;
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

  public List<SidestepModel> getSidesteps() {
    return sidesteps;
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

  private static PID getCurrentElementId(ITask task) {
    return task.getStart().getProcessElementId();
  }

  private static String buildViewerLink(ITask task) {
    return ProcessViewer.of(task).url().toWebLink().get();
  }

  private static boolean isViewerAllowed(ITask task) {
    return ProcessViewer.of(task).isViewAllowed();
  }

  public ISession getWorkerSession() {
    return this.workerSession;
  }

  public List<Note> getNotes() {
    return notes;
  }
}
