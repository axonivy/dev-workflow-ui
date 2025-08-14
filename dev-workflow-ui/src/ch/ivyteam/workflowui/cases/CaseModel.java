package ch.ivyteam.workflowui.cases;

import java.util.Date;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.category.Category;
import ch.ivyteam.ivy.workflow.caze.CaseBusinessState;
import ch.ivyteam.ivy.workflow.caze.owner.CaseOwner;
import ch.ivyteam.workflowui.util.CaseUtil;

public class CaseModel {
  private ICase caze;
  private String owners;

  public CaseModel() {}

  CaseModel(ICase caze) {
    this.caze = caze;
    this.owners = caze.owners().all().stream().map(CaseOwner::memberName).collect(Collectors.joining(", "));
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

  public String getCreatorUserName() {
    return caze.getCreatorUserName();
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

  public CaseBusinessState getBusinessState() {
    return caze.getBusinessState();
  }

  public WebLink getDetailLink() {
    return caze.getDetailLink();
  }

  public void rerunCase() {
    CaseUtil.rerunCaseProcess(this);
  }
}
