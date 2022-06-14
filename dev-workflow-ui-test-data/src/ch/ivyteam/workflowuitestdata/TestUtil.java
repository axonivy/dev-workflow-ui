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
      var users = securityContext.users();
      var user = users.find(name);
      if (user == null) {
        user = users.create(name, name);
      }
      var securityDescriptor = securityContext.securityDescriptor();
      securityDescriptor.grantPermission(TASK_READ_ALL, user);
      securityDescriptor.grantPermission(CASE_READ_ALL, user);
      securityDescriptor.grantPermission(WORKFLOW_EVENT_READ_ALL, user);
    });
  }
}
