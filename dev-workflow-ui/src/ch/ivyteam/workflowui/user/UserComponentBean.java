package ch.ivyteam.workflowui.user;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.workflowui.tasks.ResponsibleModel;

@Named
@RequestScoped
public class UserComponentBean {

  public UserComponentModel toComponentModel(Object user) {
    if (user instanceof ResponsibleModel responsible) {
      return new UserComponentModel(responsible);
    } else if (user instanceof ISecurityMember securityMember) {
      return new UserComponentModel(securityMember);
    }
    return null;
  }
}
