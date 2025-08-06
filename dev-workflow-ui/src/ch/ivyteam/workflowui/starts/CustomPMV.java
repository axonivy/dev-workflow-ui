package ch.ivyteam.workflowui.starts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.application.ActivityOperationState;
import ch.ivyteam.ivy.application.IProcessModelVersion;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.workflow.IWorkflowProcessModelVersion;

public class CustomPMV {
  private static final String PROCESS_START = "process-start";
  private static final String CASE_MAP = "casemap";

  private final IWorkflowProcessModelVersion pmv;
  private final List<CategoryModel> categories;
  private final List<CaseMapStartableModel> caseMapStarts;
  private final List<WebServiceProcess> webServiceProcesses;

  public static Optional<CustomPMV> create(IWorkflowProcessModelVersion pmv) {
    var categories = new ArrayList<CategoryModel>();
    var startables = pmv.getStartables(ISession.current());
    startables.stream().filter(e -> PROCESS_START.equals(e.getType()))
        .map(StartableModel::new)
        .collect(Collectors.groupingBy(StartableModel::getCategory))
        .forEach((cat, starts) -> categories.add(new CategoryModel(cat, starts)));

    var casemapStartElements = startables.stream()
        .filter(e -> CASE_MAP.equals(e.getType()))
        .map(CaseMapStartableModel::new)
        .toList();

    var webServiceProcesses = pmv.getWebServiceProcesses().stream()
        .map(WebServiceProcess::new)
        .toList();

    return filterEmptyStartsAndSelf(pmv, categories, casemapStartElements, webServiceProcesses);
  }

  private boolean containsIgnoreCase(String str, String searchStr) {
    var safeStr = Objects.requireNonNullElse(str, "");
    var safeSearchStr = Objects.requireNonNullElse(searchStr, "");
    return safeStr.toLowerCase().contains(safeSearchStr.toLowerCase());
  }

  public Optional<CustomPMV> filter(String filter) {
    Predicate<StartableModel> filterPredicate = startable -> true;
    Predicate<WebServiceProcess> filterPredicateWSP = wsp -> true;

    if (!containsIgnoreCase(pmv.getName(), filter)) {
      filterPredicate = startable -> containsIgnoreCase(startable.getDisplayName(), filter);
      filterPredicateWSP = wsp -> containsIgnoreCase(wsp.getName(), filter);
    }

    var filteredCategories = new ArrayList<CategoryModel>();
    categories.stream()
        .flatMap(t -> t.getStarts().stream())
        .filter(filterPredicate)
        .collect(Collectors.groupingBy(StartableModel::getCategory))
        .forEach((cat, starts) -> filteredCategories.add(new CategoryModel(cat, starts)));

    var filteredCaseMapStarts = caseMapStarts.stream()
        .filter(filterPredicate)
        .toList();

    var filteredWebServiceProcesses = webServiceProcesses.stream()
        .filter(filterPredicateWSP)
        .toList();

    return filterEmptyStartsAndSelf(pmv, filteredCategories, filteredCaseMapStarts, filteredWebServiceProcesses);
  }

  private static Optional<CustomPMV> filterEmptyStartsAndSelf(IWorkflowProcessModelVersion pmv,
      List<CategoryModel> categories, List<CaseMapStartableModel> caseMaps,
      List<WebServiceProcess> webServiceProcesses) {
    if ((categories.isEmpty() && caseMaps.isEmpty() && webServiceProcesses.isEmpty())
        || pmv.equals(IProcessModelVersion.current())) {
      return Optional.empty();
    }
    return Optional.of(new CustomPMV(pmv, categories, caseMaps, webServiceProcesses));
  }

  private CustomPMV(IWorkflowProcessModelVersion pmv, List<CategoryModel> categories,
      List<CaseMapStartableModel> caseMapStarts, List<WebServiceProcess> webServiceProcesses) {
    this.pmv = pmv;
    this.categories = categories;
    this.caseMapStarts = caseMapStarts;
    this.webServiceProcesses = webServiceProcesses;
  }

  public List<CategoryModel> getCategories() {
    Collections.sort(categories);
    return categories;
  }

  public IWorkflowProcessModelVersion getPMV() {
    return this.pmv;
  }

  public String getStateIcon() {
    if (isActive()) {
      return "check-circle-1";
    }
    if (getState() == ActivityOperationState.INACTIVE) {
      return "remove-circle";
    }
    return "question-circle";
  }

  public boolean isActive() {
    return getState() == ActivityOperationState.ACTIVE;
  }

  public ActivityOperationState getState() {
    return pmv.getActivityOperationState();
  }

  public String getProjectName() {
    return pmv.getProcessModel().getName();
  }

  public String getVersionName() {
    return pmv.getVersionName();
  }

  public List<CaseMapStartableModel> getCaseMapStarts() {
    return caseMapStarts;
  }

  public List<WebServiceProcess> getWebServiceProcesses() {
    return webServiceProcesses;
  }
}
