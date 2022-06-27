package ch.ivyteam.workflowui.starts;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.ViewerUtil;

@ManagedBean
@ViewScoped
public class ProcessesIvyDevWfBean {
  private StartsDataModel startsDataModel;
  private String viewerLink;
  private String viewerTitle;

  public ProcessesIvyDevWfBean() {
    startsDataModel = new StartsDataModel();
  }

  public StartsDataModel getStartsDataModel() {
    return startsDataModel;
  }

  public List<CustomPMV> getPMVs() {
    return startsDataModel.getPMVs();
  }

  public String getFilter() {
    return this.startsDataModel.getFilter();
  }

  public void setFilter(String filter) {
    this.startsDataModel.setFilter(filter);
  }

  public void redirect() {
    RedirectUtil.redirect("home.xhtml");
  }

  public void setViewerStart(String link) {
    var processStarts = startsDataModel.getPMVs().stream()
            .flatMap(pmv -> pmv.getCategories().stream())
            .flatMap(category -> category.getStarts().stream());
    var caseMapStarts = startsDataModel.getPMVs().stream()
            .flatMap(pmv -> pmv.getCaseMapStarts().stream());
    viewerTitle = findViewerTitle(processStarts, link)
            .orElseGet(() -> findViewerTitle(caseMapStarts, link)
                    .orElse(""));
    viewerLink = link;
  }

  private Optional<String> findViewerTitle(Stream<? extends StartableModel> starts, String link) {
    return starts.filter(start -> start.getViewerLink().toString().equals(link))
            .findFirst()
            .map(start -> ViewerUtil.getViewerDialogTitle(start));
  }

  public String getViewerTitle() {
    return viewerTitle;
  }

  public String getViewerLink() {
    return viewerLink;
  }

}
