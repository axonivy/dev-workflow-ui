package ch.ivyteam.workflowui.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;

import ch.ivyteam.ivy.model.value.WebLink;

public class UrlUtil {

  public static String evalOriginPage() {
    return getPageString(false);
  }

  public static String evalOriginalUrl() {
    return getPageString(true);
  }

  public static String getPageString(boolean asXhtml) {
    var request = getHttpServletRequest();
    var rawPageName = getOriginParamOrPath(request);

    var pageName = rawPageName.contains(".xhtml")
        ? rawPageName.substring(0, rawPageName.indexOf(".xhtml"))
        : rawPageName;

    var baseName = "frame".equals(pageName) ? "tasks" : pageName;
    var resultPage = asXhtml ? baseName + ".xhtml" : baseName;

    var filteredQuery = removePreviousOriginParam(request);
    return resultPage + (filteredQuery == null ? "" : "?" + filteredQuery);
  }

  private static String removePreviousOriginParam(HttpServletRequest request) {
    var builder = UriBuilder.fromPath("");
    var queryString = request.getQueryString();
    if (queryString != null) {
      builder.replaceQuery(queryString);
    }

    return builder.replaceQueryParam("origin", (Object[]) null)
        .replaceQueryParam("originalUrl", (Object[]) null)
        .build()
        .getQuery();
  }

  private static String getOriginParamOrPath(HttpServletRequest request) {
    return Optional.ofNullable(request.getParameter("origin"))
        .or(() -> Optional.ofNullable(request.getParameter("originalUrl")))
        .filter(p -> !p.isBlank())
        .orElseGet(() -> getPageNameFromPath(request));
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
    return generateFrameUrl(startLink, redirectPage);
  }

  public static String generateFrameUrl(WebLink startLink, String redirectPage) {
    var originalPageEncoded = URLEncoder.encode(redirectPage, StandardCharsets.UTF_8);
    String startLinkEncoded = URLEncoder.encode(startLink.get(), StandardCharsets.UTF_8);
    return "frame.xhtml?origin=" + originalPageEncoded + "&taskUrl=" + startLinkEncoded;
  }

}
