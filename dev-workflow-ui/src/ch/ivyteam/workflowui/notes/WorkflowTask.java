package ch.ivyteam.workflowui.notes;

import java.util.List;

import ch.ivyteam.ivy.workflow.INote;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.workflowui.util.TaskUtil;

public class WorkflowTask implements WorkflowItem {

  private final ITask task;

  public WorkflowTask(ITask task) {
    this.task = task;
  }

  @Override
  public List<INote> getNotes() {
    return task.getNotes();
  }

  @Override
  public boolean canResume() {
    return TaskUtil.canResume(task.uuid());
  }
}
