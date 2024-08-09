package ch.ivyteam.workflowui.state;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class StateBadgeBean {

  public StateBadgeModel toStateBadgeModel(String state) {
    return new StateBadgeModel(state);
  }
}
