package ch.ivyteam.workflowui.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.model.value.WebLink;

public class UrlUtil {

  public static String evalOriginalUrl() {
    return getHttpServletRequest().getRequestURI();
  }

  public static String evalOriginalPage() {
    var page = StringUtils.substringAfterLast(getHttpServletRequest().getRequestURI(), "/");
    var parameter = getHttpServletRequest().getQueryString();
    if (page.equals("frame.xhtml")) {
      page = "allTasks.xhtml";
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
    return generateStartFrameUrl(startLink, evalOriginalPage());
  }

  public static String generateStartFrameUrl(WebLink startLink, String redirectUrl) {
    return generateFrameUrl(startLink, redirectUrl);
  }

  public static String generateFrameUrl(WebLink startLink, String redirectUrl) {
    var originalPageEncoded = URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);
    String startLinkEncoded = URLEncoder.encode(startLink.getRelative(), StandardCharsets.UTF_8);
    return "frame.xhtml?originalUrl=" + originalPageEncoded + "&taskUrl=" + startLinkEncoded;
  }

  public static String generateProcessViewerUrl(WebLink viewerLink) {
    String viewerLinkEncoded = URLEncoder.encode(viewerLink.getRelative(), StandardCharsets.UTF_8);
    return "process-viewer.xhtml?viewerUrl=" + viewerLinkEncoded;
  }

  public static String generateProcessViewerUrl(String viewerLink) {
    String viewerLinkEncoded = URLEncoder.encode(viewerLink, StandardCharsets.UTF_8);
    return "process-viewer.xhtml?viewerUrl=" + viewerLinkEncoded;
  }
}
