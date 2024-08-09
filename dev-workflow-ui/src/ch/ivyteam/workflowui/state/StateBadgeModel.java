package ch.ivyteam.workflowui.state;

public class StateBadgeModel {
  private final String cssClass;
  private final String name;


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
