package ch.ivyteam.workflowui.intermediateEvents;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import ch.ivyteam.ivy.jsf.primefaces.sort.SortMetaConverter;
import ch.ivyteam.ivy.workflow.IIntermediateEvent;
import ch.ivyteam.ivy.workflow.IIntermediateEventElement;

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
  public List<IIntermediateEvent> load(int first, int pageSize, Map<String, SortMeta> sortBy,  Map<String, FilterMeta> filterBy) {
    var sort = new SortMetaConverter(sortBy);
    var events = ie.getIntermediateEvents();
    setRowCount(events.size());
    var comp = sort(sort.toField(), sort.toOrder());
    return events.stream()
            .sorted(comp)
            .collect(Collectors.toList());
  }

  public int getSize() {
    return ie.getIntermediateEvents().size();
  }

  private static Comparator<IIntermediateEvent> sort(String sortField, SortOrder order) {
    var comp = Comparator.comparing(IIntermediateEvent::getId);
    if ("id".equals(sortField)) {
      comp = Comparator.comparing(IIntermediateEvent::getId);
    }
    if ("state".equals(sortField)) {
      comp = Comparator.comparing(IIntermediateEvent::getState);
    }
    if ("eventTask".equals(sortField)) {
      comp = Comparator.comparing(i -> i.getTaskStart().getId());
    }
    if ("eventTask".equals(sortField)) {
      comp = Comparator.comparing(i -> i.getTaskStart().getId());
    }
    if ("eventIdentifier".equals(sortField)) {
      comp = Comparator.comparing(IIntermediateEvent::getEventIdentifier);
    }
    if ("timeoutTimestamp".equals(sortField)) {
      comp = Comparator.comparing(IIntermediateEvent::getTimeoutTimestamp);
    }
    if ("timeoutAction".equals(sortField)) {
      comp = Comparator.comparing(IIntermediateEvent::getTimeoutAction);
    }
    if ("task".equals(sortField)) {
      comp = Comparator.comparing(i -> i.getTaskStart().getId());
    }
    if (order == SortOrder.DESCENDING) {
      comp = comp.reversed();
    }
    return comp;
  }
}
