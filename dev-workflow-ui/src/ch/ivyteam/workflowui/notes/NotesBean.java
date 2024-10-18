package ch.ivyteam.workflowui.notes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DialogFrameworkOptions;

import ch.ivyteam.ivy.workflow.note.Note;
import ch.ivyteam.workflowui.util.CaseUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.TaskUtil;

@ManagedBean
@ViewScoped
public class NotesBean {

  private String id;
  private WorkflowItem workflowItem;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
    var task = TaskUtil.getTaskById(id);
    if (task != null) {
      this.workflowItem = new WorkflowTask(task);
      return;
    }
    var caze = CaseUtil.getCaseById(id);
    if (caze != null) {
      this.workflowItem = new WorkflowCase(caze);
    } else {
      RedirectUtil.redirect();
    }
  }

  public List<Note> getNotes() {
    if (workflowItem != null) {
      return workflowItem.getNotes();
    }
    return List.of();
  }

  public void redirectIfCantResume() {
    if (workflowItem == null || !workflowItem.canResume()) {
      RedirectUtil.redirect();
    }
  }

  public void openNotesDialog(String elementId) {
    var options = DialogFrameworkOptions.builder()
            .modal(true)
            .responsive(true)
            .width("640")
            .height("340")
            .contentHeight("100%")
            .contentWidth("100%")
            .build();
    Map<String, List<String>> params = new HashMap<>();
    params.put("id", List.of(elementId));
    PrimeFaces.current().dialog().openDynamic("notesDialog", options, params);
  }
}
