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
import ch.ivyteam.ivy.workflow.WorkflowPriority;
import ch.ivyteam.ivy.workflow.query.TaskQuery;
import ch.ivyteam.ivy.workflow.task.TaskBusinessState;
import ch.ivyteam.workflowui.util.PermissionsUtil;
import ch.ivyteam.workflowui.util.TaskUtil;

public class TasksDataModel extends LazyDataModel<TaskModel> {

  private static final long serialVersionUID = 1L;
  private String filter;
  private boolean showAll = PermissionsUtil.isAdmin();

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
  public List<TaskModel> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
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
      if (PermissionsUtil.isAdmin()) {
        var systemUser = ISecurityContext.current().users().system().getSecurityMemberId();
        taskQuery.where().and().not(TaskQuery.create().where().responsibleId().isEqual(systemUser));
      }
    }
  }

  private void applyFilter(TaskQuery query) {
    if (filter != null && !filter.isEmpty()) {
      var lowerFilter = filter.toLowerCase();
      var taskState = Arrays.stream(TaskBusinessState.values())
          .filter(state -> state.toString() != null && state.toString().toLowerCase().startsWith(lowerFilter))
          .findFirst().orElse(null);
      var taskPriority = Arrays.stream(WorkflowPriority.values())
          .filter(priority -> priority.toString() != null && priority.toString().toLowerCase().startsWith(lowerFilter))
          .findFirst().orElse(null);

      var baseQuery = TaskQuery.create().where().name().isLikeIgnoreCase("%" + filter + "%");

      try {
        long taskId = Long.parseLong(filter);
        baseQuery = baseQuery.or().taskId().isEqual(taskId);
      } catch (NumberFormatException e) {}

      query.where().and(baseQuery
          .or().uuid().isEqual(filter)
          .or().responsibleName().isLikeIgnoreCase("%" + filter + "%")
          .or().responsibleDisplayName().isLikeIgnoreCase("%" + filter + "%")
          .or().businessState().isEqual(taskState)
          .or().priority().isEqual(taskPriority));
    }
  }

  private static void applyOrdering(TaskQuery query, String sortField, SortOrder sortOrder) {
    if (StringUtils.isEmpty(sortField)) {
      applySorting(query.orderBy().startTimestamp(), SortOrder.DESCENDING);
    }
    if ("businessState".equals(sortField)) {
      applySorting(query.orderBy().businessState(), sortOrder);
    }
    if ("priority".equals(sortField)) {
      applySorting(query.orderBy().priority(), sortOrder);
    }
    if ("name".equals(sortField)) {
      applySorting(query.orderBy().name(), sortOrder);
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
