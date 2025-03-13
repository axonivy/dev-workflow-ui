package ch.ivyteam.workflowui.user;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.workflowui.tasks.ResponsibleModel;

@ManagedBean
public class UserComponentIvyDevWfBean {

  public UserComponentModel toComponentModel(Object user) {
    if (user instanceof ResponsibleModel responsible) {
      return new UserComponentModel(responsible);
    } else if (user instanceof ISecurityMember securityMember) {
      return new UserComponentModel(securityMember);
    }
    return null;
  }
}
