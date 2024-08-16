package ch.ivyteam.workflowui.customfield;

import java.util.List;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.custom.field.ICustomField;
import ch.ivyteam.workflowui.util.PermissionsUtil;

public class CustomFieldModel {

  private final String name;
  private final String value;
  private final String type;
  private final String label;
  private final String description;

  public static List<CustomFieldModel> create(ICase selectedCase) {
    return selectedCase.customFields().all().stream()
            .filter(CustomFieldModel::designerOrNotHidden)
            .map(CustomFieldModel::new).collect(Collectors.toList());
  }

  public static List<CustomFieldModel> create(ITask task) {
    return task.customFields().all().stream()
            .filter(CustomFieldModel::designerOrNotHidden)
            .map(CustomFieldModel::new).collect(Collectors.toList());
  }

  public CustomFieldModel(ICustomField<?> field) {
    this.name = field.name();
    this.value = field.get().map(Object::toString).orElse("<null>");
    this.type = field.type().toString();
    this.label = field.meta().label();
    this.description = field.meta().description();
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

  public String getLabel() {
    return label;
  }

  public String getDescription() {
    return description;
  }

  private static boolean designerOrNotHidden(ICustomField<?> customField) {
    return PermissionsUtil.isDemoOrDevMode() || ! customField.meta().isHidden();
  }
}
