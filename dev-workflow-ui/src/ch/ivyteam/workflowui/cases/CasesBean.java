package ch.ivyteam.workflowui.cases;

import java.io.Serializable;
import java.util.Map;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.workflowui.util.CaseUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.url.Page;

@Named
@ViewScoped
public class CasesBean implements Serializable {

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

  public String getName(CaseModel caseModel) {
    return CaseUtil.getPrettyName(caseModel.getCase());
  }

  public void redirectToCaseRow(SelectEvent<CaseModel> event) {
    var object = event.getObject();
    if (object instanceof CaseModel caze) {
      RedirectUtil.redirect(Page.CASE, Map.of("id", caze.getUuid()));
    }
  }
}
