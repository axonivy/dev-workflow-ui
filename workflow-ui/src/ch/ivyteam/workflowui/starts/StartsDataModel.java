package ch.ivyteam.workflowui.starts;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.primefaces.model.LazyDataModel;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.application.IProcessModel;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;

public class StartsDataModel extends LazyDataModel<CustomPMV> {
  private static final long serialVersionUID = 6607241324190280974L;
  private String filter;

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public List<CustomPMV> getPMVs() {
    return IApplication.current().getProcessModels().stream()
            .map(IProcessModel::getReleasedProcessModelVersion)
            .map(IWorkflowProcessModelVersion::of).filter(Objects::nonNull)
            .map(pmv -> CustomPMV.create(pmv, filter)).filter(Optional::isPresent).map(Optional::get)
            .collect(Collectors.toList());
  }
}
