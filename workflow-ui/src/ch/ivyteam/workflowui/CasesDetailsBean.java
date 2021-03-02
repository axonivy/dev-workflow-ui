package ch.ivyteam.workflowui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.casemap.runtime.model.ICaseMap;
import ch.ivyteam.ivy.casemap.runtime.repo.restricted.ICaseMapRepository;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.WorkflowNavigationUtil;
import ch.ivyteam.ivy.workflow.custom.field.ICustomField;
import ch.ivyteam.ivy.workflow.document.IDocument;
import ch.ivyteam.ivy.workflow.document.Path;

@SuppressWarnings("restriction")
@ManagedBean
@ViewScoped
public class CasesDetailsBean
{

  private String selectedCaseId;
  private ICase selectedCase;

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

  public ICaseMap getCaseMap()
  {
    return ICaseMapRepository.getInstance().find(selectedCase.getId());
  }

  public List<IDocument> getCaseDocuments()
  {
    return selectedCase.documents().getAll();
  }

  public StreamedContent getDocumentFile(Path pathToDocument) throws FileNotFoundException
  {
    String contentType = FacesContext.getCurrentInstance().getExternalContext().getMimeType(pathToDocument.asString());
    String filePath = IApplication.current().getFileArea().getAbsolutePath() + "/" + pathToDocument.asString();
    return new DefaultStreamedContent (new FileInputStream(filePath), contentType, pathToDocument.getLastSegment());
  }

  public String getCustomFields()
  {
    String customFields = "";
    for (ICustomField customField : selectedCase.customFields().all())
    {
      customFields += customField.name() + " = " + customField.getOrNull();
    }
    return StringUtils.isNotBlank(customFields) ? customFields : "No custom fields";
  }
}
