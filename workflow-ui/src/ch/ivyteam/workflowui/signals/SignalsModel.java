package ch.ivyteam.workflowui.signals;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.query.SignalEventQuery;
import ch.ivyteam.ivy.workflow.signal.ISignalEvent;

public class SignalsModel extends LazyDataModel<ISignalEvent>
{

  private static final long serialVersionUID = -7194541143134204696L;

  @Override
  public List<ISignalEvent> load(int first, int pageSize, String sortField, SortOrder sortOrder,
      Map<String, Object> filters)
  {
    var signalQuery = IWorkflowContext.current().signals().history().createSignalEventQuery();
    applyOrdering(signalQuery, sortField, sortOrder);
    var signalsList = signalQuery.executor().resultsPaged().window(first, pageSize);
    setRowCount((int) signalQuery.executor().count());
    return signalsList;
  }

  private static void applyOrdering(SignalEventQuery query, String sortField, SortOrder sortOrder)
  {
    if (StringUtils.isEmpty(sortField))
    {
      applySorting(query.orderBy().sentTimestamp(), SortOrder.DESCENDING);
    }
    if ("signalCode".equals(sortField))
    {
      applySorting(query.orderBy().signalCode(), sortOrder);
    }
    if ("sentTimestamp".equals(sortField))
    {
      applySorting(query.orderBy().sentTimestamp(), sortOrder);
    }
    if ("sentByUser".equals(sortField))
    {
      applySorting(query.orderBy().sentByUserName(), sortOrder);
    }
  }

  private static void applySorting(SignalEventQuery.OrderByColumnQuery query, SortOrder sortOrder)
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
}
