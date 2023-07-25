package ch.ivyteam.workflowui.notification;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import ch.ivyteam.ivy.notification.web.WebNotifications;

public class NotificationDataModel extends LazyDataModel<NotificationDto> {

  private final WebNotifications webNotifications;

  public NotificationDataModel(WebNotifications webNotifications) {
    this.webNotifications = webNotifications;
  }

  @Override
  public int count(Map<String, FilterMeta> filterBy) {
    return (int) webNotifications.countAll();
  }

  @Override
  public List<NotificationDto> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
    return webNotifications.read(first, pageSize).stream()
            .map(NotificationDto::new)
            .collect(Collectors.toList());
  }
}
