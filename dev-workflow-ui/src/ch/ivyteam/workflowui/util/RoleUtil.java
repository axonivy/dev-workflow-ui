package ch.ivyteam.workflowui.util;

import java.util.List;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.IRole;

public class RoleUtil {

    public static List<IRole> getRoles() {
      return Ivy.security().roles().all();
    }
}
