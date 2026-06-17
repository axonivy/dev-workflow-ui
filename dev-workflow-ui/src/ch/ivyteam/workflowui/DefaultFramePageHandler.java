package ch.ivyteam.workflowui;

import java.io.IOException;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.request.IHttpResponse;

public class DefaultFramePageHandler {

  public static void handleRedirect(String relativeUrl) throws IOException {
    if(relativeUrl != null && !UriChecker.isRelative(relativeUrl)) {
      throw new RuntimeException("Redirecting to external websites is not allowed. Tried to redirect to: " + relativeUrl);
    }
    var url = IApplication.current().getContextPath()+"/faces/view/dev-workflow-ui/frame.xhtml?taskUrl=" + relativeUrl;
    IHttpResponse.current().sendRedirect(url);
  }
}
