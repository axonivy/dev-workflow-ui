package ch.ivyteam.workflowui;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.menu.MenuModel;

import ch.ivyteam.ivy.bpm.error.BpmError;
import ch.ivyteam.ivy.dialog.execution.api.DialogInstance;
import ch.ivyteam.workflowui.casemap.SidestepModel;
import ch.ivyteam.workflowui.casemap.SidestepUtil;
import ch.ivyteam.workflowui.util.TaskUtil;
import ch.ivyteam.workflowui.util.UrlUtil;

@ViewScoped
@ManagedBean
public class FrameIvyDevWfBean {
  private String taskName;
  private String taskUrl;
  private List<SidestepModel> sidesteps;
  private String originalUrl;
  private MenuModel sidestepMenuModel;

  public FrameIvyDevWfBean() {
    this.taskUrl = checkTaskUrl();
  }

  public void useTaskInIFrame() {
    var processUrl = UrlUtil.getUrlParameter("url");
    var task = DialogInstance.of(processUrl).task();
    if (StringUtils.isNotBlank(processUrl) && task != null) {
      this.taskName = TaskUtil.getName(task);
      this.sidesteps = SidestepUtil.getSidesteps(task.getCase());
      this.sidestepMenuModel = SidestepUtil.createMenuModel(getSidesteps(), getOriginalUrl());
    } else {
      this.taskName = "[No Task Name]";
    }
  }

  private String checkTaskUrl() {
    var url = UrlUtil.getUrlParameter("taskUrl");
    if (StringUtils.startsWith(url, "/")) {
      return url;
    } else {
      throw BpmError.create("frame:unsupported:url")
              .withMessage("Only relative urls are supported (security reasons)").build();
    }
  }

  public String getTaskUrl() {
    return taskUrl;
  }

  public String getTaskName() {
    return taskName;
  }

  public boolean renderSidestepBtn() {
    return (sidesteps != null && !sidesteps.isEmpty());
  }

  public List<SidestepModel> getSidesteps() {
    return sidesteps;
  }

  public MenuModel getSidestepsMenuModel() {
    return sidestepMenuModel;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }
}
