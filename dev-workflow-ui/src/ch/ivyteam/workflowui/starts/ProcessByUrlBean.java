package ch.ivyteam.workflowui.starts;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.application.app.IApplicationRepository;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.ivy.workflow.start.IWebStartable;

@ManagedBean
public class ProcessByUrlBean {
  private String app;
  private String pmv;
  private String startableId;


  public void executeStart() {
    var releasedPmv = IApplicationRepository.instance().findByName(app)
            .map(a -> a.findProcessModelVersion(pmv));
    if (!releasedPmv.isPresent()) {
      return;
    }
    IWorkflowProcessModelVersion.of(releasedPmv.get()).getAllStartables(ISession.current())
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

  public String getStartableId() {
    return startableId;
  }

  public void setStartableId(String startableId) {
    this.startableId = startableId;
  }

  public boolean isStartableIdSet() {
    return startableId != null && !startableId.isEmpty();
  }

  public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }

  public String getPmv() {
    return pmv;
  }

  public void setPmv(String pmv) {
    this.pmv = pmv;
  }


}
