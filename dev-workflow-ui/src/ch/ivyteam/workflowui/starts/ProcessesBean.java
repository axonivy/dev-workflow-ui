package ch.ivyteam.workflowui.starts;

import java.util.Optional;
import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.ViewerUtil;

@ManagedBean
@ViewScoped
public class ProcessesBean {
  private final StartsDataModel startsDataModel;
  private String viewerLink;
  private String viewerTitle;
  private boolean showProjectFilter = false;

  public ProcessesBean() {
    startsDataModel = new StartsDataModel();
  }

  public StartsDataModel getStartsDataModel() {
    return startsDataModel;
  }

  public void redirect() {
    RedirectUtil.redirect("home.xhtml");
  }

  public void setViewerStart(String link) {
    var processStarts = startsDataModel.getStartables().stream();
    viewerTitle = findViewerTitle(processStarts, link).orElse("");
    viewerLink = link;
  }

  private Optional<String> findViewerTitle(Stream<? extends StartableModel> starts, String link) {
    return starts.filter(start -> start.getViewerLink().get().equals(link))
        .findFirst()
        .map(ViewerUtil::getViewerDialogTitle);
  }

  public void startRow(SelectEvent<StartableModel> event) {
    event.getObject().execute();
  }

  public String getViewerTitle() {
    return viewerTitle;
  }

  public String getViewerLink() {
    return viewerLink;
  }

  public boolean isShowProjectFilter() {
    return showProjectFilter;
  }

  public void toggleProjectFilter() {
    showProjectFilter = !showProjectFilter;
  }

  public void initFilter() {
    startsDataModel.getProjectFilterModel().initTransientSelections();
  }

  public void applyProjectFilter() {
    startsDataModel.getProjectFilterModel().applyFilter();
    startsDataModel.applyProjectFilter();
  }

  public void resetAllFilters() {
    startsDataModel.resetAllProjectFilters();
    showProjectFilter = false;
  }
}
