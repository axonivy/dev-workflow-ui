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
    return createMenuModel(sidesteps, UrlUtil.evalOriginPage());
  }

  public static MenuModel createMenuModel(List<SidestepModel> sidesteps, String redirectPage) {
    MenuModel menuModel = new DefaultMenuModel();
    if (sidesteps != null) {
      for (SidestepModel sidestep : sidesteps) {
        var link = UrlUtil.generateStartFrameUrl(sidestep.getStartLink(), redirectPage);
        DefaultMenuItem item = DefaultMenuItem.builder().value(sidestep.getName()).url(link).build();
        menuModel.getElements().add(item);
      }
    }
    return menuModel;
  }

  public static List<SidestepModel> getSidesteps(long taskId) {
    ITask task = IWorkflowContext.current().findTask(taskId);
    return getSidesteps(task.getCase().getBusinessCase());
  }

  public static List<SidestepModel> getSidesteps(ICase caze) {
    if (caze == null) {
      return List.of();
    }
    return ICaseMapService.current().getCaseMapService(caze.getBusinessCase()).findStartableSideSteps()
        .stream().map(SidestepModel::new).collect(Collectors.toList());
  }
}
