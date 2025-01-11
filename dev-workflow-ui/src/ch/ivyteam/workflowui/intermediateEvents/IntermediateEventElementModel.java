package ch.ivyteam.workflowui.intermediateEvents;

import java.util.List;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.workflow.IIntermediateEventElement;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.workflowui.util.ProcessModelsUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;

public class IntermediateEventElementModel {

  private final String processElementId;
  private final String name;
  private final String description;
  private final IntermediateEventModel eventModel;

  @SuppressWarnings("deprecation")
  public static List<IntermediateEventElementModel> create() {
    return getPMVs().stream()
        .flatMap(pmv -> pmv.getIntermediateEventElements().stream())
        .map(IntermediateEventElementModel::new)
        .collect(Collectors.toList());
  }

  public static IntermediateEventElementModel byProcessElementId(String processElementId) {
    return IntermediateEventElementModel.create().stream()
        .filter(ie -> ie.getProcessElementId().equals(processElementId))
        .findFirst()
        .orElse(null);
  }

  private static List<IWorkflowProcessModelVersion> getPMVs() {
    return ProcessModelsUtil.getWorkflowPMVs();
  }

  public IntermediateEventElementModel(IIntermediateEventElement ie) {
    this.processElementId = ie.getProcessElementId();
    this.name = ie.getName();
    this.description = ie.getDescription();
    this.eventModel = new IntermediateEventModel(ie);
  }

  public void redirectToElement() {
    RedirectUtil.redirect("intermediateEventDetails.xhtml?intermediateEvent=" + this.processElementId);
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getProcessElementId() {
    return processElementId;
  }

  public int getSize() {
    return getEventModel().getSize();
  }

  public IntermediateEventModel getEventModel() {
    return eventModel;
  }
}
