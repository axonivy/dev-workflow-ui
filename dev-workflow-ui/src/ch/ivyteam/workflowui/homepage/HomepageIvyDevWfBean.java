package ch.ivyteam.workflowui.homepage;

import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.workflowui.cases.StartedCasesDataModel;
import ch.ivyteam.workflowui.starts.StartableModel;
import ch.ivyteam.workflowui.tasks.TasksDataModel;
import ch.ivyteam.workflowui.util.LastSessionStarts;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.ViewerUtil;

@ManagedBean
@ViewScoped
public class HomepageIvyDevWfBean {
  private TasksDataModel tasksDataModel;
  private StartedCasesDataModel startedCasesDataModel;
  private Set<StartableModel> lastStarts;
  private String viewerTitle;
  private String viewerLink;

  public HomepageIvyDevWfBean() {
    tasksDataModel = new TasksDataModel();
    startedCasesDataModel = new StartedCasesDataModel();
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

  public void setViewerStart(String link) {
    viewerTitle = lastStarts.stream()
            .filter(start -> start.getViewerLink().get().equals(link))
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

  public StartedCasesDataModel getStartedCasesDataModel() {
    return startedCasesDataModel;
  }

  public void rerunProcess(ICase caze) {
    var startable = new StartableModel(caze.getBusinessCase().getStartedFrom());
    LastSessionStarts.current().add(startable);
    startable.execute();
  }
}
