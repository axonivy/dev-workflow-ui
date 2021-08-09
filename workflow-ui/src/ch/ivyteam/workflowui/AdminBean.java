package ch.ivyteam.workflowui;

import java.io.Serializable;
import java.util.Arrays;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.security.IPermission;
import ch.ivyteam.ivy.workflow.IWorkflowSession;

@ManagedBean
public class AdminBean implements Serializable {

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

  public Boolean hasReadAbsencesPermission() {
    return hasPermission(IPermission.USER_READ_ABSENCES);
  }

  public Boolean hasAbsencePermission() {
    return hasPermission(
            IPermission.USER_CREATE_ABSENCE,
            IPermission.USER_READ_ABSENCES);
  }

  public Boolean hasAnyAbsencePermission() {
    return hasAtLeastOnePermission(
            IPermission.USER_CREATE_ABSENCE,
            IPermission.USER_READ_ABSENCES,
            IPermission.USER_CREATE_OWN_ABSENCE,
            IPermission.USER_READ_OWN_ABSENCES);
  }

  public Boolean hasSubstitutePermission() {
    return hasPermission(
            IPermission.USER_CREATE_SUBSTITUTE,
            IPermission.USER_READ_SUBSTITUTIONS);
  }

  public Boolean hasAnySubstitutePermission() {
    return hasAtLeastOnePermission(
            IPermission.USER_CREATE_SUBSTITUTE,
            IPermission.USER_READ_SUBSTITUTIONS,
            IPermission.USER_CREATE_OWN_SUBSTITUTE,
            IPermission.USER_READ_OWN_SUBSTITUTIONS);
  }

  public Boolean hasSessionReadAllPermission() {
    return hasPermission(
            IPermission.SESSION_READ_ALL,
            IPermission.SESSION_READ_SESSION_USER,
            IPermission.SESSION_READ_ACTIVE_SUBSTITUTIONS,
            IPermission.SESSION_READ_ABSENT);
  }

  private static Boolean hasPermission(IPermission... permissions) {
    var session = IWorkflowSession.current();
    var securityDescriptor = IApplication.current().getSecurityDescriptor();
    return Arrays
            .stream(permissions)
            .anyMatch(p -> session.hasPermission(securityDescriptor, p));
  }

  private static Boolean hasAtLeastOnePermission(IPermission... permissions) {
    var session = IWorkflowSession.current();
    var securityDescriptor = IApplication.current().getSecurityDescriptor();
    return Arrays
            .stream(permissions)
            .anyMatch(p -> session.hasPermission(securityDescriptor, p));
  }
}
