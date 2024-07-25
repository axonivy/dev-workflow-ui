package ch.ivyteam.workflowui.notes;

import java.util.List;

import ch.ivyteam.ivy.workflow.INote;

public interface WorkflowItem {

  List<INote> getNotes();

  boolean canResume();
}
