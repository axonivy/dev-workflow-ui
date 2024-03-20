package ch.ivyteam.workflowui.notification;

import ch.ivyteam.ivy.model.value.WebLink;

public class NotificationActionDTO {

  private final WebLink link;
  private final String title;

  public NotificationActionDTO(WebLink link, String title) {
    this.link = link;
    this.title = title;
  }

  public WebLink getLink() {
    return link;
  }

  public String getTitle() {
    return title;
  }

  public String icon() {
    return "si-controls-play";
  }
}
