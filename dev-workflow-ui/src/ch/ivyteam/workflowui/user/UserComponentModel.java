package ch.ivyteam.workflowui.user;

import ch.ivyteam.ivy.security.ISecurityConstants;
import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.ivy.security.restricted.UnknownSecurityMember;
import ch.ivyteam.workflowui.tasks.ResponsibleModel;
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

  public UserComponentModel(ResponsibleModel responsible) {
    var member = responsible.member();
    this.name = member.getDisplayName();
    this.cssIcon = getCssIcon(member);
    this.detailLinkUrl = getUserDetailLink(member);
  }

  private static String getCssIcon(ISecurityMember member) {
    if (isMemberUnknown(member)) {
      return "si si-question-circle";
    }
    if (member.isUser()) {
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

  private static String getUserDetailLink(ISecurityMember member) {
    if (isMemberUnknown(member)) {
      return null;
    }
    if (!member.isUser()) {
      return null;
    }
    if (ISecurityConstants.SYSTEM_USER_NAME.equals(member.getName())) {
      return null;
    }
    if (!PermissionsUtil.isAdmin()) {
      return null;
    }
    return "user.xhtml?userId=" + member.getSecurityMemberId();
  }

  public String getDetailLinkUrl() {
    return detailLinkUrl;
  }

  public boolean getShowUserDetailLink() {
    return detailLinkUrl != null;
  }

  private static boolean isMemberUnknown(ISecurityMember member) {
    return member == null || member instanceof UnknownSecurityMember;
  }
}
