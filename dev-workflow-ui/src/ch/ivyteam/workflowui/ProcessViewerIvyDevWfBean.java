package ch.ivyteam.workflowui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.ivy.bpm.error.BpmError;
import ch.ivyteam.workflowui.util.UrlUtil;

@ViewScoped
@ManagedBean
public class ProcessViewerIvyDevWfBean {
  private String viewerUrl;

  public ProcessViewerIvyDevWfBean() {
    this.viewerUrl = checkViewerUrl();
  }

  private String checkViewerUrl() {
    String url = UrlUtil.getUrlParameter("viewerUrl");
    if (url.startsWith("/")) {
      return url;
    } else {
      throw BpmError.create("frame:unsupported:url")
              .withMessage("Only relative urls are supported (security reasons)").build();
    }
  }

  public String getViewerUrl() {
    return viewerUrl;
  }
}
