package ch.ivyteam.workflowui.intermediateEvents;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.workflow.IIntermediateEventElement;
import ch.ivyteam.workflowui.util.ProcessModelsUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.url.Page;

public class IntermediateEventElementModel {

  private final String processElementId;
  private final String name;
  private final String description;
  private final IntermediateEventInstanceLazyDataModel eventInstances;

  @SuppressWarnings("deprecation")
  public static List<IntermediateEventElementModel> create() {
    return ProcessModelsUtil.getWorkflowPMVs().stream()
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

  public IntermediateEventElementModel(IIntermediateEventElement ie) {
    this.processElementId = ie.getProcessElementId();
    this.name = ie.getName();
    this.description = ie.getDescription();
    this.eventInstances = new IntermediateEventInstanceLazyDataModel(ie);
  }

  public void redirectToElement() {
    RedirectUtil.redirect(Page.INTERMEDIATE_EVENT, Map.of("intermediateEvent", processElementId));
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

  public int getEventInstancesCount() {
    return eventInstances.getSize();
  }

  public IntermediateEventInstanceLazyDataModel getEventInstances() {
    return eventInstances;
  }
}
