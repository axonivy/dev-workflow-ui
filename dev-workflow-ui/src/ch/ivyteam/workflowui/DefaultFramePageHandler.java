package ch.ivyteam.workflowui;

import java.io.IOException;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.dialog.execution.api.DialogInstance;
import ch.ivyteam.ivy.request.IHttpResponse;
import ch.ivyteam.workflowui.starts.CustomFieldsHelper;
import ch.ivyteam.workflowui.tasks.TaskModel;

public class DefaultFramePageHandler {

  public static void handleRedirect(String relativeUrl) throws IOException {
    var task = DialogInstance.of(relativeUrl).task();
    var url = relativeUrl;
    if (task == null || !shouldEscapeIFrame(new TaskModel(task))) {
      url = IApplication.current().getContextPath() + "/faces/view/dev-workflow-ui/frame.xhtml?taskUrl=" + relativeUrl;
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
