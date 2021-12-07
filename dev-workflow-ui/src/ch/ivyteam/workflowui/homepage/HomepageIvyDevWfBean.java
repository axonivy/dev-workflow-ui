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

@ManagedBean
@ViewScoped
public class HomepageIvyDevWfBean {
  private TasksDataModel tasksDataModel;

  public HomepageIvyDevWfBean() {
    tasksDataModel = new TasksDataModel();
  }

  public TasksDataModel getTasksDataModel() {
    return tasksDataModel;
  }

  public Set<StartableModel> getLastStarts() {
    return LastSessionStarts.current().getAll();
  }

  public int getTasksSize() {
    return tasksDataModel.getSize();
  }

  public void executeLastStart(SelectEvent event) {
    Object object = event.getObject();
    if (object instanceof StartableModel) {
      ((StartableModel) object).execute();
    }
  }

  public void redirectIfNoTasksOrLastStarts() {
    if (LastSessionStarts.current().getAll().isEmpty() && tasksDataModel.getSize() < 1 && Ivy.session().getAttribute("redirectedToStarts") == null) {
      RedirectUtil.redirect("starts.xhtml");
      Ivy.session().setAttribute("redirectedToStarts", true);
    }
  }
}
