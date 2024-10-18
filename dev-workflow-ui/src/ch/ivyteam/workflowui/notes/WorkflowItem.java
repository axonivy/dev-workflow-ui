package ch.ivyteam.workflowui.notes;

import java.util.List;

import ch.ivyteam.ivy.workflow.note.Note;

public interface WorkflowItem {

  List<Note> getNotes();
  boolean canResume();
}
