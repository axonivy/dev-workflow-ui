package ch.ivyteam.workflowui.starts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

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
  private String globalFilter;
  private String projects;

  public ProcessesBean() {
    startsDataModel = new StartsDataModel();
  }

  public void applyUrlFilters() {
    if (!FacesContext.getCurrentInstance().isPostback()) {
      boolean hasUrlParams = StringUtils.isNotBlank(globalFilter) || StringUtils.isNotBlank(projects);

      if (!hasUrlParams && shouldLoadFromSession()) {
        initializeFiltersFromSession();
      } else if (hasUrlParams) {
        if (StringUtils.isNotBlank(globalFilter)) {
          startsDataModel.setGlobalFilter(globalFilter);
        }

        if (StringUtils.isNotBlank(projects)) {
          var projectList = Arrays.asList(projects.split(","));
          startsDataModel.getProjectFilterModel().setAppliedProjects(projectList);
          saveProjectFiltersToSession();
        }

        startsDataModel.applyAllFilters();
      }
    }
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
    startsDataModel.applyAllFilters();
    updateUrlWithFilters();
    saveProjectFiltersToSession();
  }

  public void resetAllFilters() {
    startsDataModel.resetAllProjectFilters();
    showProjectFilter = false;
    updateUrlWithFilters();
    clearSessionFilters();
  }

  public void applyGlobalFilter() {
    startsDataModel.applyAllFilters();
    updateUrlWithFilters();
  }

  private void updateUrlWithFilters() {
    var appliedProjects = startsDataModel.getProjectFilterModel().getAppliedProjects();
    if (appliedProjects.size() == startsDataModel.getProjectFilterModel().getAllProjects().size()) {
      appliedProjects = null;
    }

    var params = new java.util.LinkedHashMap<String, String>();
    if (StringUtils.isNotBlank(startsDataModel.getGlobalFilter())) {
      params.put("q", startsDataModel.getGlobalFilter());
    }
    if (appliedProjects != null && !appliedProjects.isEmpty()) {
      params.put("projects", String.join(",", appliedProjects));
    }

    try {
      String url = "starts.xhtml";
      if (!params.isEmpty()) {
        url += "?" + params.entrySet().stream()
            .map(e -> {
              try {
                return e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8.toString());
              } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
              }
            })
            .collect(Collectors.joining("&"));
      }
      PrimeFaces.current().executeScript("history.pushState(null, null, '" + url + "');");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void saveProjectFiltersToSession() {
    var currentAppliedProjects = startsDataModel.getProjectFilterModel().getAppliedProjects();

    if (currentAppliedProjects != null &&
        currentAppliedProjects.size() == startsDataModel.getProjectFilterModel().getAllProjects().size()) {
      currentAppliedProjects = null;
    }

    var projectSet = currentAppliedProjects != null ? new HashSet<>(currentAppliedProjects) : null;
    SessionFilters.current().save(projectSet);
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
      startsDataModel.applyAllFilters();
      updateUrlWithFilters();
    }
  }

  private void clearSessionFilters() {
    SessionFilters.current().clear();
  }

  public String getGlobalFilter() {
    var dataModelFilter = startsDataModel.getGlobalFilter();
    return StringUtils.isNotBlank(dataModelFilter) ? dataModelFilter : globalFilter;
  }

  public void setGlobalFilter(String globalFilter) {
    this.globalFilter = globalFilter;
    if (StringUtils.isNotBlank(globalFilter)) {
      startsDataModel.setGlobalFilter(globalFilter);
    } else {
      startsDataModel.setGlobalFilter(null);
    }
  }

  public String getProjects() {
    return projects;
  }

  public void setProjects(String projects) {
    this.projects = projects;
  }
}
