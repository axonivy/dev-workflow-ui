package ch.ivyteam.workflowui.intermediateEvents;

import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import ch.ivyteam.ivy.jsf.primefaces.sort.SortMetaConverter;
import ch.ivyteam.ivy.persistence.OrderDirection;
import ch.ivyteam.ivy.workflow.IIntermediateEvent;
import ch.ivyteam.ivy.workflow.IIntermediateEventElement;
import ch.ivyteam.ivy.workflow.IntermediateEventProperty;
import ch.ivyteam.ivy.workflow.PropertyOrder;

public class IntermediateEventModel extends LazyDataModel<IIntermediateEvent> {
  private static final long serialVersionUID = -7194541143134204696L;
  private IIntermediateEventElement ie;

  public IntermediateEventModel(IIntermediateEventElement ie) {
    this.ie = ie;
  }

  @Override
  public int count(Map<String, FilterMeta> filterBy) {
    return 0;
  }

  @Override
  public List<IIntermediateEvent> load(int first, int pageSize, Map<String, SortMeta> sortBy,
          Map<String, FilterMeta> filterBy) {
    var sort = new SortMetaConverter(sortBy);
    var propertyOrder = PropertyOrder.create(getProperty(sort.toField()), getOrder(sort.toOrder()));
    var signalQuery = ie.findIntermediateEvents(null, propertyOrder, 0, -1, true);
    setRowCount(signalQuery.getResultCount());
    return signalQuery.getResultList();
  }

  public int getSize() {
    return ie.findIntermediateEvents(null, null, 0, -1, true).getResultCount();
  }

  private static IntermediateEventProperty getProperty(String sortField) {
    if ("id".equals(sortField)) {
      return IntermediateEventProperty.ID;
    }
    if ("state".equals(sortField)) {
      return IntermediateEventProperty.STATE;
    }
    if ("eventTask".equals(sortField)) {
      return IntermediateEventProperty.TASK_ID;
    }
    if ("eventTask".equals(sortField)) {
      return IntermediateEventProperty.TASK_ID;
    }
    if ("eventIdentifier".equals(sortField)) {
      return IntermediateEventProperty.EVENT_ID;
    }
    if ("timeoutTimestamp".equals(sortField)) {
      return IntermediateEventProperty.TIMEOUT_TIMESTAMP;
    }
    if ("timeoutACtion".equals(sortField)) {
      return IntermediateEventProperty.TIMEOUT_ACTION;
    }
    if ("task".equals(sortField)) {
      return IntermediateEventProperty.TASK_ID;
    }
    return IntermediateEventProperty.ID;
  }

  private OrderDirection getOrder(SortOrder sortOrder) {
    if (SortOrder.ASCENDING.equals(sortOrder)) {
      return OrderDirection.ASCENDING;
    }
    return OrderDirection.DESCENDING;
  }
}
