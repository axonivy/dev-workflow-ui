package ch.ivyteam.workflowui;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.server.restricted.EngineMode;

@ManagedBean
@SuppressWarnings("restriction")
public class ApiBrowserIvyDevWfBean {

  public String getApp() {
    return EngineMode.isEmbeddedInDesigner() ? "designer" : "system";
  }

}