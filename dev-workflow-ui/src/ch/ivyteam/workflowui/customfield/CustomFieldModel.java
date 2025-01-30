package ch.ivyteam.workflowui.customfield;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.custom.field.CustomFieldType;
import ch.ivyteam.ivy.workflow.custom.field.ICustomField;
import ch.ivyteam.workflowui.util.PermissionsUtil;

public class CustomFieldModel {

  private final String name;
  private final String value;
  private final String type;
  private final String label;
  private final String description;
  private final String valueDescription;

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
    this.value = field.get().map(val -> toValueLabel(field, val)).orElse("<null>");
    this.valueDescription = field.get().map(val -> toValueDescription(value, val)).orElse("<null>");
    this.type = field.type().toString();
    this.label = field.meta().label();
    this.description = toDescription(field, label);
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public String getValueDescription() {
    return valueDescription;
  }

  public String getType() {
    return type;
  }

  public String getLabel() {
    return label;
  }

  public String getLabelAndName() {
    if (name.equals(label)) {
      return name;
    }
    return label + " (" + name + ")";
  }

  public String getDescription() {
    return description;
  }

  private static boolean designerOrNotHidden(ICustomField<?> customField) {
    return PermissionsUtil.isDemoOrDevMode() || !customField.meta().isHidden();
  }

  private static String toValueLabel(ICustomField<?> field, Object value) {
    if (field.type() == CustomFieldType.STRING || field.type() == CustomFieldType.NUMBER) {
      return field.meta().values().label(value);
    }
    return Objects.toString(value);
  }

  private static String toValueDescription(String valueLabel, Object value) {
    String valStr = Objects.toString(value);
    if (valStr.equals(valueLabel)) {
      return valueLabel;
    }
    return valueLabel + " (" + valStr + ")";
  }

  private static String toDescription(ICustomField<?> field, String label) {
    var desc = field.meta().description();
    if (Objects.equals(label, desc)) {
      return "";
    }
    return desc;
  }

}
