package ch.ivyteam.workflowui.user;

import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.ivy.workflow.task.IActivator;
import ch.ivyteam.workflowui.util.PermissionsUtil;

public class UserComponentModel {

  private final String name;
  private final String cssIcon;
  private final String userSecurityMemberId;
  private final boolean hasUserDetailLink;

  public UserComponentModel(ISecurityMember activator) {
    this.name = activator.getDisplayName();
    this.cssIcon = getCssIcon(activator);
    this.userSecurityMemberId = getUserSecurityMemberId(activator);
    this.hasUserDetailLink = setHasUserDetailLink();
  }

  public UserComponentModel(IActivator activator) {
    var securityMember = activator.get();
    if (securityMember == null) {
      this.name = activator.name();
    } else {
      this.name = securityMember.getDisplayName();
    }
    this.cssIcon = getCssIcon(securityMember);
    this.userSecurityMemberId = getUserSecurityMemberId(securityMember);
    this.hasUserDetailLink = setHasUserDetailLink();
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

  public String getUserSecurityMemberId(ISecurityMember activator) {
    if (activator == null) {
      return null;
    }
    if (activator.isUser()) {
      return activator.getSecurityMemberId();
    }
    return null;
  }

  public String getCssIcon() {
    return cssIcon;
  }

  public String getSecurityMemberId() {
    return userSecurityMemberId;
  }

  private boolean setHasUserDetailLink() {
    return PermissionsUtil.isAdmin() && userSecurityMemberId != null;
  }

  public boolean getHasUserDetailLink() {
    return this.hasUserDetailLink;
  }

}
