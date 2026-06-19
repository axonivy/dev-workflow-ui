package ch.ivyteam.workflowui.notification;

import java.io.Serializable;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import ch.ivyteam.ivy.notification.web.WebNotifications;

@ViewScoped
@Named
public class NotificationBean implements Serializable {

  private final WebNotifications webNotifications;
  private final NotificationDataModel dataModel;

  private long countAll;
  private long countUnread;

  public NotificationBean() {
    this.webNotifications = WebNotifications.current();
    this.countAll = webNotifications.countAll();
    this.countUnread = webNotifications.countUnread();
    this.dataModel = new NotificationDataModel(webNotifications);
  }

  public NotificationDataModel getDataModel() {
    return dataModel;
  }

  public void hideAll() {
    webNotifications.hideAll();
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
