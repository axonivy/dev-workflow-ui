package ch.ivyteam.workflowui.cases;

import static ch.ivyteam.ivy.workflow.CaseState.CREATED;
import static ch.ivyteam.ivy.workflow.CaseState.RUNNING;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.menu.MenuModel;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.query.CaseQuery;
import ch.ivyteam.workflowui.casemap.CaseMapModel;
import ch.ivyteam.workflowui.casemap.SidestepModel;
import ch.ivyteam.workflowui.casemap.SidestepUtil;
import ch.ivyteam.workflowui.customfield.CustomFieldModel;
import ch.ivyteam.workflowui.document.DocumentModel;
import ch.ivyteam.workflowui.util.CaseDetailUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;

@ManagedBean
@ViewScoped
public class CasesDetailsBean {

  private String selectedCaseId;
  private ICase selectedCase;
  private List<CustomFieldModel> customFields;
  private List<DocumentModel> documents;
  private CaseMapModel caseMapModel;
  private boolean showSystemTasks = false;
  private List<ITask> tasks;
  private List<SidestepModel> sidesteps;
  private MenuModel sidestepsMenuModel;

  public String getSelectedCaseId() {
    return selectedCaseId;
  }

  public ICase getSelectedCase() {
    return selectedCase;
  }

  public void setSelectedCaseId(String selectedCaseId) {
    this.selectedCaseId = selectedCaseId;
    this.selectedCase = getCaseById(Long.parseLong(selectedCaseId));
    customFields = CustomFieldModel.create(selectedCase);
    documents = DocumentModel.create(selectedCase);
    caseMapModel = CaseMapModel.create(selectedCase);
    tasks = CaseDetailUtil.filterTasksOfCase(selectedCase.tasks().all(), showSystemTasks);
    sidesteps = SidestepUtil.getSidesteps(selectedCase);
    sidestepsMenuModel = SidestepUtil.createMenuModel(sidesteps);
  }

  public ICase getCaseById(long id) {
    return IWorkflowContext.current().findCase(id);
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

  public void redirectToCase(ICase toCase) {
    RedirectUtil.redirect("caseDetails.xhtml?case=" + toCase.getId());
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

  public List<ITask> getTasks() {
    return tasks;
  }

  public boolean isShowSystemTasks() {
    return showSystemTasks;
  }

  public void setShowSystemTasks(boolean showSystemTasks) {
    this.showSystemTasks = showSystemTasks;
    tasks = CaseDetailUtil.filterTasksOfCase(selectedCase.tasks().all(), showSystemTasks);
  }

  public List<SidestepModel> getSidesteps()
  {
    return sidesteps;
  }

  public MenuModel getSidestepsMenuModel() {
    return sidestepsMenuModel;
  }
}
