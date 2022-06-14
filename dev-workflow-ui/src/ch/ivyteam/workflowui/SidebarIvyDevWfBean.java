package ch.ivyteam.workflowui;

import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

@ManagedBean
@RequestScoped
public class SidebarIvyDevWfBean {

  private final String activePage;

  public SidebarIvyDevWfBean() {
    String currentUrl = FacesContext.getCurrentInstance().getViewRoot().getViewId();
    var page = StringUtils.substringAfterLast(currentUrl, "/");
    this.activePage = StringUtils.substringBeforeLast(page, ".");
  }

  public boolean isActive(String... pages) {
    return Stream.of(pages).anyMatch(page -> activePage.equals(page));
  }
}
