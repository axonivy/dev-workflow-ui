package ch.ivyteam.workflowui.user;

import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.ivy.workflow.task.IActivator;

public class UserComponentModel {
  private final String name;
  private final String cssIcon;

  public UserComponentModel(ISecurityMember user) {
    this.name = user.getDisplayName();
    this.cssIcon = getCssIcon(user);
  }

  public UserComponentModel(IActivator user) {
    this.name = user.displayName();
    this.cssIcon = getCssIcon(user.get());
  }

  private static String getCssIcon(ISecurityMember user) {
    if (user == null || user.isUser()) {
      return "si si-single-neutral-circle";
    }
    return "si si-multiple-neutral-1";
  }

  public String getName() {
    return name;
  }

  public String getCssIcon() {
    return cssIcon;
  }

}
