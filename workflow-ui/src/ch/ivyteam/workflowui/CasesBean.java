package ch.ivyteam.workflowui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.ivy.workflow.CaseState;

@ManagedBean
@ViewScoped
public class CasesBean
{
  private CasesDataModel casesDataModel;

  public CasesBean()
  {
    casesDataModel = new CasesDataModel();
    casesDataModel.setFilter("");
  }

  public CasesDataModel getCasesDataModel()
  {
    return casesDataModel;
  }

  public String getStateIcon(CaseState caseState)
  {
    switch (caseState)
    {
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
}
