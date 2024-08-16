package ch.ivyteam.workflowui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.ivy.security.IPermission;
import ch.ivyteam.workflowui.util.PermissionsUtil;

@ManagedBean
@ViewScoped
public class PermissionsBean {

  public boolean isDemoOrDevMode() {
    return PermissionsUtil.isDemoOrDevMode();
  }

  public boolean isDevModeAndAdmin() {
    return PermissionsUtil.isDevMode() && isAdmin();
  }

  public boolean isAdmin() {
    return PermissionsUtil.isAdmin();
  }

  public boolean hasWorkflowEventReadPermission() {
    return PermissionsUtil.hasPermission(IPermission.WORKFLOW_EVENT_READ_ALL);
  }

  public boolean isDemoDevModeOrAdmin() {
    return PermissionsUtil.isDemoDevModeOrAdmin();
  }

  public boolean isDemoDevModeAndAdmin() {
    return PermissionsUtil.isDemoDevModeAndAdmin();
  }
}
