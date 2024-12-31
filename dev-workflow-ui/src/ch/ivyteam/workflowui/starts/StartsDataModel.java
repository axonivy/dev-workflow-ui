package ch.ivyteam.workflowui.starts;

import java.util.List;

import ch.ivyteam.workflowui.util.ProcessModelsUtil;

public class StartsDataModel {

  private final List<StartableModel> startables;
  private String filter;
  private final boolean isOnlySingleApplication;
  private final boolean isOnlySingleProject;

  public StartsDataModel() {
    startables = ProcessModelsUtil.getDeployedStartables();
    isOnlySingleApplication = startables.stream().map(StartableModel::getApplicationName).distinct().count() == 1;
    isOnlySingleProject = startables.stream().map(StartableModel::getProjectName).distinct().count() == 1;
  }

  public List<StartableModel> getStartables() {
    return startables;
  }

  public void setFilter(String query) {
    filter = query;
  }

  public String getFilter() {
    return filter;
  }

  public boolean isOnlySingleApplication() {
    return isOnlySingleApplication;
  }

  public boolean isOnlySingleProject() {
    return isOnlySingleProject;
  }

}
