package ch.ivyteam.workflowui.util;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

public class ResponseHelper {

  public static void notFound(String msg) {
    try {
      var facesContext = FacesContext.getCurrentInstance();
      var externalContext = facesContext.getExternalContext();
      facesContext.responseComplete();
      externalContext.responseSendError(HttpServletResponse.SC_NOT_FOUND, msg);
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }

  public static void noPermission(String msg) {
    try {
      var facesContext = FacesContext.getCurrentInstance();
      var externalContext = facesContext.getExternalContext();
      facesContext.responseComplete();
      externalContext.responseSendError(HttpServletResponse.SC_FORBIDDEN, msg);
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }
}
