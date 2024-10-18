package ch.ivyteam.workflowui.notes;

import java.util.List;

import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.note.Note;
import ch.ivyteam.workflowui.util.TaskUtil;

public class WorkflowTask implements WorkflowItem {

  private final ITask task;

  public WorkflowTask(ITask task) {
    this.task = task;
  }

  @Override
  public List<Note> getNotes() {
    return task.notes().all();
  }

  @Override
  public boolean canResume() {
    return TaskUtil.canResume(task.uuid());
  }
}
