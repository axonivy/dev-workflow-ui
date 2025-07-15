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

public class IntermediateEventInstanceLazyDataModel extends LazyDataModel<IntermediateEventInstance> {

  private static final long serialVersionUID = -7194541143134204696L;
  private final IIntermediateEventElement ie;

  public IntermediateEventInstanceLazyDataModel(IIntermediateEventElement ie) {
    this.ie = ie;
  }

  @Override
  public int count(Map<String, FilterMeta> filterBy) {
    return 0;
  }

  @Override
  public List<IntermediateEventInstance> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
    var sort = new SortMetaConverter(sortBy);
    var events = ie.getIntermediateEvents();
    setRowCount(events.size());

    var comp = comparator(sort.toField());
    if (sort.toOrder() == SortOrder.DESCENDING) {
      comp = comp.reversed();
    }

    return events.stream()
        .sorted(comp)
        .map(IntermediateEventInstance::new)
        .collect(Collectors.toList());
  }

  public int getSize() {
    return ie.getIntermediateEvents().size();
  }

  private static Comparator<IIntermediateEvent> comparator(String sortField) {
    return switch (sortField) {
      case "state" -> Comparator.comparing(IIntermediateEvent::getState);
      case "eventTask", "task" -> Comparator.comparing(i -> i.getTaskStart().getId());
      case "eventIdentifier" -> Comparator.comparing(IIntermediateEvent::getEventIdentifier);
      case "timeoutTimestamp" -> Comparator.comparing(IIntermediateEvent::getTimeoutTimestamp);
      case "timeoutAction" -> Comparator.comparing(IIntermediateEvent::getTimeoutAction);
      default -> Comparator.comparing(IIntermediateEvent::getId);
    };
  }
}
