package ch.ivyteam.workflowui.starts;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.workflow.category.Category;
import ch.ivyteam.ivy.workflow.start.IStartCustomFields;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.util.LastSessionStarts;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.UrlUtil;
import ch.ivyteam.workflowui.util.ViewerUtil;

public class StartableModel {
  private final String displayName;
  private final String description;
  private final WebLink link;
  private final Category category;
  private final String applicationName;
  private final String projectName;
  private final String icon;
  private final boolean embedInFrame;
  private final WebLink viewerLink;
  private final boolean viewerAllowed;

  public StartableModel(String displayName, String description, WebLink link, Category category,
      String applicationName, String projectName, String icon, boolean embedInFrame, WebLink viewerLink, boolean viewerAllowed) {
    this.displayName = displayName;
    this.description = description;
    this.link = link;
    this.category = category;
    this.applicationName = applicationName;
    this.projectName = projectName;
    this.icon = icon;
    this.embedInFrame = embedInFrame;
    this.viewerLink = viewerLink;
    this.viewerAllowed = viewerAllowed;
  }

  public StartableModel(IWebStartable startable) {
    this(startable.getDisplayName(),
        startable.getDescription(),
        startable.getLink(),
        startable.getCategory(),
        startable.pmv().getApplication().getName(),
        startable.pmv().project().name(),
        getIcon(startable.customFields()),
        evaluateEmbedInFrame(startable.customFields().value(CustomFieldsHelper.EMBED_IN_FRAME)),
        ViewerUtil.getViewerLink(startable),
        ViewerUtil.isViewerAllowed(startable));
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
    return !Strings.CS.contains(value, "false");
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

  public String getApplicationName() {
    return applicationName;
  }

  public String getProjectName() {
    return projectName;
  }

  public boolean isEmbedInFrame() {
    return embedInFrame;
  }

  public WebLink getViewerLink() {
    return viewerLink;
  }

  public boolean isViewerAllowed() {
    return viewerAllowed;
  }

  public boolean isProcessStart() {
    return true;
  }

  public void execute() {
    LastSessionStarts.current().add(this);
    if (embedInFrame) {
      RedirectUtil.redirect(UrlUtil.generateStartFrameUrl(this.getLink(), "home"));
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
