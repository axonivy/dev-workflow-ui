package ch.ivyteam.workflowui.starts;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.util.ProcessModelsUtil;

@ManagedBean
public class ProcessByUrlBean {
  private String startableId;

  public String getStartableId() {
    return startableId;
  }

  public void setStartableId(String startableId) {
    this.startableId = startableId;
    var releasedPmvs = ProcessModelsUtil.getReleasedWorkflowPMVs();
    releasedPmvs.flatMap(pmv -> pmv.getStartables(ISession.current()).stream())
      .filter(s -> s.getId().equals(startableId))
      .findFirst()
      .map(ProcessByUrlBean::createCaseMapOrProcessStartable)
      .ifPresent(StartableModel::execute);
  }

  private static StartableModel createCaseMapOrProcessStartable(IWebStartable startable) {
    if (startable.getType().equals("casemap")) {
      return new CaseMapStartableModel(startable);
    }
    return new StartableModel(startable);
  }

  public boolean isStartableIdSet() {
    return startableId != null && !startableId.isEmpty();
  }


}
