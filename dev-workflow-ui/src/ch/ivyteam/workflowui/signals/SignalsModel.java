package ch.ivyteam.workflowui.signals;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import ch.ivyteam.ivy.jsf.primefaces.sort.SortMetaConverter;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.query.SignalEventQuery;

public class SignalsModel extends LazyDataModel<SignalEventModel> {

  private static final long serialVersionUID = -7194541143134204696L;

  @Override
  public int count(Map<String, FilterMeta> filterBy) {
    return 0;
  }

  @Override
  public List<SignalEventModel> load(int first, int pageSize, Map<String, SortMeta> sortBy,
      Map<String, FilterMeta> filterBy) {
    var sort = new SortMetaConverter(sortBy);
    var signalQuery = IWorkflowContext.current().signals().history().createSignalEventQuery();
    applyOrdering(signalQuery, sort.toField(), sort.toOrder());
    var signalsList = signalQuery.executor().resultsPaged().window(first, pageSize).stream()
        .map(SignalEventModel::new).toList();
    setRowCount((int) signalQuery.executor().count());
    return signalsList;
  }

  private static void applyOrdering(SignalEventQuery query, String sortField, SortOrder sortOrder) {
    if (StringUtils.isEmpty(sortField)) {
      applySorting(query.orderBy().sentTimestamp(), SortOrder.DESCENDING);
    }
    if ("signalCode".equals(sortField)) {
      applySorting(query.orderBy().signalCode(), sortOrder);
    }
    if ("sentTimestamp".equals(sortField)) {
      applySorting(query.orderBy().sentTimestamp(), sortOrder);
    }
    if ("sentByUser".equals(sortField)) {
      applySorting(query.orderBy().sentByUserName(), sortOrder);
    }
  }

  private static void applySorting(SignalEventQuery.OrderByColumnQuery query, SortOrder sortOrder) {
    if (SortOrder.ASCENDING.equals(sortOrder)) {
      query.ascending();
    }
    if (SortOrder.DESCENDING.equals(sortOrder)) {
      query.descending();
    }
  }
}
