package ch.ivyteam.workflowui.user;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.security.ISecurityMember;

@ManagedBean
public class UserComponentBean {

  public UserComponentModel toComponentModel(ISecurityMember user) {
    return user != null ? new UserComponentModel(user) : null;
  }
}
