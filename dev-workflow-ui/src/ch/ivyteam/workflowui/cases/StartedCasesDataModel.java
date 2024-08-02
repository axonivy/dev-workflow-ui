package ch.ivyteam.workflowui.cases;

import ch.ivyteam.ivy.workflow.query.CaseQuery;

public class StartedCasesDataModel extends CasesDataModel {

  private static final long serialVersionUID = 6711980004059004230L;

  @Override
  protected CaseQuery createCaseQuery() {
    return CaseQuery.create().where().isBusinessCase()
            .and(CaseQuery.create().where().currentUserHasStarted().or().currentUserIsOwner());
  }
}
