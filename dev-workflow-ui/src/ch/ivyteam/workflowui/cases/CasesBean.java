package ch.ivyteam.workflowui.cases;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.workflowui.util.CaseUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;

@ManagedBean
@ViewScoped
public class CasesBean {

  private final CasesDataModel casesDataModel;

  public CasesBean() {
    casesDataModel = new CasesDataModel();
    casesDataModel.setFilter("");
  }

  public CasesDataModel getCasesDataModel() {
    return casesDataModel;
  }

  public String getName(ICase icase) {
    return CaseUtil.getPrettyName(icase);
  }

  public void rerunProcess(ICase caze) {
    CaseUtil.rerunCaseProcess(caze);
  }

  public void redirectToCaseRow(SelectEvent<ICase> event) {
    var object = event.getObject();
    if (object instanceof ICase caze) {
      RedirectUtil.redirect("case.xhtml?id=" + caze.uuid());
    }
  }
}
