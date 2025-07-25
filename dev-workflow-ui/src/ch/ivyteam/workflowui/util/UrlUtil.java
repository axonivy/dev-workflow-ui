package ch.ivyteam.workflowui.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.model.value.WebLink;

public class UrlUtil {

  public static String evalOriginPage() {
    var pageXhtml = StringUtils.substringAfterLast(getHttpServletRequest().getRequestURI(), "/");
    var page = StringUtils.substringBefore(pageXhtml, ".xhtml");
    var parameter = getHttpServletRequest().getQueryString();
    if ("frame".equals(page)) {
      page = "tasks";
    }
    if (!StringUtils.isBlank(parameter)) {
      page += "?" + parameter;
    }
    return page;
  }

  public static HttpServletRequest getHttpServletRequest() {
    return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
        .getRequest();
  }

  public static String getUrlParameter(String parameter) {
    return getHttpServletRequest().getParameter(parameter);
  }

  public static String generateStartFrameUrl(WebLink startLink) {
    return generateStartFrameUrl(startLink, evalOriginPage());
  }

  public static String generateStartFrameUrl(WebLink startLink, String redirectPage) {
    return generateFrameUrl(startLink, redirectPage);
  }

  public static String generateFrameUrl(WebLink startLink, String redirectPage) {
    var originalPageEncoded = URLEncoder.encode(redirectPage, StandardCharsets.UTF_8);
    String startLinkEncoded = URLEncoder.encode(startLink.get(), StandardCharsets.UTF_8);
    return "frame.xhtml?origin=" + originalPageEncoded + "&taskUrl=" + startLinkEncoded;
  }

}
