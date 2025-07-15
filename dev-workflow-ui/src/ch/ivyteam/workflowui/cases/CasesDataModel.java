package ch.ivyteam.workflowui.cases;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import ch.ivyteam.ivy.jsf.primefaces.sort.SortMetaConverter;
import ch.ivyteam.ivy.workflow.WorkflowPriority;
import ch.ivyteam.ivy.workflow.caze.CaseBusinessState;
import ch.ivyteam.ivy.workflow.query.CaseQuery;
import ch.ivyteam.workflowui.util.CaseUtil;
import ch.ivyteam.workflowui.util.PermissionsUtil;

public class CasesDataModel extends LazyDataModel<CaseModel> {

  private static final long serialVersionUID = -7707950729638849827L;
  private String filter;
  private boolean showAll = PermissionsUtil.isAdmin();

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  @Override
  public String getRowKey(CaseModel caze) {
    return caze.getUuid();
  }

  @Override
  public CaseModel getRowData(String rowKey) {
    var caze = CaseUtil.getCaseById(rowKey);
    if (caze == null) {
      return null;
    }
    return new CaseModel(caze);
  }

  @Override
  public int count(Map<String, FilterMeta> filterBy) {
    return (int) createCaseQuery().executor().count();
  }

  @Override
  public List<CaseModel> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
    var caseQuery = createCaseQuery();
    applyOrdering(caseQuery, sortBy);
    return caseQuery
        .executor()
        .resultsPaged()
        .window(first, pageSize)
        .stream()
        .map(CaseModel::new)
        .toList();
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
    if (filter != null && !filter.isEmpty()) {
      var lowerFilter = filter.toLowerCase();
      var caseState = Arrays.stream(CaseBusinessState.values())
          .filter(state -> state.toString().toLowerCase().startsWith(lowerFilter))
          .findFirst().orElse(null);
      var casePriority = Arrays.stream(WorkflowPriority.values())
          .filter(priority -> priority.toString().toLowerCase().startsWith(lowerFilter))
          .findFirst().orElse(null);

      var baseQuery = CaseQuery.create().where().name().isLikeIgnoreCase("%" + filter + "%");

      try {
        long caseId = Long.parseLong(filter);
        baseQuery = baseQuery.or().caseId().isEqual(caseId);
      } catch (NumberFormatException e) {}

      query.where().and(baseQuery
          .or().uuid().isEqual(filter)
          .or().businessState().isEqual(caseState)
          .or().priority().isEqual(casePriority)
          .or().creatorUserDisplayName().isLikeIgnoreCase("%" + filter + "%"));
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
    if ("businessState".equals(sort.getField())) {
      applySorting(query.orderBy().businessState(), sort.getOrder());
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
