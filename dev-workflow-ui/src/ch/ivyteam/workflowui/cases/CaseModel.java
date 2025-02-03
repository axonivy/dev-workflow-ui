package ch.ivyteam.workflowui.cases;

import java.util.Date;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.category.Category;

public class CaseModel {
  private final ICase caze;
  private final String owners;

  CaseModel(ICase caze) {
    this.caze = caze;
    this.owners = caze.owners().all().stream().map(o -> o.memberName()).collect(Collectors.joining(", "));
  }

  public ICase getCase() {
    return caze;
  }

  public long getId() {
    return caze.getId();
  }

  public String getUuid() {
    return caze.uuid();
  }

  public String getName() {
    return caze.getName();
  }

  public String getOwners() {
    return owners;
  }

  public IUser getCreatorUser() {
    return caze.getCreatorUser();
  }

  public Category getCategory() {
    return caze.getCategory();
  }

  public Date getStartTimestamp() {
    return caze.getStartTimestamp();
  }

  public Date getEndTimestamp() {
    return caze.getEndTimestamp();
  }

}
