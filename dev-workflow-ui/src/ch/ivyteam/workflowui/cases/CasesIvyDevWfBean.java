package ch.ivyteam.workflowui.cases;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.workflow.CaseState;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.workflowui.util.RedirectUtil;

@ManagedBean
@ViewScoped
public class CasesIvyDevWfBean {
  private CasesDataModel casesDataModel;

  public CasesIvyDevWfBean() {
    casesDataModel = new CasesDataModel();
    casesDataModel.setFilter("");
  }

  public CasesDataModel getCasesDataModel() {
    return casesDataModel;
  }

  public String getName(ICase icase) {
    if (StringUtils.isBlank(icase.getName())) {
      return "[Case: " + icase.getId() + "]";
    }
    return icase.getName();
  }

  public String getStateIcon(CaseState caseState) {
    switch (caseState) {
      case DONE:
        return "check-circle-1 case-state-done";
      case CREATED:
      case RUNNING:
        return "hourglass case-state-in-progress case-state-in-progress";
      case DESTROYED:
      case ZOMBIE:
        return "alert-circle case-state-zombie-destroyed";
      default:
        return "synchronize-arrows";
    }
  }

  public void redirectToCaseRow(SelectEvent<?> event) {
    Object object = event.getObject();
    if (object instanceof ICase) {
      RedirectUtil.redirect("caseDetails.xhtml?case=" + ((ICase) object).getId());
    }
  }
}
