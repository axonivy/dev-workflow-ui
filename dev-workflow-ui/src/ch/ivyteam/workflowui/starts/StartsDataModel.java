package ch.ivyteam.workflowui.starts;

import java.util.List;
import java.util.Optional;

import ch.ivyteam.workflowui.util.ProcessModelsUtil;

public class StartsDataModel {

  private List<CustomPMV> pmvs;
  private String filter = "";
  private String activeIndex;

  public StartsDataModel() {
    pmvs = ProcessModelsUtil.getReleasedWorkflowPMVs()
            .map(pmv -> CustomPMV.create(pmv))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public List<CustomPMV> getPMVs() {
    var filteredPmvs = pmvs.stream()
            .map(cpmv -> cpmv.filter(filter))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    setActiveIndex(filteredPmvs);
    return filteredPmvs;
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
}
