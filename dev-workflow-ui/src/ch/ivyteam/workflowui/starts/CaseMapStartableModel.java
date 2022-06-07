package ch.ivyteam.workflowui.starts;

import ch.ivyteam.ivy.application.IProcessModelVersion;
import ch.ivyteam.ivy.workflow.start.IWebStartable;

public class CaseMapStartableModel extends StartableModel {

  public CaseMapStartableModel(IWebStartable startable, IProcessModelVersion pmv) {
    super(startable);
  }

  @Override
  public boolean isProcessStart() {
    return false;
  }

}
