package ch.ivyteam.workflowui;

import ch.ivyteam.ivy.security.ISecurityMember;

public class UserComponentModel
{
  private final String name;
  private final String cssIcon;

  public UserComponentModel(ISecurityMember user)
  {
    this.name = user.getDisplayName();
    this.cssIcon = getCssIcon(user);
  }

  private static String getCssIcon(ISecurityMember user)
  {
    if (user.isUser())
    {
      return "si si-single-neutral-circle";
    }
    return "si si-cog";
  }

  public String getName()
  {
    return name;
  }

  public String getCssIcon()
  {
    return cssIcon;
  }

}
