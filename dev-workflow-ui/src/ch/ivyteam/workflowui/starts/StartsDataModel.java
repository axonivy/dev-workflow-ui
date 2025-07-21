package ch.ivyteam.workflowui.starts;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import ch.ivyteam.workflowui.util.ProcessModelsUtil;

public class StartsDataModel {

  private final List<StartableModel> allStartables;
  private String globalFilter = "";
  private final boolean isOnlySingleApplication;
  private final boolean isOnlySingleProject;
  private final ProjectFilterModel projectFilterModel;

  public StartsDataModel() {
    allStartables = ProcessModelsUtil.getDeployedStartables();
    isOnlySingleApplication = allStartables.stream().map(StartableModel::getApplicationName).distinct().count() == 1;
    isOnlySingleProject = allStartables.stream().map(StartableModel::getProjectName).distinct().count() == 1;
    var allProjects = allStartables.stream().map(StartableModel::getProjectName).distinct().sorted().toList();
    projectFilterModel = new ProjectFilterModel(allProjects);
  }

  public List<StartableModel> getStartables() {
    return allStartables.stream()
        .filter(start -> projectFilterModel.getAppliedProjects().contains(start.getProjectName()))
        .filter(this::matchesGlobalFilter)
        .toList();
  }

  private boolean matchesGlobalFilter(StartableModel start) {
    if (StringUtils.isBlank(globalFilter)) {
      return true;
    }
    return Strings.CI.contains(start.getDisplayName(), globalFilter) ||
        Strings.CI.contains(start.getDescription(), globalFilter) ||
        Strings.CI.contains(start.getApplicationName(), globalFilter) ||
        Strings.CI.contains(start.getProjectName(), globalFilter) ||
        (start.getCategory() != null && Strings.CI.contains(start.getCategory().getName(), globalFilter));
  }

  public String getGlobalFilter() {
    return globalFilter;
  }

  public void setGlobalFilter(String globalFilter) {
    this.globalFilter = globalFilter;
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

  public void resetAllProjectFilters() {
    projectFilterModel.resetAll();
    resetGlobalFilter();
  }
}
