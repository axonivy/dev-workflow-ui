package ch.ivyteam.workflowui.notification;

import java.util.Date;

import ch.ivyteam.ivy.notification.web.WebNotification;
import ch.ivyteam.ivy.notification.web.WebNotificationAction;

public class NotificationDto {

  private final WebNotification notification;
  private final NotificationActionDTO info;
  private final NotificationActionDTO run;

  public NotificationDto(WebNotification notification) {
    this.notification = notification;
    this.info = toNotificationActionDTO(notification.info());
    this.run = toNotificationActionDTO(notification.run());
  }

  public String getMessage() {
    return notification.message();
  }

  public NotificationActionDTO getInfoAction() {
    return info;
  }

  public NotificationActionDTO getRunAction() {
    return run;
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

  private static NotificationActionDTO toNotificationActionDTO(WebNotificationAction action) {
    return new NotificationActionDTO(action.link(), action.title());
  }
}
