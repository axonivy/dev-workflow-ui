package ch.ivyteam.workflowui.user;

import ch.ivyteam.ivy.security.ISecurityConstants;
import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.ivy.workflow.task.IActivator;
import ch.ivyteam.workflowui.util.PermissionsUtil;

public class UserComponentModel {

  private final String name;
  private final String cssIcon;
  private final String detailLinkUrl;

  public UserComponentModel(ISecurityMember securityMember) {
    this.name = securityMember.getDisplayName();
    this.cssIcon = getCssIcon(securityMember);
    this.detailLinkUrl = getUserDetailLink(securityMember);
  }

  public UserComponentModel(IActivator activator) {
    var securityMember = activator.get();
    if (securityMember == null) {
      this.name = activator.name();
    } else {
      this.name = securityMember.getDisplayName();
    }
    this.cssIcon = getCssIcon(securityMember);
    this.detailLinkUrl = getUserDetailLink(securityMember);
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

  private static String getUserDetailLink(ISecurityMember activator) {
    if (activator != null && activator.isUser()
        && !ISecurityConstants.SYSTEM_USER_NAME.equals(activator.getName())
        && PermissionsUtil.isAdmin()) {
      var securityMemberId = activator.getSecurityMemberId();
      return "user.xhtml?userId=" + securityMemberId;
    }
    return null;
  }

  public String getDetailLinkUrl() {
    return detailLinkUrl;
  }

  public boolean getShowUserDetailLink() {
    return detailLinkUrl != null;
  }

}
