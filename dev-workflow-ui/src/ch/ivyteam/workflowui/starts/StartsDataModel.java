package ch.ivyteam.workflowui.starts;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.ivyteam.workflowui.util.ProcessModelsUtil;

public class StartsDataModel {

  private List<CustomPMV> pmvs;
  private String filter = "";
  private String activeIndex;

  public StartsDataModel() {
    reloadPmvs();
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
    reloadPmvs();
  }

  public List<CustomPMV> getPMVs() {
    return pmvs;
  }

  private void setActiveIndex(List<CustomPMV> pmvs) {
    if (pmvs.size() == 1) {
      activeIndex = "0";
    } else {
      activeIndex = "null";
    }
  }

  public void setActiveIndex(@SuppressWarnings("unused") String index) {}

  public String getActiveIndex() {
    return activeIndex;
  }

  private void reloadPmvs() {
    pmvs = ProcessModelsUtil.getReleasedWorkflowPMVs()
            .map(pmv -> CustomPMV.create(pmv, filter))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    setActiveIndex(pmvs);
  }
}
