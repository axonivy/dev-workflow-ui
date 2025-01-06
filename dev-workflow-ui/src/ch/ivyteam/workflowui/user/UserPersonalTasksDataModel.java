package ch.ivyteam.workflowui.user;

import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.ivy.workflow.query.TaskQuery;
import ch.ivyteam.workflowui.tasks.TasksDataModel;

public class UserPersonalTasksDataModel extends TasksDataModel {

  private ISecurityMember securityMember;

  public void setSecurityMember(ISecurityMember securityMember) {
    this.securityMember = securityMember;
  }

  @Override
  protected void applyCustomFilter(TaskQuery taskQuery) {
    taskQuery.where().and(TaskQuery.create().where().canWorkOn(securityMember));
  }
}
