package ch.ivyteam.workflowui.user;

import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.ivy.workflow.query.CaseQuery;
import ch.ivyteam.workflowui.cases.CasesDataModel;

public class UserStartedCasesDataModel extends CasesDataModel {

  private ISecurityMember securityMember;

  public void setSecurityMember(ISecurityMember securityMember) {
    this.securityMember = securityMember;
  }

  @Override
  protected CaseQuery createCaseQuery() {
    return CaseQuery.create().where().isBusinessCase()
        .and(CaseQuery.create().where().hasStarted(securityMember));
  }
}
