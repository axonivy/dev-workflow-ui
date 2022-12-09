package ch.ivyteam.workflowui.cases;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.workflow.CaseState;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.workflowui.util.CaseUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;

@ManagedBean
@ViewScoped
public class CasesIvyDevWfBean {

  private final CasesDataModel casesDataModel;

  public CasesIvyDevWfBean() {
    casesDataModel = new CasesDataModel();
    casesDataModel.setFilter("");
  }

  public CasesDataModel getCasesDataModel() {
    return casesDataModel;
  }

  public String getName(ICase icase) {
    return CaseUtil.getPrettyName(icase);
  }

  public String getStateIcon(CaseState caseState) {
    return switch (caseState) {
      case DONE -> "check-circle-1 case-state-done";
      case CREATED, RUNNING -> "hourglass case-state-in-progress case-state-in-progress";
      case DESTROYED -> "alert-circle case-state-destroyed";
      default -> "synchronize-arrows";
    };
  }

  public void redirectToCaseRow(SelectEvent<?> event) {
    var object = event.getObject();
    if (object instanceof ICase caze) {
      RedirectUtil.redirect("caseDetails.xhtml?case=" + caze.getId());
    }
  }
}
