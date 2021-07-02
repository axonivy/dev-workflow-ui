package ch.ivyteam.workflowui.signals;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.ivy.workflow.query.TaskBoundarySignalEventReceiverQuery;
import ch.ivyteam.ivy.workflow.signal.ITaskBoundarySignalEventReceiver;

public class BoundarySignalModel extends LazyDataModel<ITaskBoundarySignalEventReceiver>
{

  private static final long serialVersionUID = -7194541143134204696L;

  @Override
  public List<ITaskBoundarySignalEventReceiver> load(int first, int pageSize, String sortField,
      SortOrder sortOrder, Map<String, Object> filters)
  {

    var taskBoundaryQuery = IWorkflowContext.current().signals().receivers().createTaskBoundaryQuery();
    applyOrdering(taskBoundaryQuery, sortField, sortOrder);
    var taskBoundaryList = taskBoundaryQuery.executor().resultsPaged().window(first, pageSize);
    setRowCount((int) taskBoundaryQuery.executor().count());
    return taskBoundaryList;
  }

  private static void applyOrdering(TaskBoundarySignalEventReceiverQuery query, String sortField,
      SortOrder sortOrder)
  {
    if (StringUtils.isEmpty(sortField))
    {
      return;
    }
    if ("signalCodePattern".equals(sortField))
    {
      applySorting(query.orderBy().signalCodePattern(), sortOrder);
    }
    if ("waitingTask".equals(sortField))
    {
      applySorting(query.orderBy().waitingTaskId(), sortOrder);
    }
  }

  private static void applySorting(TaskBoundarySignalEventReceiverQuery.OrderByColumnQuery query,
      SortOrder sortOrder)
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
