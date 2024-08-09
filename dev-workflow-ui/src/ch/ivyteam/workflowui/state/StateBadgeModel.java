package ch.ivyteam.workflowui.state;

public class StateBadgeModel {

  private final String name;
  private final String cssClass;

  public StateBadgeModel(String state) {
    this.name = state.toLowerCase();
    this.cssClass = "state-" + name;
  }

  public String getName() {
    return name;
  }

  public String getCssClass() {
    return cssClass;
  }
}
