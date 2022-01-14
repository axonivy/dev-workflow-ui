package ch.ivyteam.workflowui.starts;

import ch.ivyteam.workflowui.customfield.CustomFieldModel;
import ch.ivyteam.workflowui.tasks.TaskModel;

public class CustomFieldsHelper {

  public static final String EMBED_IN_FRAME = "embedInFrame";

  private TaskModel task;

  public CustomFieldsHelper(TaskModel task) {
    this.task = task;
  }

  public CustomFieldModel find(String fieldName) {
    return task.getCustomFields().stream()
            .filter(field -> field.getName().equals(fieldName))
            .findAny().orElse(null);
  }

  public CustomFieldModel getEmbedInFrame() {
    return task.getCustomFields().stream()
            .filter(field -> field.getName().equals(EMBED_IN_FRAME))
            .findAny().orElse(null);
  }

}
