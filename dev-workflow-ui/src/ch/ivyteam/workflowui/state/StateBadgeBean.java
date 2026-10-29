package ch.ivyteam.workflowui.state;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.workflowui.cases.CaseModel;
import ch.ivyteam.workflowui.tasks.TaskModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class StateBadgeBean {

  public StateBadgeModel toStateBadgeModel(Object data) {
    return switch (data) {
      case CaseModel caseModel -> new StateBadgeModel(caseModel.getCase());
      case ICase caze -> new StateBadgeModel(caze);
      case TaskModel taskModel -> new StateBadgeModel(taskModel);
      case ITask task -> new StateBadgeModel(task);
      case TaskState state -> new StateBadgeModel(state);
      default -> throw new IllegalArgumentException("Unsupported data type");
    };
  }
}
