package ch.ivyteam.workflowui;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.application.ActivityOperationState;
import ch.ivyteam.ivy.application.IProcessModelVersion;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.ivy.workflow.start.IWebStartable;

public class CustomPMV
{
  private final IWorkflowProcessModelVersion pmv;
  private final List<IWebStartable> starts;

  public static Optional<CustomPMV> create(IWorkflowProcessModelVersion pmv, String filter)
  {
    if (StringUtils.containsIgnoreCase(pmv.getName(), filter))
    {
      var startElements = pmv.getStartables(ISession.current());
      return filterEmptyStartsAndSelf(pmv, startElements);
    }
    else
    {
      var startElements = pmv.getStartables(ISession.current()).stream()
              .filter(e -> StringUtils.containsIgnoreCase(e.getLink().getRelative(), filter))
              .collect(Collectors.toList());
      return filterEmptyStartsAndSelf(pmv, startElements);
    }
  }

  private static Optional<CustomPMV> filterEmptyStartsAndSelf(IWorkflowProcessModelVersion pmv,
          List<IWebStartable> startElements)
  {
    if (startElements.isEmpty() || pmv.equals(IProcessModelVersion.current()))
    {
      return Optional.empty();
    }
    return Optional.of(new CustomPMV(pmv, startElements));
  }

  private CustomPMV(IWorkflowProcessModelVersion pmv, List<IWebStartable> startElements)
  {
    this.pmv = pmv;
    this.starts = startElements;
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

  public List<IWebStartable> getStartElements()
  {
    return this.starts;
  }
}
