package ch.ivyteam.workflowui;

import java.util.Arrays;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.security.IPermission;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.workflow.IWorkflowSession;

@ManagedBean
public class AdminIvyDevWfBean {

  public Boolean isWorkflowAdmin() {
    return hasTaskPermissions() && hasCasePermissions();
  }

  private Boolean hasTaskPermissions() {
    return hasPermission(IPermission.TASK_READ_ALL);
  }

  private Boolean hasCasePermissions() {
    return hasPermission(IPermission.CASE_READ_ALL);
  }

  public Boolean hasWorkflowEventReadPermission() {
    return hasPermission(IPermission.WORKFLOW_EVENT_READ_ALL);
  }

  private static Boolean hasPermission(IPermission... permissions) {
    var session = IWorkflowSession.current();
    var securityDescriptor = ISecurityContext.current().securityDescriptor();
    return Arrays
            .stream(permissions)
            .anyMatch(p -> session.hasPermission(securityDescriptor, p));
  }
}
