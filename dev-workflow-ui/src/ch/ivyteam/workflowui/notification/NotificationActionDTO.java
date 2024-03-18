package ch.ivyteam.workflowui.notification;

import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.notification.web.WebNotificationAction;

public record NotificationActionDTO(WebNotificationAction.Type type, WebLink link) {
  public String icon() {
    return "si-controls-play";
  }

  public String tooltip() {
    return "Run";
  }
}
