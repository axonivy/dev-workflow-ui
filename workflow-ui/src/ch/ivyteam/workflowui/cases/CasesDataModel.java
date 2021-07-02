package ch.ivyteam.workflowui.cases;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ch.ivyteam.ivy.workflow.CaseState;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.WorkflowPriority;
import ch.ivyteam.ivy.workflow.query.CaseQuery;
import ch.ivyteam.workflowui.util.UserUtil;

public class CasesDataModel extends LazyDataModel<ICase>
{

  private static final long serialVersionUID = -7707950729638849827L;
  private String filter;
  private boolean showAllCases = UserUtil.isAdmin();

  public String getFilter()
  {
    return filter;
  }

  public void setFilter(String filter)
  {
    this.filter = filter;
  }

  @Override
  public List<ICase> load(int first, int pageSize, String sortField, SortOrder sortOrder,
      Map<String, Object> filters)
  {
    var caseQuery = CaseQuery.create();

    applyFilter(caseQuery);
    applyOrdering(caseQuery, sortField, sortOrder);

    checkIfPersonalCases(caseQuery);

    List<ICase> cases = caseQuery.where()
        .isBusinessCase()
        .executor()
        .resultsPaged()
        .window(first, pageSize);
    setRowCount((int) caseQuery.executor().count());
    return cases;
  }

  private void checkIfPersonalCases(CaseQuery caseQuery)
  {
    if (!showAllCases)
    {
      caseQuery.where().currentUserIsInvolved();
    }
  }

  private void applyFilter(CaseQuery query)
  {
    if (StringUtils.isNotEmpty(filter))
    {
      var caseState = Arrays.asList(CaseState.values()).stream()
          .filter(state -> StringUtils.startsWithIgnoreCase(state.toString(), filter))
          .findFirst().orElse(null);
      var casePriority = Arrays.asList(WorkflowPriority.values()).stream()
          .filter(priority -> StringUtils.startsWithIgnoreCase(priority.toString(), filter))
          .findFirst().orElse(null);
      query.where().name().isLikeIgnoreCase("%" + filter + "%")
          .or().state().isEqual(caseState)
          .or().priority().isEqual(casePriority);
    }
  }

  private static void applyOrdering(CaseQuery query, String sortField, SortOrder sortOrder)
  {
    if (StringUtils.isEmpty(sortField))
    {
      applySorting(query.orderBy().startTimestamp(), SortOrder.DESCENDING);
    }
    if ("state".equals(sortField))
    {
      applySorting(query.orderBy().state(), sortOrder);
    }
    if ("getName(case)".equals(sortField))
    {
      applySorting(query.orderBy().name(), sortOrder);
    }
    if ("creatorUserName".equals(sortField))
    {
      applySorting(query.orderBy().creatorUserName(), sortOrder);
    }
    if ("startTimestamp".equals(sortField))
    {
      applySorting(query.orderBy().startTimestamp(), sortOrder);
    }
    if ("endTimestamp".equals(sortField))
    {
      applySorting(query.orderBy().endTimestamp(), sortOrder);
    }
  }

  private static void applySorting(CaseQuery.OrderByColumnQuery query, SortOrder sortOrder)
  {
    if (SortOrder.ASCENDING.equals(sortOrder))
    {
      query.ascending();
    }
    if (SortOrder.DESCENDING.equals(sortOrder))
    {
      query.descending();
    }
  }

  public boolean getShowAllCases()
  {
    return showAllCases;
  }

  public void setShowAllCases(boolean showAllCases)
  {
    this.showAllCases = showAllCases;
  }
}
