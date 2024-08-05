package ch.ivyteam.workflowui.cases;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import ch.ivyteam.ivy.jsf.primefaces.sort.SortMetaConverter;
import ch.ivyteam.ivy.workflow.CaseState;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.WorkflowPriority;
import ch.ivyteam.ivy.workflow.query.CaseQuery;
import ch.ivyteam.workflowui.util.CaseUtil;
import ch.ivyteam.workflowui.util.UserUtil;

public class CasesDataModel extends LazyDataModel<ICase> {

  private static final long serialVersionUID = -7707950729638849827L;
  private String filter;
  private boolean showAll = UserUtil.isAdmin();

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  @Override
  public String getRowKey(ICase caze) {
    return caze.uuid();
  }

  @Override
  public ICase getRowData(String rowKey) {
    return CaseUtil.getCaseById(rowKey);
  }

  @Override
  public int count(Map<String, FilterMeta> filterBy) {
    return (int)createCaseQuery().executor().count();
  }

  @Override
  public List<ICase> load(int first, int pageSize, Map<String, SortMeta> sortBy,
          Map<String, FilterMeta> filterBy) {
    var caseQuery = createCaseQuery();
    applyOrdering(caseQuery, sortBy);
    List<ICase> cases = caseQuery
            .executor()
            .resultsPaged()
            .window(first, pageSize);
    return cases;
  }

  protected CaseQuery createCaseQuery() {
    var caseQuery = CaseQuery.create();

    applyFilter(caseQuery);
    checkIfShowAll(caseQuery);

    caseQuery.where().and(CaseQuery.create().where().isBusinessCase());
    return caseQuery;
  }

  private void checkIfShowAll(CaseQuery caseQuery) {
    if (!showAll) {
      caseQuery.where().and(CaseQuery.create().where().currentUserIsInvolved());
    }
  }

  private void applyFilter(CaseQuery query) {
    if (StringUtils.isNotEmpty(filter)) {
      var caseState = Arrays.asList(CaseState.values()).stream()
              .filter(state -> StringUtils.startsWithIgnoreCase(state.toString(), filter))
              .findFirst().orElse(null);
      var casePriority = Arrays.asList(WorkflowPriority.values()).stream()
              .filter(priority -> StringUtils.startsWithIgnoreCase(priority.toString(), filter))
              .findFirst().orElse(null);
      query.where().and(CaseQuery.create().where().name().isLikeIgnoreCase("%" + filter + "%")
              .or().state().isEqual(caseState)
              .or().priority().isEqual(casePriority));
    }
  }

  private static void applyOrdering(CaseQuery query, Map<String, SortMeta> sortBy) {
    var sorts = new SortMetaConverter(sortBy).toList();
    if (sorts.isEmpty()) {
      applySorting(query.orderBy().startTimestamp(), SortOrder.DESCENDING);
      return;
    }
    for (SortMeta meta : sortBy.values()) {
      applyOrdering(query, meta);
    }
  }

  private static void applyOrdering(CaseQuery query, SortMeta sort) {
    if ("state".equals(sort.getField())) {
      applySorting(query.orderBy().state(), sort.getOrder());
    }
    if ("name".equals(sort.getField())) {
      applySorting(query.orderBy().name(), sort.getOrder());
    }
    if ("creatorUserName".equals(sort.getField())) {
      applySorting(query.orderBy().creatorUserName(), sort.getOrder());
    }
    if ("startTimestamp".equals(sort.getField())) {
      applySorting(query.orderBy().startTimestamp(), sort.getOrder());
    }
    if ("endTimestamp".equals(sort.getField())) {
      applySorting(query.orderBy().endTimestamp(), sort.getOrder());
    }
  }

  private static void applySorting(CaseQuery.OrderByColumnQuery query, SortOrder sortOrder) {
    if (SortOrder.ASCENDING.equals(sortOrder)) {
      query.ascending();
    }
    if (SortOrder.DESCENDING.equals(sortOrder)) {
      query.descending();
    }
  }

  public boolean getShowAll() {
    return showAll;
  }

  public void setShowAll(boolean showAll) {
    this.showAll = showAll;
  }
}
