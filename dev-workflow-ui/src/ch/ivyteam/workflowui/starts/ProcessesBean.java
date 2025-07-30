package ch.ivyteam.workflowui.starts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
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
  private String projects;

  public ProcessesBean() {
    startsDataModel = new StartsDataModel();
  }

  public void initializeView() {
    if (FacesContext.getCurrentInstance().isPostback()) {
      return;
    }

    boolean hasUrlParams = StringUtils.isNotBlank(startsDataModel.getGlobalFilter()) || StringUtils.isNotBlank(projects);

    if (!hasUrlParams && shouldLoadFromSession()) {
      initializeFiltersFromSession();
      return;
    }

    if (!hasUrlParams) {
      return;
    }

    if (StringUtils.isNotBlank(projects)) {
      var projectList = Arrays.asList(projects.split(","));
      var availableProjects = startsDataModel.getProjectFilterModel().getAllProjects();
      var validProjects = projectList.stream()
          .filter(availableProjects::contains)
          .collect(Collectors.toList());

      if (!validProjects.isEmpty()) {
        startsDataModel.getProjectFilterModel().setAppliedProjects(validProjects);
        saveProjectFiltersToSession();
      }
    }
  }

  public void updateUrlWithFilters() {
    UriBuilder uriBuilder = UriBuilder.fromPath("starts.xhtml");

    if (StringUtils.isNotBlank(startsDataModel.getGlobalFilter())) {
      uriBuilder.queryParam("q", startsDataModel.getGlobalFilter());
    }

    var projectsToPersist = startsDataModel.getProjectFilterModel().getEffectiveAppliedProjects();
    if (!projectsToPersist.isEmpty()) {
      uriBuilder.queryParam("projects", String.join(",", projectsToPersist));
    }

    String url = uriBuilder.build().toString();
    PrimeFaces.current().executeScript("history.pushState(null, null, '" + url + "');");
  }

  private void saveProjectFiltersToSession() {
    var projectsToPersist = startsDataModel.getProjectFilterModel().getEffectiveAppliedProjects();
    SessionFilters.current().save(new HashSet<>(projectsToPersist));
  }

  public StartsDataModel getStartsDataModel() {
    return startsDataModel;
  }

  public void redirect() {
    RedirectUtil.redirect();
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
    updateUrlWithFilters();
    saveProjectFiltersToSession();
  }

  public void resetAllFilters() {
    startsDataModel.resetAllProjectFilters();
    showProjectFilter = false;
    updateUrlWithFilters();
    clearSessionFilters();
  }

  private boolean shouldLoadFromSession() {
    return StringUtils.isBlank(projects);
  }

  private void initializeFiltersFromSession() {
    SessionFilters sessionFilters = SessionFilters.current();
    if (!sessionFilters.hasStoredFilters()) {
      return;
    }

    var storedProjects = sessionFilters.load();
    if (storedProjects == null || storedProjects.isEmpty()) {
      return;
    }

    var availableProjects = startsDataModel.getProjectFilterModel().getAllProjects();
    var validProjects = storedProjects.stream()
        .filter(availableProjects::contains)
        .collect(Collectors.toList());

    if (!validProjects.isEmpty()) {
      startsDataModel.getProjectFilterModel().setAppliedProjects(validProjects);
      updateUrlWithFilters();
    }
  }

  private void clearSessionFilters() {
    SessionFilters.current().clear();
  }

  public String getProjects() {
    return projects;
  }

  public void setProjects(String projects) {
    this.projects = projects;
  }
}
