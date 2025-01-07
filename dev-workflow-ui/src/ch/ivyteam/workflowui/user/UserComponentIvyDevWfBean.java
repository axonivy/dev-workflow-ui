package ch.ivyteam.workflowui.user;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.ivy.workflow.task.IActivator;

@ManagedBean
public class UserComponentIvyDevWfBean {

  public UserComponentModel toComponentModel(Object user) {
    if (user instanceof IActivator) {
      return new UserComponentModel((IActivator) user);
    } else if (user instanceof ISecurityMember) {
      return new UserComponentModel((ISecurityMember) user);
    }
    return null;
  }

}
