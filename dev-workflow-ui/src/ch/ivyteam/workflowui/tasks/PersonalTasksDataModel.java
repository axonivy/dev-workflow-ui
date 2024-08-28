package ch.ivyteam.workflowui.tasks;

import ch.ivyteam.ivy.workflow.query.TaskQuery;

public class PersonalTasksDataModel extends TasksDataModel {

  private static final long serialVersionUID = 1L;

  @Override
  protected void applyCustomFilter(TaskQuery taskQuery) {
    taskQuery.where().and(TaskQuery.create().where().currentUserCanWorkOn());
  }
}
