package ch.ivyteam.workflowui.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import ch.ivyteam.ivy.jsf.primefaces.sort.SortMetaConverter;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.ivy.workflow.WorkflowPriority;
import ch.ivyteam.ivy.workflow.query.TaskQuery;
import ch.ivyteam.workflowui.util.EngineModeUtil;
import ch.ivyteam.workflowui.util.TaskUtil;
import ch.ivyteam.workflowui.util.UserUtil;

public class TasksDataModel extends LazyDataModel<TaskModel> {

  private static final long serialVersionUID = -5287014754211109062L;
  private String filter;
  private boolean showAll = UserUtil.isAdmin();

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  @Override
  public String getRowKey(TaskModel task) {
    return task.getUuid();
  }

  @Override
  public TaskModel getRowData(String rowKey) {
    return new TaskModel(TaskUtil.getTaskById(rowKey));
  }

  @Override
  public int count(Map<String, FilterMeta> filterBy) {
    return 0;
  }

  @Override
  public List<TaskModel> load(int first, int pageSize, Map<String, SortMeta> sortBy,
          Map<String, FilterMeta> filterBy) {
    var sort = new SortMetaConverter(sortBy);
    var taskQuery = createBaseQuery();

    applyFilter(taskQuery);
    applyOrdering(taskQuery, sort.toField(), sort.toOrder());

    List<TaskModel> tasks = TaskUtil.toTaskModelList(taskQuery.executor().resultsPaged().window(first, pageSize));
    setRowCount((int) taskQuery.executor().count());
    return tasks;
  }

  private TaskQuery createBaseQuery() {
    var taskQuery = TaskQuery.create();
    applyCustomFilter(taskQuery);
    checkIfShowAll(taskQuery);
    return taskQuery;
  }

  public int getSize() {
    return (int) createBaseQuery().executor().count();
  }

  @SuppressWarnings("unused")
  protected void applyCustomFilter(TaskQuery taskQuery) {}

  private void checkIfShowAll(TaskQuery taskQuery) {
    if (!isShowAll()) {
      taskQuery.where().and(TaskQuery.create().where().currentUserIsInvolved());
      if (EngineModeUtil.isDesigner() && UserUtil.isAdmin()) {
        taskQuery.where().and().not(TaskQuery.create().where().activatorId().isEqual(ISecurityContext.current().users().system().getSecurityMemberId()));
      }
    }
  }

  private void applyFilter(TaskQuery query) {
    if (StringUtils.isNotEmpty(filter)) {
      var taskState = Arrays.asList(TaskState.values()).stream()
              .filter(state -> StringUtils.startsWithIgnoreCase(state.toString(), filter))
              .findFirst().orElse(null);
      var taskPriority = Arrays.asList(WorkflowPriority.values()).stream()
              .filter(priority -> StringUtils.startsWithIgnoreCase(priority.toString(), filter))
              .findFirst().orElse(null);
      query.where().and(TaskQuery.create().where().name().isLikeIgnoreCase("%" + filter + "%")
              .or().activatorName().isLikeIgnoreCase(filter + "%")
              .or().state().isEqual(taskState)
              .or().priority().isEqual(taskPriority));
    }
  }

  private static void applyOrdering(TaskQuery query, String sortField, SortOrder sortOrder) {
    if (StringUtils.isEmpty(sortField)) {
      applySorting(query.orderBy().startTimestamp(), SortOrder.DESCENDING);
    }
    if ("state".equals(sortField)) {
      applySorting(query.orderBy().state(), sortOrder);
    }
    if ("priority".equals(sortField)) {
      applySorting(query.orderBy().priority(), sortOrder);
    }
    if ("name".equals(sortField)) {
      applySorting(query.orderBy().name(), sortOrder);
    }
    if ("activatorName".equals(sortField)) {
      applySorting(query.orderBy().activatorName(), sortOrder);
    }
    if ("startTimestamp".equals(sortField)) {
      applySorting(query.orderBy().startTimestamp(), sortOrder);
    }
    if ("expiryTimestamp".equals(sortField)) {
      applySorting(query.orderBy().expiryTimestamp(), sortOrder);
    }
    if ("endTimestamp".equals(sortField)) {
      applySorting(query.orderBy().endTimestamp(), sortOrder);
    }
  }

  private static void applySorting(TaskQuery.OrderByColumnQuery query, SortOrder sortOrder) {
    if (SortOrder.ASCENDING.equals(sortOrder)) {
      query.ascending();
    }
    if (SortOrder.DESCENDING.equals(sortOrder)) {
      query.descending();
    }
  }

  public boolean isShowAll() {
    return showAll;
  }

  public void setShowAll(boolean showAll) {
    this.showAll = showAll;
  }
}
