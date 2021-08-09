package ch.ivyteam.workflowui.starts;

import java.util.List;

import ch.ivyteam.ivy.workflow.category.Category;

public class CategoryModel implements Comparable<CategoryModel> {
  private final String name;
  private final String cssIcon;
  private final String tooltip;
  private final List<StartableModel> starts;

  public CategoryModel(Category category, List<StartableModel> starts) {
    this.name = category.getName();
    this.cssIcon = category.getLocalizedText("cssIcon");
    this.tooltip = category.getLocalizedText("tooltip");
    this.starts = starts;
  }

  @Override
  public int compareTo(CategoryModel category) {
    return this.name.compareTo(category.getName());
  }

  public List<StartableModel> getStarts() {
    return starts;
  }

  public String getCssIcon() {
    return cssIcon;
  }

  public String getTooltip() {
    return tooltip;
  }

  public String getName() {
    return name;
  }
}
