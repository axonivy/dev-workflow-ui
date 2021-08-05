package ch.ivyteam.workflowui;

import javax.faces.bean.ManagedBean;

import com.google.inject.servlet.RequestScoped;

import ch.ivyteam.ivy.server.restricted.EngineMode;

@ManagedBean
@RequestScoped
@SuppressWarnings("restriction")
public class ApiBrowserBean {

  public String getApp()
  {
    return EngineMode.isEmbeddedInDesigner() ? "designer" : "system";
  }

}
