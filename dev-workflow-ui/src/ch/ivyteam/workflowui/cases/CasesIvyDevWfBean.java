package ch.ivyteam.workflowui.cases;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

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

  public String getName(CaseModel caseModel) {
    return CaseUtil.getPrettyName(caseModel.getCase());
  }

  public void rerunProcess(ICase caze) {
    CaseUtil.rerunCaseProcess(caze);
  }

  public void redirectToCaseRow(SelectEvent<CaseModel> event) {
    var object = event.getObject();
    if (object instanceof CaseModel caze) {
      RedirectUtil.redirect("case.xhtml?id=" + caze.getUuid());
    }
  }
}
