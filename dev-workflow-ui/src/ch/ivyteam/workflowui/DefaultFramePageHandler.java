package ch.ivyteam.workflowui;

import java.io.IOException;

import ch.ivyteam.ivy.application.app.Application;
import ch.ivyteam.ivy.dialog.execution.api.DialogInstance;
import ch.ivyteam.ivy.request.IHttpResponse;
import ch.ivyteam.util.uri.UriChecker;
import ch.ivyteam.workflowui.starts.CustomFieldsHelper;
import ch.ivyteam.workflowui.tasks.TaskModel;

public class DefaultFramePageHandler {

  public static void handleRedirect(String relativeUrl) throws IOException {
    if (!UriChecker.isRelative(relativeUrl)) {
      throw new RuntimeException("Redirecting to external websites is not allowed. Tried to redirect to: " + relativeUrl);
    }
    var task = DialogInstance.of(relativeUrl).task();
    var url = relativeUrl;
    if (task == null || !shouldEscapeIFrame(new TaskModel(task))) {
      url = Application.current().contextPath() + "/faces/view/dev-workflow-ui/frame.xhtml?taskUrl=" + relativeUrl;
    }
    IHttpResponse.current().sendRedirect(url);
  }

  private static boolean shouldEscapeIFrame(TaskModel task) {
    var taskEmbedField = new CustomFieldsHelper(task).getEmbedInFrame();
    var caseEmbedField = task.getBusinessCase().customFields().stringField(CustomFieldsHelper.EMBED_IN_FRAME);
    if (taskEmbedField == null) {
      return "false".equals(caseEmbedField.getOrNull());
    }
    return "false".equals(taskEmbedField.getValue());
  }
}
