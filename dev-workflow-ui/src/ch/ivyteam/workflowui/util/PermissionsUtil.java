package ch.ivyteam.workflowui.util;

import java.util.Arrays;

import ch.ivyteam.ivy.security.IPermission;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.server.restricted.EngineMode;
import ch.ivyteam.ivy.workflow.IWorkflowSession;

public class PermissionsUtil {

  public static boolean isDemoOrDevMode() {
    return EngineMode.is(EngineMode.DEMO) || isDevMode();
  }

  public static boolean isDevMode() {
    return ISecurityContext.current().isDevMode();
  }

  public static boolean isDemoDevModeOrAdmin() {
    return isDemoOrDevMode() || isAdmin();
  }

  public static boolean isDemoDevModeAndAdmin() {
    return isDemoOrDevMode() && isAdmin();
  }

  public static boolean isAdmin() {
    return hasPermission(IPermission.TASK_READ_ALL) && hasPermission(IPermission.CASE_READ_ALL);
  }

  public static Boolean hasPermission(IPermission... permissions) {
    var session = IWorkflowSession.current();
    var securityDescriptor = ISecurityContext.current().securityDescriptor();
    return Arrays
        .stream(permissions)
        .anyMatch(p -> session.hasPermission(securityDescriptor, p));
  }
}
