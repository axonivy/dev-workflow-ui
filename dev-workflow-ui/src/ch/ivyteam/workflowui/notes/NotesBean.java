package ch.ivyteam.workflowui.notes;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.ivy.workflow.INote;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.TaskUtil;

@ManagedBean
@ViewScoped
public class NotesBean {

  private String id;
  private List<INote> notes;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
    var task = TaskUtil.getTaskById(id);
    this.notes = task.getNotes();
  }

  public List<INote> getNotes() {
    return notes;
  }

  public void redirectIfCantResume() {
    if (!TaskUtil.canResume(id)) {
      RedirectUtil.redirect();
    }
  }

}
