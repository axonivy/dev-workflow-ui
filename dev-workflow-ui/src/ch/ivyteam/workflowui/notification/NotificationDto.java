package ch.ivyteam.workflowui.notification;

import java.util.Date;

import ch.ivyteam.ivy.notification.web.WebNotification;
import ch.ivyteam.ivy.notification.web.impl.BusinessWebNotification;
import ch.ivyteam.ivy.notification.web.impl.NewTaskWebNotification;

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

  // TODO: improve mapping
  public String getInfoLink() {
    return switch (notification.kind()) {
      case "new-task" -> ((NewTaskWebNotification) notification).task().getDetailLink().getRelativeEncoded();
      case "business" -> {
        var caze = ((BusinessWebNotification) notification).caze();
        yield caze == null ? null : caze.getDetailLink().getRelativeEncoded();
      }
      default -> throw new IllegalArgumentException("Unexpected value: " + notification);
    };
  }
  public String getRunLink() {
    return switch (notification.kind()) {
      case "new-task" -> ((NewTaskWebNotification) notification).task().getStartLinkEmbedded().getRelativeEncoded();
      case "business" -> null;
      default -> throw new IllegalArgumentException("Unexpected value: " + notification);
    };
  }
  public boolean isRunnable() {
    return switch (notification.kind()) {
      case "new-task" -> ((NewTaskWebNotification) notification).task().getBusinessState().intValue() == 0;
      case "business" -> false;
      default -> throw new IllegalArgumentException("Unexpected value: " + notification);
    };
  }
}
