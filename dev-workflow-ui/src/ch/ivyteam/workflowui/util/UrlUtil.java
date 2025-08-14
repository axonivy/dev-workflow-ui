package ch.ivyteam.workflowui.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import ch.ivyteam.ivy.model.value.WebLink;

public class UrlUtil {

  public static String evalOriginPage() {
    var request = getHttpServletRequest();
    var originParam = getOriginParam(request);

    if (originParam != null && !originParam.isBlank()) {
      return originParam;
    }

    var currentPage = getPageNameFromPath(request);
    if (currentPage.contains(".xhtml")) {
      currentPage = currentPage.substring(0, currentPage.indexOf(".xhtml"));
    }

    var queryString = request.getQueryString();
    if (queryString != null && !queryString.isEmpty()) {
      currentPage += "?" + queryString;
    }
    return currentPage;
  }

  private static String getOriginParam(HttpServletRequest request) {
    return Optional.ofNullable(request.getParameter("origin"))
        .or(() -> Optional.ofNullable(request.getParameter("originalUrl")))
        .orElse(null);
  }

  private static String getPageNameFromPath(HttpServletRequest request) {
    var path = request.getRequestURI();
    return path.substring(path.lastIndexOf('/') + 1);
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
    var originalPageEncoded = URLEncoder.encode(redirectPage, StandardCharsets.UTF_8);
    var startLinkEncoded = URLEncoder.encode(startLink.get(), StandardCharsets.UTF_8);
    return "frame.xhtml?origin=" + originalPageEncoded + "&taskUrl=" + startLinkEncoded;
  }

}
