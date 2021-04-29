package ch.ivyteam.workflowui.starts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.application.ActivityOperationState;
import ch.ivyteam.ivy.application.IProcessModelVersion;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;

public class CustomPMV
{
  private static final String PROCESS_START = "process-start";
  private static final String CASE_MAP = "casemap";

  private final IWorkflowProcessModelVersion pmv;
  private final List<CategoryModel> categories;
  private final List<StartableModel> caseMapStarts;

  public static Optional<CustomPMV> create(IWorkflowProcessModelVersion pmv, String filter)
  {
    var categories = new ArrayList<CategoryModel>();

    Predicate<StartableModel> filterPredicate = startable -> true;
    if (!StringUtils.containsIgnoreCase(pmv.getName(), filter))
    {
      filterPredicate = startable -> StringUtils.containsIgnoreCase(startable.getLink().getRelative(), filter)
          || StringUtils.containsIgnoreCase(startable.getDisplayName(), filter);
    }

    pmv.getStartables(ISession.current()).stream().filter(e -> e.getType().equals(PROCESS_START))
        .map(StartableModel::new).filter(filterPredicate)
        .collect(Collectors.groupingBy(StartableModel::getCategory))
        .forEach((cat, starts) -> categories.add(new CategoryModel(cat, starts)));

    var casemapStartElements = pmv.getStartables(ISession.current()).stream()
        .filter(e -> e.getType().equals(CASE_MAP)).map(StartableModel::new).filter(filterPredicate)
        .collect(Collectors.toList());
    return filterEmptyStartsAndSelf(pmv, categories, casemapStartElements);
  }

  private static Optional<CustomPMV> filterEmptyStartsAndSelf(IWorkflowProcessModelVersion pmv,
      List<CategoryModel> categories, List<StartableModel> caseMaps)
  {
    if ((categories.isEmpty() && caseMaps.isEmpty()) || pmv.equals(IProcessModelVersion.current()))
    {
      return Optional.empty();
    }
    return Optional.of(new CustomPMV(pmv, categories, caseMaps));
  }

  private CustomPMV(IWorkflowProcessModelVersion pmv, List<CategoryModel> categories,
      List<StartableModel> caseMapStarts)
  {
    this.pmv = pmv;
    this.categories = categories;
    this.caseMapStarts = caseMapStarts;
  }

  public List<CategoryModel> getCategories()
  {
    Collections.sort(categories);
    return categories;
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

  public List<StartableModel> getCaseMapStarts()
  {
    return caseMapStarts;
  }
}
