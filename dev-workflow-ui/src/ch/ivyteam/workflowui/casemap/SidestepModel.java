package ch.ivyteam.workflowui.casemap;

import ch.ivyteam.ivy.casemap.runtime.model.IStartableSideStep;
import ch.ivyteam.ivy.model.value.WebLink;

public class SidestepModel {

  private final String name;
  private final WebLink startLink;

  public SidestepModel(IStartableSideStep sidestep) {
    this.name = sidestep.getName();
    this.startLink = sidestep.getStartLink();
  }

  public String getName() {
    return name;
  }

  public WebLink getStartLink() {
    return startLink;
  }

}
