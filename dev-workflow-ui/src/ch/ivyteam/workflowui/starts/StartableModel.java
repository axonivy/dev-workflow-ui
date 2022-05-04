package ch.ivyteam.workflowui.starts;

import java.util.UUID;

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
  private final WebLink viewerLink;
  private final boolean isProcessStart;
  private final String caseMapUuid;

  public StartableModel(String displayName, String description, WebLink link, Category category,
          String icon, boolean embedInFrame, WebLink viewerLink, boolean isProcessStart, String caseMapUuid) {
    this.displayName = displayName;
    this.description = description;
    this.link = link;
    this.category = category;
    this.icon = icon;
    this.embedInFrame = embedInFrame;
    this.viewerLink = viewerLink;
    this.isProcessStart = isProcessStart;
    this.caseMapUuid = caseMapUuid;
  }

  public StartableModel(IWebStartable startable) {
    this(startable.getDisplayName(),
      startable.getDescription(),
      startable.getLink(),
      startable.getCategory(),
      getIcon(startable.customFields()),
      evaluateEmbedInFrame(startable.customFields().value(CustomFieldsHelper.EMBED_IN_FRAME)),
      startable.viewerLink(),
      StringUtils.equalsAnyIgnoreCase(startable.getType(), "process-start"),
      getCaseMapUuid(startable.getLink())
    );
  }

  private static String getIcon(IStartCustomFields customFields) {
    var customIcon = customFields.value("cssIcon");
    if (StringUtils.isBlank(customIcon)) {
      return "si si-controls-play";
    }
    return customIcon;
  }

  private static String getCaseMapUuid(WebLink link) {
    String icm = StringUtils.substringAfterLast(link.getAbsolute(), "/");
    return StringUtils.substringBefore(icm, ".icm");
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

  public boolean isEmbedInFrame() {
    return embedInFrame;
  }

  public WebLink getViewerLink() {
    return viewerLink;
  }

  public String getCaseMapLink() {
    return UrlUtil.generateCaseMapUrl(UUID.fromString(getCaseMapUuid()));
  }

  public boolean isProcessStart() {
    return isProcessStart;
  }

  public void execute() {
    LastSessionStarts.current().add(this);
    if (embedInFrame) {
      RedirectUtil.redirect(UrlUtil.generateStartFrameUrl(this.getLink()));
    } else {
      RedirectUtil.redirect(link.toString());
    }
  }

  public String getViewerUrl() {
    return UrlUtil.generateProcessViewerUrl(this.viewerLink);
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

  public String getCaseMapUuid() {
    return caseMapUuid;
  }
}
