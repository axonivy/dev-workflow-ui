package ch.ivyteam.workflowui.casemap;

import ch.ivyteam.ivy.casemap.runtime.model.IStage;

public class CaseMapStageModel {
  private final String name;
  private final String iconCss;
  private final String stateCss;
  private final String stateIconCss;
  private final boolean executed;
  private final boolean terminating;
  private final boolean last;

  public CaseMapStageModel(IStage stage, int index, int runningStageIndex, int size) {
    this.name = stage.getName();
    this.iconCss = stage.getIcon().getCssClass();
    this.executed = (index <= runningStageIndex);
    terminating = stage.isTerminating();
    this.stateCss = calculateStateCss(index, runningStageIndex);
    this.stateIconCss = calculateStateIconCss(index, runningStageIndex);
    this.last = index + 1 == size;
  }

  private static String calculateStateCss(int index, int currentStageIndex) {
    if (currentStageIndex == index) {
      return "current";
    } else if (currentStageIndex > index) {
      return "visited";
    }
    return "waiting";
  }

  private static String calculateStateIconCss(int index, int currentStageIndex) {
    if (currentStageIndex == index) {
      return "si-controls-play case-state-running";
    } else if (currentStageIndex > index) {
      return "si-check-1 case-state-finished";
    }
    return "";
  }

  public String getName() {
    return name;
  }

  public String getIconCss() {
    return iconCss;
  }

  public String getStateCss() {
    return stateCss;
  }

  public String getStateIconCss() {
    return stateIconCss;
  }

  public boolean isExecuted() {
    return executed;
  }

  public String getTerminatingCss() {
    return terminating ? "none" : "visible";
  }

  public boolean isLast() {
    return last;
  }
}
