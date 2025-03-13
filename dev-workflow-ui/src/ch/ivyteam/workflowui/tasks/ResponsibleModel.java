package ch.ivyteam.workflowui.tasks;

import ch.ivyteam.ivy.security.ISecurityMember;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.task.responsible.Responsible;

public class ResponsibleModel {

  private final Responsible responsible;

  public ResponsibleModel(Responsible responsible) {
    this.responsible = responsible;
  }

  public ISecurityMember member() {
    return responsible.get();
  }

  public boolean isMember(ISession session) {
    return responsible.get().isMember(session, false);
  }
}
