package ch.ivyteam.workflowui.starts;

import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.workflow.category.Category;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.util.RedirectUtil;

public class StartableModel
{
  private final String displayName;
  private final String description;
  private final WebLink link;
  private final Category category;

  public StartableModel(IWebStartable startable)
  {
    this.displayName = startable.getDisplayName();
    this.description = startable.getDescription();
    this.link = startable.getLink();
    this.category = startable.getCategory();
  }

  public String getDescription()
  {
    return description;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public WebLink getLink()
  {
    return link;
  }

  public Category getCategory()
  {
    return category;
  }

  public void execute()
  {
    RedirectUtil.redirect("frame.xhtml?taskUrl=" + getLink());
  }
}
