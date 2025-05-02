package ch.ivyteam.workflowui.state;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.workflowui.cases.CaseModel;
import ch.ivyteam.workflowui.tasks.TaskModel;

@ManagedBean
public class StateBadgeBean {

  public StateBadgeModel toStateBadgeModel(Object data) {
    if (data instanceof CaseModel caseModel) {
      return new StateBadgeModel(caseModel.getCase());
    } else if (data instanceof ICase caze) {
      return new StateBadgeModel(caze);
    } else if (data instanceof TaskModel taskModel) {
      return new StateBadgeModel(taskModel);
    } else if (data instanceof ITask task) {
      return new StateBadgeModel(task);
    } else if (data instanceof TaskState state) {
      return new StateBadgeModel(state);
    }
    throw new IllegalArgumentException("Unsupported data type");
  }
}
