package ch.ivyteam.workflowui;

import java.io.Serializable;
import java.util.List;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.menu.MenuModel;

import ch.ivyteam.ivy.bpm.error.BpmError;
import ch.ivyteam.ivy.dialog.execution.api.DialogInstance;
import ch.ivyteam.ivy.request.EngineUriResolver;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.util.uri.UriChecker;
import ch.ivyteam.workflowui.casemap.SidestepModel;
import ch.ivyteam.workflowui.casemap.SidestepUtil;
import ch.ivyteam.workflowui.util.TaskUtil;
import ch.ivyteam.workflowui.util.UrlUtil;

@ViewScoped
@Named
public class FrameBean implements Serializable {

  private String taskName;
  private final String taskUrl;
  private List<SidestepModel> sidesteps;
  private String origin;
  private MenuModel sidestepMenuModel;

  public FrameBean() {
    this.taskUrl = checkTaskUrl();
  }

  public void useTaskInIFrame() {
    var processUrl = UrlUtil.getUrlParameter("url");
    var task = DialogInstance.of(processUrl).task();
    if (StringUtils.isNotBlank(processUrl) && task != null) {
      this.taskName = TaskUtil.getName(task);
      this.sidesteps = SidestepUtil.getSidesteps(task.getCase());
      this.sidestepMenuModel = SidestepUtil.createMenuModel(getSidesteps(), getOrigin());
    } else {
      this.taskName = "[No Task Name]";
    }
  }

  private String checkTaskUrl() {
    var url = UrlUtil.getUrlParameter("taskUrl");
    if (url == null) {
      return url;
    }
    if (!UriChecker.isRelative(url)) {
      var info = "taskUrl=" + url + "[url=" + UrlUtil.getHttpServletRequest().getRequestURI() + ", query=" + UrlUtil.getHttpServletRequest().getQueryString() + "]";
      throw BpmError.create("frame:unsupported:url").withMessage("Only relative urls are supported (security reasons): " + info).build();
    }
    return url;
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

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getBaseUrl() {
    return EngineUriResolver.instance().external().resolve(ISecurityContext.current().contextPath()).toString();
  }
}
