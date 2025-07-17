package ch.ivyteam.workflowui.starts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import ch.ivyteam.workflowui.util.ProcessModelsUtil;

public class StartsDataModel {

  private final List<StartableModel> originalStartables;
  private List<StartableModel> startables;
  private String globalFilter = "";
  private final boolean isOnlySingleApplication;
  private final boolean isOnlySingleProject;
  private final ProjectFilterModel projectFilterModel;

  public StartsDataModel() {
    originalStartables = ProcessModelsUtil.getDeployedStartables();
    startables = new ArrayList<>(originalStartables);
    isOnlySingleApplication = startables.stream().map(StartableModel::getApplicationName).distinct().count() == 1;
    isOnlySingleProject = startables.stream().map(StartableModel::getProjectName).distinct().count() == 1;
    var allProjects = startables.stream().map(StartableModel::getProjectName).distinct().sorted().toList();
    projectFilterModel = new ProjectFilterModel(allProjects);
  }

  public List<StartableModel> getStartables() {
    return startables;
  }

  public String getGlobalFilter() {
    return globalFilter;
  }

  public void setGlobalFilter(String globalFilter) {
    this.globalFilter = globalFilter;
  }

  public void applyAllFilters() {
    List<StartableModel> filtered = originalStartables.stream()
        .filter(start -> projectFilterModel.getAppliedProjects().contains(start.getProjectName()))
        .collect(Collectors.toList());

    if (StringUtils.isNotBlank(globalFilter)) {
      filtered = filtered.stream().filter(s -> Strings.CI.contains(s.getDisplayName(), globalFilter) ||
          Strings.CI.contains(s.getDescription(), globalFilter) ||
          Strings.CI.contains(s.getApplicationName(), globalFilter) ||
          Strings.CI.contains(s.getProjectName(), globalFilter) ||
          (s.getCategory() != null && Strings.CI.contains(s.getCategory().getName(), globalFilter)))
          .collect(Collectors.toList());
    }
    this.startables = filtered;
  }

  public void resetGlobalFilter() {
    this.globalFilter = "";
  }

  public boolean isOnlySingleApplication() {
    return isOnlySingleApplication;
  }

  public boolean isOnlySingleProject() {
    return isOnlySingleProject;
  }

  public ProjectFilterModel getProjectFilterModel() {
    return projectFilterModel;
  }

  public void applyProjectFilter() {
    applyAllFilters();
  }

  public void resetAllProjectFilters() {
    projectFilterModel.resetAll();
    resetGlobalFilter();
    applyAllFilters();
  }
}
