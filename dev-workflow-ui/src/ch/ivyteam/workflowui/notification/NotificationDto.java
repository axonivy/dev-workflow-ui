package ch.ivyteam.workflowui.notification;

import java.util.Date;

import ch.ivyteam.ivy.notification.web.WebNotification;

public class NotificationDto {

  private final WebNotification notification;

  public NotificationDto(WebNotification notification) {
    this.notification = notification;
  }

  public String getMessage() {
    return notification.message();
  }

  public Date getCreatedAt() {
    return Date.from(notification.createdAt());
  }

  public String getStyle() {
    if (notification.isRead()) {
      return "";
    }
    return "p-text-bold";
  }

  public WebNotification getNotification() {
    return notification;
  }

  public boolean isRead() {
    return notification.isRead();
  }
}
