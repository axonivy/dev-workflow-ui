package ch.ivyteam.workflowui.state;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.workflowui.cases.CaseModel;
import ch.ivyteam.workflowui.tasks.TaskModel;

@ManagedBean
public class StateBadgeBean {

  public StateBadgeModel toStateBadgeModel(Object data) {
    return switch (data) {
      case CaseModel caseModel -> new StateBadgeModel(caseModel.getCase());
      case ICase caze -> new StateBadgeModel(caze);
      case TaskModel taskModel -> new StateBadgeModel(taskModel);
      case ITask task -> new StateBadgeModel(task);
      default -> throw new IllegalArgumentException("Unsupported data type");
    };
  }
}
