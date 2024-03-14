package ch.ivyteam.workflowui.notification;

import java.util.Date;

import ch.ivyteam.ivy.notification.web.WebNotification;
import ch.ivyteam.ivy.notification.web.WebNotificationAction;

public class NotificationDto {

  private final WebNotification notification;

  public NotificationDto(WebNotification notification) {
    this.notification = notification;
  }

  public String getMessage() {
    return notification.message();
  }

  public WebNotificationAction getInfoAction() {
    return notification.actions().stream()
            .filter(action -> action.type().equals(WebNotificationAction.Type.INFO))
            .findAny()
            .orElse(null);
  }

  public WebNotificationAction getRunAction() {
    return notification.actions().stream()
            .filter(action -> action.type().equals(WebNotificationAction.Type.RUN))
            .findAny()
            .orElse(null);
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
