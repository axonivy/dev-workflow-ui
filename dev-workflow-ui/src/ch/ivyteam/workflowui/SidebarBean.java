package ch.ivyteam.workflowui;

import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.security.context.EngineCockpitUrlPath;

@ManagedBean
@RequestScoped
public class SidebarBean {

  private final String activePage;
  private final String[] developerPages = {
      "signals",
      "intermediateEvents",
      "cleanup",
      "api-browser",
      "statistics",
      "webservices"
  };

  public SidebarBean() {
    var currentUrl = FacesContext.getCurrentInstance().getViewRoot().getViewId();
    var page = StringUtils.substringAfterLast(currentUrl, "/");
    this.activePage = StringUtils.substringBeforeLast(page, ".");
  }

  public boolean isActive(String... pages) {
    return Stream.of(pages).anyMatch(page -> activePage.equals(page));
  }

  public String getCockpitPath() {
    return EngineCockpitUrlPath.toPath();
  }

  public String isDeveloperPageActive() {
    if (isActive(developerPages)) {
      return "active-nav-page";
    }
    return "";
  }
}
