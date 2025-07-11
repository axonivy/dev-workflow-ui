package ch.ivyteam.workflowui.starts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    this.startables = originalStartables.stream()
        .filter(start -> this.projectFilterModel.getAppliedProjects().contains(start.getProjectName()))
        .collect(Collectors.toList());
  }

  public void resetAllProjectFilters() {
    projectFilterModel.resetAll();
    applyProjectFilter();
  }
}
