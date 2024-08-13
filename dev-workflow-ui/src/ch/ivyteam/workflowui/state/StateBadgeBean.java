package ch.ivyteam.workflowui.state;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.workflowui.tasks.TaskModel;

@ManagedBean
public class StateBadgeBean {

  public StateBadgeModel toStateBadgeModel(Object data) {
    if (data instanceof ICase) {
        return new StateBadgeModel((ICase) data);
    } else if (data instanceof TaskModel) {
        return new StateBadgeModel((TaskModel) data);
    } else if (data instanceof ITask) {
      return new StateBadgeModel((ITask) data);
    }
    throw new IllegalArgumentException("Unsupported data type");
  }
}
