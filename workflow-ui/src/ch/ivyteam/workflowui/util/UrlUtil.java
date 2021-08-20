package ch.ivyteam.workflowui.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class UrlUtil {

  public static String evalOriginalUrl() {
    return getHttpServletRequest().getRequestURI();
  }

  public static String evalOriginalPage() {
    return StringUtils.substringAfterLast(getHttpServletRequest().getRequestURI(), "/");
  }

  public static HttpServletRequest getHttpServletRequest() {
    var request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
            .getRequest();
    return request;
  }
}
