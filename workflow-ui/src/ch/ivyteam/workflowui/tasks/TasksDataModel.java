package ch.ivyteam.workflowui.tasks;

import static ch.ivyteam.workflowui.util.UserUtil.checkIfHomepage;
import static ch.ivyteam.workflowui.util.UserUtil.checkIfPersonalTasks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.TaskState;
import ch.ivyteam.ivy.workflow.WorkflowPriority;
import ch.ivyteam.ivy.workflow.query.TaskQuery;
import ch.ivyteam.workflowui.util.UserUtil;

public class TasksDataModel extends LazyDataModel<ITask>
{
  private static final long serialVersionUID = -5287014754211109062L;
  private String filter;
  private boolean showAllTasks = UserUtil.isAdmin();

  public String getFilter()
  {
    return filter;
  }

  public void setFilter(String filter)
  {
    this.filter = filter;
  }

  @Override
  public List<ITask> load(int first, int pageSize, String sortField, SortOrder sortOrder,
      Map<String, Object> filters)
  {
    var taskQuery = TaskQuery.create();

    applyFilter(taskQuery);
    applyOrdering(taskQuery, sortField, sortOrder);

    checkIfPersonalTasksOrHomepage(taskQuery);

    List<ITask> tasks = taskQuery
        .executor()
        .resultsPaged()
        .window(first, pageSize);
    setRowCount((int) taskQuery.executor().count());
    return tasks;
  }

  private void checkIfPersonalTasksOrHomepage(TaskQuery taskQuery)
  {
    if (checkIfPersonalTasks() || checkIfHomepage())
    {
      taskQuery.where().currentUserCanWorkOn();
    }
  }

  private void applyFilter(TaskQuery query)
  {
    if (StringUtils.isNotEmpty(filter))
    {
      var taskState = Arrays.asList(TaskState.values()).stream()
          .filter(state -> StringUtils.startsWithIgnoreCase(state.toString(), filter))
          .findFirst().orElse(null);
      var taskPriority = Arrays.asList(WorkflowPriority.values()).stream()
          .filter(priority -> StringUtils.startsWithIgnoreCase(priority.toString(), filter))
          .findFirst().orElse(null);
      query.where().name().isLikeIgnoreCase("%" + filter + "%")
          .or().activatorName().isLikeIgnoreCase(filter + "%")
          .or().state().isEqual(taskState)
          .or().priority().isEqual(taskPriority);
    }
  }

  private static void applyOrdering(TaskQuery query, String sortField, SortOrder sortOrder)
  {
    if (StringUtils.isEmpty(sortField))
    {
      applySorting(query.orderBy().startTimestamp(), SortOrder.DESCENDING);
    }
    if ("state".equals(sortField))
    {
      applySorting(query.orderBy().state(), sortOrder);
    }
    if ("priority".equals(sortField))
    {
      applySorting(query.orderBy().priority(), sortOrder);
    }
    if ("name".equals(sortField))
    {
      applySorting(query.orderBy().name(), sortOrder);
    }
    if ("activatorName".equals(sortField))
    {
      applySorting(query.orderBy().activatorName(), sortOrder);
    }
    if ("startTimestamp".equals(sortField))
    {
      applySorting(query.orderBy().startTimestamp(), sortOrder);
    }
    if ("expiryTimestamp".equals(sortField))
    {
      applySorting(query.orderBy().expiryTimestamp(), sortOrder);
    }
    if ("endTimestamp".equals(sortField))
    {
      applySorting(query.orderBy().endTimestamp(), sortOrder);
    }
  }

  private static void applySorting(TaskQuery.OrderByColumnQuery query, SortOrder sortOrder)
  {
    if (SortOrder.ASCENDING.equals(sortOrder))
    {
      query.ascending();
    }
    if (SortOrder.DESCENDING.equals(sortOrder))
    {
      query.descending();
    }
  }

  public int getSize()
  {
    var taskQuery = TaskQuery.create();
    applyFilter(taskQuery);
    checkIfPersonalTasksOrHomepage(taskQuery);
    return (int) taskQuery.executor().count();
  }

  public boolean getShowAllTasks()
  {
    return showAllTasks;
  }

  public void setShowAllTasks(boolean allTasks)
  {
    this.showAllTasks = allTasks;
  }
}
