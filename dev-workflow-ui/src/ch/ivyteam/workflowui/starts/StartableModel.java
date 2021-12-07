package ch.ivyteam.workflowui.starts;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.workflow.category.Category;
import ch.ivyteam.ivy.workflow.start.IStartCustomFields;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.util.LastSessionStarts;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.UrlUtil;

public class StartableModel {
  private final String displayName;
  private final String description;
  private final WebLink link;
  private final Category category;
  private final String icon;
  private boolean embedInFrame;

  public StartableModel(String displayName, String description, WebLink link, Category category,
          String icon, boolean embedInFrame) {
    this.displayName = displayName;
    this.description = description;
    this.link = link;
    this.category = category;
    this.icon = icon;
    this.embedInFrame = embedInFrame;
  }

  public StartableModel(IWebStartable startable) {
    this(startable.getDisplayName(),
      startable.getDescription(),
      startable.getLink(),
      startable.getCategory(),
      getIcon(startable.customFields()),
      evaluateEmbedInFrame(startable.customFields().value("embedInFrame"))
    );
  }

  private static String getIcon(IStartCustomFields customFields) {
    var customIcon = customFields.value("cssIcon");
    if (StringUtils.isBlank(customIcon)) {
      return "si si-controls-play";
    }
    return customIcon;
  }

  public static boolean evaluateEmbedInFrame(String value) {
    // default is true
    return !StringUtils.contains(value, "false");
  }

  public String getDescription() {
    return description;
  }

  public String getDisplayName() {
    return displayName;
  }

  public WebLink getLink() {
    return link;
  }

  public Category getCategory() {
    return category;
  }

  public void execute() {
    LastSessionStarts.current().add(this);
    if (embedInFrame) {
      RedirectUtil.redirect(UrlUtil.generateStartFrameUrl(this.getLink()));
    } else {
      RedirectUtil.redirect(link.toString());
    }
  }

  public String getIcon() {
    return icon;
  }

  @Override
  public int hashCode() {
    return link.hashCode();
  }

  @Override
  public boolean equals(Object otherObject) {
    if (this == otherObject) {
      return true;
    }
    if (otherObject == null) {
      return false;
    }
    if (getClass() != otherObject.getClass()) {
      return false;
    }
    StartableModel otherStartable = (StartableModel) otherObject;
    return this.link.equals(otherStartable.link);
  }
}
