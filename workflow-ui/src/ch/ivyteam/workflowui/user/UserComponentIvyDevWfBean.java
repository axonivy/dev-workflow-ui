package ch.ivyteam.workflowui.user;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.security.ISecurityMember;

@ManagedBean
public class UserComponentIvyDevWfBean {

  public UserComponentModel toComponentModel(ISecurityMember user) {
    return user != null ? new UserComponentModel(user) : null;
  }
}
