package ch.ivyteam.workflowui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ch.ivyteam.workflowui.util.EngineModeUtil;

@ManagedBean
@SessionScoped
public class EngineModeBean {
  public boolean isDemoOrDesigner() {
    return EngineModeUtil.isDemoOrDesigner();
  }
}
