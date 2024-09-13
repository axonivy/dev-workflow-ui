package ch.ivyteam.workflowui.state;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.workflowui.tasks.TaskModel;

@ManagedBean
public class StateBadgeBean {

  public StateBadgeModel toStateBadgeModel(Object data) {
    if (data instanceof ICase caze) {
        return new StateBadgeModel(caze);
    } else if (data instanceof TaskModel taskModel) {
        return new StateBadgeModel(taskModel);
    } else if (data instanceof ITask task) {
      return new StateBadgeModel(task);
    }
    throw new IllegalArgumentException("Unsupported data type");
  }
}
