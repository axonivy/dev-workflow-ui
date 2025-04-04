package ch.ivyteam.workflowui.user;

import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.query.CaseQuery;
import ch.ivyteam.workflowui.cases.CasesDataModel;

public class UserStartedCasesDataModel extends CasesDataModel {

  private static final long serialVersionUID = 1L;
  private IUser user;

  public void setSecurityMember(IUser user) {
    this.user = user;
  }

  @Override
  protected CaseQuery createCaseQuery() {
    return CaseQuery.create().where().isBusinessCase()
        .and(CaseQuery.create().where().hasStarted(user));
  }
}
