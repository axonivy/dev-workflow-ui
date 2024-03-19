package ch.ivyteam.workflowui.notification;

import ch.ivyteam.ivy.model.value.WebLink;

public record NotificationActionDTO(WebLink link, String title) {

  public String icon() {
    return "si-controls-play";
  }
}
