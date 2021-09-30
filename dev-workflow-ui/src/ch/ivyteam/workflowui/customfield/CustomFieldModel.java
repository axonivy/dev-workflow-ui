package ch.ivyteam.workflowui.customfield;

import java.util.List;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.custom.field.ICustomField;

public class CustomFieldModel {

  private final String name;
  private final String value;
  private final String type;

  public static List<CustomFieldModel> create(ICase selectedCase) {
    return selectedCase.customFields().all().stream()
            .map(CustomFieldModel::new).collect(Collectors.toList());
  }

  public static List<CustomFieldModel> create(ITask task) {
    return task.customFields().all().stream()
            .map(CustomFieldModel::new).collect(Collectors.toList());
  }

  public CustomFieldModel(ICustomField<?> field) {
    this.name = field.name();
    this.value = field.get().map(Object::toString).orElse("<null>");
    this.type = field.type().toString();
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public String getType() {
    return type;
  }
}
