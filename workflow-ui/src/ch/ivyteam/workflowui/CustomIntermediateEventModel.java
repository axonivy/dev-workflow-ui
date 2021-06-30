package ch.ivyteam.workflowui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.workflow.IIntermediateEvent;
import ch.ivyteam.ivy.workflow.IIntermediateEventElement;
import ch.ivyteam.ivy.workflow.IPropertyFilter;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.ivy.workflow.IntermediateEventProperty;
import ch.ivyteam.ivy.workflow.IntermediateEventState;
import ch.ivyteam.logicalexpression.RelationalOperator;
import ch.ivyteam.workflowui.util.RedirectUtil;

public class CustomIntermediateEventModel
{
  private final String processElementId;
  private final String name;
  private final String description;
  private final List<IIntermediateEvent> events;

  public static List<CustomIntermediateEventModel> create()
  {
    var pmvs = getPMVs();
    List<CustomIntermediateEventModel> ieList = new ArrayList<>();

    for (IWorkflowProcessModelVersion pmv : pmvs)
    {
      var filter = pmv.getWorkflowContext().createIntermediateEventPropertyFilter(
          IntermediateEventProperty.STATE, RelationalOperator.EQUAL,
          IntermediateEventState.WAITING.intValue());
      for (IIntermediateEventElement bean : pmv.getIntermediateEventElements())
      {
        ieList.add(new CustomIntermediateEventModel(bean, filter));
      }
    }
    return ieList;
  }

  public static CustomIntermediateEventModel byProcessElementId(String processElementId)
  {
    return CustomIntermediateEventModel.create().stream()
        .filter(ie -> ie.getProcessElementId().equals(processElementId)).findFirst().orElse(null);
  }

  private static List<IWorkflowProcessModelVersion> getPMVs()
  {
    return IApplication.current().getProcessModels().stream()
        .flatMap(pm -> pm.getProcessModelVersions().stream()).map(IWorkflowProcessModelVersion::of)
        .collect(Collectors.toList());
  }

  public CustomIntermediateEventModel(IIntermediateEventElement ie,
      IPropertyFilter<IntermediateEventProperty> filter)
  {
    this.processElementId = ie.getProcessElementId();
    this.name = ie.getName();
    this.description = ie.getDescription();
    this.events = ie.findIntermediateEvents(filter, null, 0, -1, true).getResultList();
  }

  public void redirectToElement()
  {
    RedirectUtil.redirect("intermediateEventDetails.xhtml?intermediateEvent=" + this.processElementId);
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

  public List<IIntermediateEvent> getEvents()
  {
    return events;
  }

  public String getProcessElementId()
  {
    return processElementId;
  }

  public int getSize()
  {
    return this.events.size();
  }

}
