package ch.ivyteam.workflowui.homepage;

import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.workflowui.cases.StartedCasesDataModel;
import ch.ivyteam.workflowui.starts.StartableModel;
import ch.ivyteam.workflowui.tasks.PersonalTasksDataModel;
import ch.ivyteam.workflowui.tasks.TaskModel;
import ch.ivyteam.workflowui.tasks.TasksDataModel;
import ch.ivyteam.workflowui.util.LastSessionStarts;
import ch.ivyteam.workflowui.util.TaskUtil;
import ch.ivyteam.workflowui.util.ViewerUtil;

@ManagedBean
@ViewScoped
public class HomepageBean {

  private final TasksDataModel tasksDataModel;
  private final StartedCasesDataModel startedCasesDataModel;
  private final Set<StartableModel> lastStarts;
  private String viewerTitle;
  private String viewerLink;

  public HomepageBean() {
    tasksDataModel = new PersonalTasksDataModel();
    startedCasesDataModel = new StartedCasesDataModel();
    lastStarts = LastSessionStarts.current().getAll();
  }

  public TasksDataModel getTasksDataModel() {
    return tasksDataModel;
  }

  public void executeTask(TaskModel task) {
    TaskUtil.executeTask(task);
  }

  public void executeTaskRow(SelectEvent<?> event) {
    TaskUtil.executeTaskRow(event);
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
        .map(ViewerUtil::getViewerDialogTitle)
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
}
