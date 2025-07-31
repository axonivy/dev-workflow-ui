package ch.ivyteam.workflowui.starts;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import ch.ivyteam.workflowui.util.ProcessModelsUtil;

public class StartsDataModel {

  private final List<StartableModel> allStartables;
  private List<StartableModel> filteredStartables;
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
    return allStartables;
  }

  public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
    var filterText = (filter == null) ? "" : filter.toString().trim().toLowerCase();
    if (filterText.isBlank()) {
      return true;
    }
    var start = (StartableModel) value;
    return Stream.of(start.getDisplayName(),
        start.getDescription(),
        start.getApplicationName(),
        start.getProjectName(),
        (start.getCategory() != null ? start.getCategory().getName() : null))
        .filter(Objects::nonNull)
        .map(String::toLowerCase)
        .anyMatch(text -> text.contains(filterText));
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

  public List<StartableModel> getFilteredStartables() {
    return filteredStartables;
  }

  public void setFilteredStartables(List<StartableModel> filteredStartables) {
    this.filteredStartables = filteredStartables;
  }
}
