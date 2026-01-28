package ch.ivyteam.workflowui.starts;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.application.ReleaseState;
import ch.ivyteam.ivy.application.app.IApplicationRepository;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;
import ch.ivyteam.ivy.workflow.start.IWebStartable;

@ManagedBean
public class ProcessByUrlBean {

  private String appName;
  private String projectName;
  private String startableId;

  public void executeStart() {
    var app = IApplicationRepository.instance().findByName(appName).stream()
        .filter(a -> a.getReleaseState() == ReleaseState.RELEASED)
        .findAny()
        .orElse(null);
    if (app == null) {
      return;
    }
    var project = app.findProcessModelVersion(projectName);
    if (project == null) {
      return;
    }
    IWorkflowProcessModelVersion.of(project).getAllStartables(ISession.current())
        .filter(s -> s.getId().equals(startableId))
        .findFirst()
        .map(ProcessByUrlBean::createCaseMapOrProcessStartable)
        .ifPresent(StartableModel::execute);
  }

  private static StartableModel createCaseMapOrProcessStartable(IWebStartable startable) {
    if ("casemap".equals(startable.getType())) {
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
    return appName;
  }

  public void setApp(String app) {
    this.appName = app;
  }

  public String getPmv() {
    return projectName;
  }

  public void setPmv(String pmv) {
    this.projectName = pmv;
  }
}
