package ch.ivyteam.workflowui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ch.ivyteam.ivy.server.restricted.EngineMode;

@ManagedBean
@SessionScoped
@SuppressWarnings("restriction")
public class EngineModeBean
{
  public boolean isDemoOrDesigner()
  {
    return EngineMode.is(EngineMode.DEMO) || EngineMode.is(EngineMode.DESIGNER_EMBEDDED);
  }
}
