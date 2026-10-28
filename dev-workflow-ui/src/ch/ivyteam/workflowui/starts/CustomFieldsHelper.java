package ch.ivyteam.workflowui.starts;

import ch.ivyteam.workflowui.customfield.CustomFieldModel;
import ch.ivyteam.workflowui.tasks.TaskDetailModel;

public class CustomFieldsHelper {

  public static final String EMBED_IN_FRAME = "embedInFrame";

  private final TaskDetailModel task;

  public CustomFieldsHelper(TaskDetailModel task) {
    this.task = task;
  }

  public CustomFieldModel find(String fieldName) {
    return task.getCustomFields().stream()
        .filter(field -> field.getName().equals(fieldName))
        .findAny().orElse(null);
  }

  public CustomFieldModel getEmbedInFrame() {
    return task.getCustomFields().stream()
        .filter(field -> EMBED_IN_FRAME.equals(field.getName()))
        .findAny().orElse(null);
  }

}
