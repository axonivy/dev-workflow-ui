package ch.ivyteam.workflowui.notification;

import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.ivy.notification.web.WebNotifications;

@ViewScoped
@ManagedBean
public class NotificationBean {

  private final WebNotifications webNotifications;

  private long countAll;
  private long countUnread;

  public NotificationBean() {
    this.webNotifications = WebNotifications.current();
    this.countAll = webNotifications.countAll();
    this.countUnread = webNotifications.countUnread();
  }

  public List<NotificationDto> getNotifications() {
    return webNotifications.read(0, 100).stream()
            .map(NotificationDto::new)
            .collect(Collectors.toList());
  }

  public void deleteAll() {
    webNotifications.deleteAll();
    countAll = 0;
    countUnread = 0;
  }

  public void readAll() {
    webNotifications.markAllAsRead();
    countUnread = 0;
  }

  public void markAsRead(NotificationDto dto) {
    webNotifications.markAsRead(dto.getNotification());
    countUnread--;
  }

  public boolean hasNotifications() {
    return countAll != 0;
  }

  public boolean hasUnreadNotifications() {
    return countUnread != 0;
  }

  public long getUnreadNotifications() {
    return countUnread;
  }
}
