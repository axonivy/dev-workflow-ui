package ch.ivyteam.workflowui.notes;

import java.util.List;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.note.Note;
import ch.ivyteam.workflowui.util.TaskUtil;

public class WorkflowCase implements WorkflowItem {

  private final ICase caze;

  public WorkflowCase(ICase caze) {
    this.caze = caze;
  }

  @Override
  public List<Note> getNotes() {
    return caze.notes().all();
  }

  @Override
  public boolean canResume() {
    ITask firstTask = caze.getFirstTask();
    return firstTask != null && TaskUtil.canResume(firstTask.uuid());
  }
}
