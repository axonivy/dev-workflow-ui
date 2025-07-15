package ch.ivyteam.workflowui.starts;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;


public class ProjectFilterModel {

  private final List<String> allProjects;
  private String projectFilter = "";
  private List<String> appliedProjects;
  private List<String> selectionsInPanel;
  private List<String> lastDisplayedProjects;

  public ProjectFilterModel(List<String> allProjects) {
    this.allProjects = allProjects;
    this.appliedProjects = new ArrayList<>(allProjects);
    this.selectionsInPanel = new ArrayList<>(appliedProjects);
  }

  public List<String> getProjects() {
    List<String> projects;
    if (StringUtils.isBlank(projectFilter)) {
      projects = allProjects;
    } else {
      projects = allProjects.stream()
          .filter(p -> Strings.CI.contains(p, projectFilter))
          .toList();
    }
    this.lastDisplayedProjects = projects;
    return projects;
  }

  public List<String> getAppliedProjects() {
    return appliedProjects;
  }

  public List<String> getTransientSelectedProjects() {
    return selectionsInPanel;
  }

  public void setTransientSelectedProjects(List<String> selectionsFromComponent) {
    if (selectionsFromComponent == null) {
      return;
    }
    var hiddenSelections = new ArrayList<>(this.selectionsInPanel);
    if (this.lastDisplayedProjects != null) {
      hiddenSelections.removeAll(this.lastDisplayedProjects);
    }

    var newSelections = new ArrayList<>(hiddenSelections);
    newSelections.addAll(selectionsFromComponent);
    this.selectionsInPanel = newSelections.stream().distinct().toList();
  }

  public void initTransientSelections() {
    this.selectionsInPanel = new ArrayList<>(this.appliedProjects);
  }

  public void applyFilter() {
    this.appliedProjects = new ArrayList<>(this.selectionsInPanel);
  }

  public void resetTransientSelections() {
    selectionsInPanel = new ArrayList<>(allProjects);
  }

  public void clearTransientSelections() {
    selectionsInPanel.clear();
  }

  public String getProjectFilter() {
    return projectFilter;
  }

  public void setProjectFilter(String projectFilter) {
    this.projectFilter = projectFilter;
  }

  public boolean isProjectFilterActive() {
    return allProjects.size() != appliedProjects.size() || StringUtils.isNotBlank(projectFilter);
  }

  public void resetAll() {
    appliedProjects = new ArrayList<>(allProjects);
    selectionsInPanel = new ArrayList<>(appliedProjects);
    projectFilter = "";
  }
}
