package ch.ivyteam.workflowui.cases;

import static ch.ivyteam.ivy.process.viewer.api.ProcessViewerUrlBuilder.Mode.PREVIEW;
import static ch.ivyteam.ivy.workflow.CaseState.CREATED;
import static ch.ivyteam.ivy.workflow.CaseState.RUNNING;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.menu.MenuModel;

import ch.ivyteam.ivy.process.viewer.api.ProcessViewer;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.query.CaseQuery;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.casemap.CaseMapModel;
import ch.ivyteam.workflowui.casemap.SidestepModel;
import ch.ivyteam.workflowui.casemap.SidestepUtil;
import ch.ivyteam.workflowui.customfield.CustomFieldModel;
import ch.ivyteam.workflowui.document.DocumentModel;
import ch.ivyteam.workflowui.tasks.TaskModel;
import ch.ivyteam.workflowui.tasks.WorkflowEventModel;
import ch.ivyteam.workflowui.util.CaseUtil;
import ch.ivyteam.workflowui.util.ResponseHelper;
import ch.ivyteam.workflowui.util.TaskUtil;
import ch.ivyteam.workflowui.util.ViewerUtil;

@ManagedBean
@ViewScoped
public class CasesDetailsIvyDevWfBean {

  private String selectedCaseId;
  private ICase selectedCase;
  private List<CustomFieldModel> customFields;
  private List<DocumentModel> documents;
  private CaseMapModel caseMapModel;
  private boolean showSystemTasks = false;
  private List<TaskModel> tasks;
  private List<SidestepModel> sidesteps;
  private MenuModel sidestepsMenuModel;
  private List<WorkflowEventModel> workflowEvents;
  private IWebStartable startable;
  private String viewerLink;
  private String processPreviewLink;
  private boolean viewerAllowed;

  public String getSelectedCaseId() {
    return selectedCaseId;
  }

  public ICase getSelectedCase() {
    return selectedCase;
  }

  public void setSelectedCaseId(String selectedCaseId) {
    this.selectedCaseId = selectedCaseId;
    this.selectedCase = getCaseById(selectedCaseId);
    if (selectedCase == null) {
      ResponseHelper.notFound("Case " + selectedCaseId + " does not exist");
      return;
    }
    customFields = CustomFieldModel.create(selectedCase);
    documents = DocumentModel.create(selectedCase);
    caseMapModel = CaseMapModel.create(selectedCase);
    tasks = TaskUtil.toTaskModelList(CaseUtil.filterTasksOfCase(selectedCase.tasks().all(), showSystemTasks));
    sidesteps = SidestepUtil.getSidesteps(selectedCase);
    sidestepsMenuModel = SidestepUtil.createMenuModel(sidesteps);
    workflowEvents = WorkflowEventModel.toList(selectedCase.getWorkflowEvents());
    startable = selectedCase.getBusinessCase().getStartedFrom();
    viewerLink = ViewerUtil.getViewerLink(selectedCase);
    viewerAllowed = ViewerUtil.isViewerAllowed(selectedCase);
    processPreviewLink = generateProcessPreviewLink();
  }

  private String generateProcessPreviewLink() {
    return ProcessViewer.of(selectedCase).url().mode(PREVIEW).zoom(75).toWebLink().get();
  }

  public ICase getCaseById(String uuid) {
    return IWorkflowContext.current().findCase(uuid);
  }

  public String getCreatorUser() {
    return selectedCase.getCreatorUserName() == null ? "N/A" : selectedCase.getCreatorUserName();
  }

  public String getDescription() {
    return StringUtils.isEmpty(selectedCase.getDescription()) ? "No description"
            : selectedCase.getDescription();
  }

  public CaseMapModel getCaseMap() {
    return caseMapModel;
  }

  public List<CustomFieldModel> getCustomFields() {
    return customFields;
  }

  public List<DocumentModel> getDocuments() {
    return documents;
  }

  public List<ICase> getCaseList() {
    return CaseQuery.subCases().where().businessCaseId()
            .isEqual(selectedCase.getBusinessCase().getId())
            .executor().results();
  }

  public String isCurrentHierarchyCase(ICase caze) {
    if (caze.equals(selectedCase)) {
      return "current-hierarchy-case";
    }
    return "";
  }

  public boolean canBeDestoryed() {
    return Arrays.asList(CREATED, RUNNING).contains(selectedCase.getState());
  }

  public List<TaskModel> getTasks() {
    return tasks;
  }

  public boolean isShowSystemTasks() {
    return showSystemTasks;
  }

  public void setShowSystemTasks(boolean showSystemTasks) {
    this.showSystemTasks = showSystemTasks;
    tasks = TaskUtil.toTaskModelList(CaseUtil.filterTasksOfCase(selectedCase.tasks().all(), showSystemTasks));
  }

  public List<SidestepModel> getSidesteps() {
    return sidesteps;
  }

  public MenuModel getSidestepsMenuModel() {
    return sidestepsMenuModel;
  }

  public List<WorkflowEventModel> getWorkflowEvents() {
    return workflowEvents;
  }

  public boolean isCaseMap() {
    return startable != null && startable.getType().equals("casemap");
  }

  public String getViewerLink() {
    return viewerLink;
  }

  public boolean isViewerAllowed() {
    return viewerAllowed;
  }

  public String getViewerDialogTitle() {
    return ViewerUtil.getViewerDialogTitle(selectedCase);
  }

  public String getProcessPreviewLink() {
    return processPreviewLink;
  }
}
