package ch.ivyteam.workflowui.homepage;

import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.workflowui.starts.StartableModel;
import ch.ivyteam.workflowui.tasks.TasksDataModel;
import ch.ivyteam.workflowui.util.LastSessionStarts;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.ViewerUtil;

@ManagedBean
@ViewScoped
public class HomepageIvyDevWfBean {
  private TasksDataModel tasksDataModel;
  private Set<StartableModel> lastStarts;
  private String viewerTitle;
  private String viewerLink;

  public HomepageIvyDevWfBean() {
    tasksDataModel = new TasksDataModel();
    lastStarts = LastSessionStarts.current().getAll();
  }

  public TasksDataModel getTasksDataModel() {
    return tasksDataModel;
  }

  public Set<StartableModel> getLastStarts() {
    return lastStarts;
  }

  public int getTasksSize() {
    return tasksDataModel.getSize();
  }

  public void executeLastStart(SelectEvent<?> event) {
    Object object = event.getObject();
    if (object instanceof StartableModel) {
      ((StartableModel) object).execute();
    }
  }

  public void redirectIfNoTasksOrLastStarts() {
    if (lastStarts.isEmpty() && tasksDataModel.getSize() < 1 && Ivy.session().getAttribute("redirectedToStarts") == null) {
      RedirectUtil.redirect("starts.xhtml");
      Ivy.session().setAttribute("redirectedToStarts", true);
    }
  }

  public void setViewerStart(String link) {
    viewerTitle = lastStarts.stream()
            .filter(start -> start.getViewerLink().toString().equals(link))
            .findFirst()
            .map(start -> ViewerUtil.getViewerDialogTitle(start))
            .orElse("");
    viewerLink = link;
  }

  public String getViewerTitle() {
    return viewerTitle;
  }

  public String getViewerLink() {
    return viewerLink;
  }
}
