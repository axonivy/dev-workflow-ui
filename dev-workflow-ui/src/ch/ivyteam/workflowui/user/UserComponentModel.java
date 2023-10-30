package ch.ivyteam.workflowui.user;

import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.ivy.workflow.task.IActivator;

public class UserComponentModel {

  private final String name;
  private final String cssIcon;

  public UserComponentModel(ISecurityMember activator) {
    this.name = activator.getDisplayName();
    this.cssIcon = getCssIcon(activator);
  }

  public UserComponentModel(IActivator activator) {
    var securityMember = activator.get();
    if (securityMember == null) {
      this.name = activator.name();
    } else {
      this.name = securityMember.getDisplayName();
    }
    this.cssIcon = getCssIcon(securityMember);
  }

  private static String getCssIcon(ISecurityMember activator) {
    if (activator == null) {
      return "si si-question-circle";
    }
    if (activator.isUser()) {
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
