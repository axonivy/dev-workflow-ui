package ch.ivyteam.workflowui.casemap;

import java.util.List;
import java.util.stream.Collectors;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import ch.ivyteam.ivy.casemap.runtime.ICaseMapService;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowContext;
import ch.ivyteam.workflowui.util.UrlUtil;

public class SidestepUtil {

  public static MenuModel createMenuModel(List<SidestepModel> sidesteps) {
    MenuModel menuModel = new DefaultMenuModel();
    for (SidestepModel sidestep : sidesteps) {
      DefaultMenuItem item = new DefaultMenuItem(sidestep.getName(), "",
              UrlUtil.generateStartFrameUrl(sidestep.getStartLink()));
      menuModel.getElements().add(item);
    }
    return menuModel;
  }

  public static List<SidestepModel> getSidesteps(long taskId) {
    ITask task = IWorkflowContext.current().findTask(taskId);
    return getSidesteps(task.getCase().getBusinessCase());
  }

  public static List<SidestepModel> getSidesteps(ICase caze) {
    return ICaseMapService.current().getCaseMapService(caze.getBusinessCase()).findStartableSideSteps()
            .stream().map(SidestepModel::new).collect(Collectors.toList());
  }
}
