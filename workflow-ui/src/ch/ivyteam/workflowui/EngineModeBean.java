package ch.ivyteam.workflowui;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.workflowui.util.EngineModeUtil;

@ManagedBean
public class EngineModeBean {
  public boolean isDemoOrDesigner() {
    return EngineModeUtil.isDemoOrDesigner();
  }
}
