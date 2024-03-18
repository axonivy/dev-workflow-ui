package ch.ivyteam.workflowui.notification;

import java.util.Date;
import java.util.List;

import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.notification.web.WebNotification;
import ch.ivyteam.ivy.notification.web.WebNotificationAction;

public class NotificationDto {

  private final WebNotification notification;
  private final WebLink info;
  private final List<NotificationActionDTO> actions;

  public NotificationDto(WebNotification notification) {
    this.notification = notification;
    this.info = info();
    this.actions = actions();
  }

  public String getMessage() {
    return notification.message();
  }

  public WebLink getInfo() {
    return info;
  }

  public List<NotificationActionDTO> getActions() {
    return actions;
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

  private WebLink info() {
    var infoAction = notification.actions().stream()
            .filter(action -> action.type().equals(WebNotificationAction.Type.INFO))
            .findAny()
            .orElse(null);
    return infoAction != null ? infoAction.link() : null;
  }

  private List<NotificationActionDTO> actions() {
    return notification.actions().stream()
            .filter(action -> !action.type().equals(WebNotificationAction.Type.INFO))
            .map(action -> toNotificationActionDTO(action))
            .toList();
  }

  private NotificationActionDTO toNotificationActionDTO(WebNotificationAction action) {
    return new NotificationActionDTO(action.type(), action.link());
  }
}
