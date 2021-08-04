package ch.ivyteam.workflowui;

import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.servlet.RequestScoped;

import ch.ivyteam.ivy.application.IApplication;

@ManagedBean
@RequestScoped
public class SidebarBean {
  private final String activePage;

  public SidebarBean() {
    String currentUrl = FacesContext.getCurrentInstance().getViewRoot().getViewId();
    var page = StringUtils.substringAfterLast(currentUrl, "/");
    this.activePage = StringUtils.substringBeforeLast(page, ".");
  }

  public boolean isActive(String... pages) {
    return Stream.of(pages).anyMatch(page -> activePage.equals(page));
  }

  public String getAppContextPath() {
    return IApplication.current().getContextPath();
  }
}
