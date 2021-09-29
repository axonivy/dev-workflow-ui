package ch.ivyteam.workflowuitestdata;

import static ch.ivyteam.ivy.security.IPermission.CASE_READ_ALL;
import static ch.ivyteam.ivy.security.IPermission.TASK_READ_ALL;
import static ch.ivyteam.ivy.security.IPermission.WORKFLOW_EVENT_READ_ALL;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.security.exec.Sudo;

public class TestUtil
{

  public static void makeAdmin()
  {
    String testDevUser = "DeveloperTest";
    Sudo.exec(() -> {
      IUser developerTestUser = IApplication.current().getSecurityContext().users().find(testDevUser);
      if (developerTestUser == null)
      {
        developerTestUser = IApplication.current().getSecurityContext().users().create(testDevUser, testDevUser);
      }
      IApplication.current().getSecurityDescriptor().grantPermission(TASK_READ_ALL,
              developerTestUser);
      IApplication.current().getSecurityDescriptor().grantPermission(CASE_READ_ALL,
              developerTestUser);
      IApplication.current().getSecurityDescriptor().grantPermission(WORKFLOW_EVENT_READ_ALL,
              developerTestUser);
    });
  }

}
