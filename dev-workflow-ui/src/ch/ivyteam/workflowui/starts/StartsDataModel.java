package ch.ivyteam.workflowui.starts;

import java.util.List;
import java.util.stream.Collectors;

import ch.ivyteam.workflowui.util.ProcessModelsUtil;

public class StartsDataModel {

  private List<StartableModel> startables;
  private List<StartableModel> filteredStartables;
  private String filter;
  private final boolean isOnlySingleApplication;
  private final boolean isOnlySingleProject;

  public StartsDataModel() {
    startables = ProcessModelsUtil.getDeployedStartables();
    isOnlySingleApplication = startables.stream().map(s -> s.getApplicationName()).distinct().count() == 1;
    isOnlySingleProject = startables.stream().map(s -> s.getProjectName()).distinct().count() == 1;
  }

  public List<StartableModel> getStartables() {
    if (filter != null && !filter.isEmpty()) {
      return filteredStartables;
    }
    return startables;
  }

  public void setFilter(String query) {
    filter = query;
    filteredStartables = startables.stream()
            .filter(s -> s.getDisplayName().toLowerCase().contains(filter.toLowerCase())
                    || s.getDescription().toLowerCase().contains(filter.toLowerCase())
                    || s.getCategory().getName().toLowerCase().contains(filter.toLowerCase()))
            .collect(Collectors.toList());
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
