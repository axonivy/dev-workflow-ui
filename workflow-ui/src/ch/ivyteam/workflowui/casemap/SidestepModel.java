package ch.ivyteam.workflowui.casemap;

import ch.ivyteam.ivy.casemap.runtime.model.IStartableSideStep;

public class SidestepModel {

  private final String name;
  private final String startLink;

  public SidestepModel(IStartableSideStep sidestep) {
    this.name = sidestep.getName();
    this.startLink = sidestep.getStartLink().getRelative();
  }

  public String getName() {
    return name;
  }

  public String getStartLink() {
    return startLink;
  }

}
