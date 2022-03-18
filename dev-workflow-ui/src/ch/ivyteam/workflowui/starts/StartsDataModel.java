package ch.ivyteam.workflowui.starts;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.application.IProcessModel;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;

public class StartsDataModel {
  private String filter;
  private String activeIndex;

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public List<CustomPMV> getPMVs() {
    List<CustomPMV> pmvs = IApplication.current().getProcessModels().stream()
            .map(IProcessModel::getReleasedProcessModelVersion)
            .map(IWorkflowProcessModelVersion::of).filter(Objects::nonNull)
            .map(pmv -> CustomPMV.create(pmv, filter)).filter(Optional::isPresent)
            .map(Optional::get).collect(Collectors.toList());
    setActiveIndex(pmvs);
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
}
