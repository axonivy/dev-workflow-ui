package ch.ivyteam.workflowui.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class UrlUtil {

  public static String evalOriginalUrl() {
    var request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    return request.getRequestURI();
  }

  public static String evalOriginalPage() {
    var request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    return StringUtils.substringAfterLast(request.getRequestURI(), "/");
  }
}
