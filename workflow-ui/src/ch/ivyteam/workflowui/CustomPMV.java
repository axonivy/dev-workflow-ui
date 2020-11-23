package ch.ivyteam.workflowui;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.application.ActivityOperationState;
import ch.ivyteam.ivy.workflow.IStartElement;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;

public class CustomPMV
{
  private final IWorkflowProcessModelVersion pmv;
  private final List<IStartElement> startElements;

  public static Optional<CustomPMV> create(IWorkflowProcessModelVersion pmv, String filter)
  {
    if (StringUtils.containsIgnoreCase(pmv.getName(), filter))
    {
      var startElements = pmv.getStartElements()
              .stream().filter(e -> (e.isVisible())).collect(Collectors.toList());
      return filterNoStarts(pmv, startElements);
    }
    else
    {
      var startElements = pmv.getStartElements().stream()
              .filter(e -> (e.isVisible() &&  StringUtils.containsIgnoreCase(e.getUserFriendlyRequestPath(), filter)))
              .collect(Collectors.toList());
      return filterNoStarts(pmv, startElements);
    }
  }

  private static Optional<CustomPMV> filterNoStarts(IWorkflowProcessModelVersion pmv,
          List<IStartElement> startElements)
  {
    if (startElements.isEmpty())
    {
      return Optional.empty();
    }
    else
    {
      return Optional.of(new CustomPMV(pmv, startElements));
    }
  }

  private CustomPMV(IWorkflowProcessModelVersion pmv, List<IStartElement> startElements)
  {
    this.pmv = pmv;
    this.startElements = startElements;
  }

  public IWorkflowProcessModelVersion getPMV()
  {
    return this.pmv;
  }

  public String getStateIcon()
  {
    if (isActive())
    {
      return "check-circle-1";
    }
    if (getState() == ActivityOperationState.INACTIVE)
    {
      return "remove-circle";
    }
    return "question-circle";
  }

  public boolean isActive()
  {
    return getState() == ActivityOperationState.ACTIVE;
  }

  public ActivityOperationState getState()
  {
    return pmv.getActivityOperationState();
  }

  public String getProjectName()
  {
    return pmv.getProcessModel().getName();
  }

  public String getVersionName()
  {
    return pmv.getVersionName();
  }

  public List<IStartElement> getStartElements()
  {
    return this.startElements;
  }
}
