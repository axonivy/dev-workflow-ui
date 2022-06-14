package ch.ivyteam.workflowuitestdata;

import static ch.ivyteam.ivy.security.IPermission.CASE_READ_ALL;
import static ch.ivyteam.ivy.security.IPermission.TASK_READ_ALL;
import static ch.ivyteam.ivy.security.IPermission.WORKFLOW_EVENT_READ_ALL;

import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.exec.Sudo;

public class TestUtil {

  public static void makeAdmin() {
    var name = "DeveloperTest";
    Sudo.run(() -> {
      var securityContext = ISecurityContext.current();
      var user = securityContext.users().find(name);
      if (user == null) {
        user = securityContext.users().create(name, name);
      }
      var securityDescritpor = securityContext.securityDescriptor();
      securityDescritpor.grantPermission(TASK_READ_ALL, user);
      securityDescritpor.grantPermission(CASE_READ_ALL, user);
      securityDescritpor.grantPermission(WORKFLOW_EVENT_READ_ALL, user);
    });
  }
}
