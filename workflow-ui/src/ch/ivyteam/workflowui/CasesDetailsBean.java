package ch.ivyteam.workflowui;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.WorkflowNavigationUtil;
import ch.ivyteam.workflowui.casemap.CaseMapModel;
import ch.ivyteam.workflowui.customfield.CustomFieldModel;
import ch.ivyteam.workflowui.document.DocumentModel;

@ManagedBean
@ViewScoped
public class CasesDetailsBean
{

  private String selectedCaseId;
  private ICase selectedCase;
  private List<CustomFieldModel> customFields;
  private List<DocumentModel> documents;
  private CaseMapModel caseMapModel;

  public String getSelectedCaseId()
  {
    return selectedCaseId;
  }

  public ICase getSelectedCase()
  {
    return selectedCase;
  }

  public void setSelectedCaseId(String selectedCaseId)
  {
    this.selectedCaseId = selectedCaseId;
    this.selectedCase = getCaseById(Long.parseLong(selectedCaseId));
    customFields = CustomFieldModel.create(selectedCase);
    documents = DocumentModel.create(selectedCase);
    caseMapModel = CaseMapModel.create(selectedCase);
  }

  public ICase getCaseById(long id)
  {
    return WorkflowNavigationUtil.getWorkflowContext(IApplication.current()).findCase(id);
  }

  public String getCreatorUser()
  {
    return selectedCase.getCreatorUserName() == null ? "N/A" : selectedCase.getCreatorUserName();
  }

  public String getDescription()
  {
    return StringUtils.isEmpty(selectedCase.getDescription()) ? "No description"
            : selectedCase.getDescription();
  }

  public List<ITask> getRelatedTasks()
  {
    List<ITask> tasks = selectedCase.tasks().all();
    return tasks.size() > 20 ? tasks.subList(0, 20) : tasks;
  }

  public CaseMapModel getCaseMap()
  {
    return caseMapModel;
  }

  public List<CustomFieldModel> getCustomFields()
  {
    return customFields;
  }

  public List<DocumentModel> getDocuments()
  {
    return documents;
  }
}
