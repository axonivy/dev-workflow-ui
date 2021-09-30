package ch.ivyteam.workflowui.starts;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.workflow.category.Category;
import ch.ivyteam.ivy.workflow.start.IStartCustomFields;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.UrlUtil;

public class StartableModel {
  private final String displayName;
  private final String description;
  private final WebLink link;
  private final Category category;
  private final String executeLink;
  private final String icon;

  public StartableModel(IWebStartable startable) {
    this.displayName = startable.getDisplayName();
    this.description = startable.getDescription();
    this.link = startable.getLink();
    this.category = startable.getCategory();
    this.executeLink = UrlUtil.generateStartFrameUrl(link);
    this.icon = getIcon(startable.customFields());
  }

  private String getIcon(IStartCustomFields customFields) {
    var customIcon = customFields.value("cssIcon");
    if (StringUtils.isBlank(customIcon)) {
      return "si si-controls-play";
    }
    return customIcon;
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

  public String getExecuteLink() {
    return executeLink;
  }

  public void execute() {
    RedirectUtil.redirect(executeLink);
  }

  public String getIcon() {
    return icon;
  }
}