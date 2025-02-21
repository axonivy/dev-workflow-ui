package ch.ivyteam.ivy.project.workflow.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.workflow.custom.field.CustomFieldType;
import ch.ivyteam.ivy.workflow.custom.field.ICustomField;
import ch.ivyteam.ivy.workflow.custom.field.ICustomFieldMeta;
import ch.ivyteam.ivy.workflow.custom.field.ICustomFieldValues;
import ch.ivyteam.workflowui.customfield.CustomFieldModel;

class TestCustomFieldModel {

  ICustomField<?> customField = new CustomFieldMock();
  String value = "Silver";
  String label = "Kundenart";
  String valueLabel = "Silber";
  String description = "Die Art des Kunden";
  CustomFieldModel testee = new CustomFieldModel(customField);

  @Test
  void name() {
    assertThat(testee.getName()).isEqualTo("CustomerKind");
  }

  @Test
  void label() {
    assertThat(testee.getLabel()).isEqualTo("Kundenart");
  }

  @Test
  void label_noLocalization() {
    label = null;
    testee = new CustomFieldModel(customField);
    assertThat(testee.getLabel()).isEqualTo("CustomerKind");
  }

  @Test
  void labelAndName() {
    assertThat(testee.getLabelAndName()).isEqualTo("Kundenart (CustomerKind)");
  }

  @Test
  void labelAndName_noLocalization() {
    label = null;
    testee = new CustomFieldModel(customField);
    assertThat(testee.getLabelAndName()).isEqualTo("CustomerKind");
  }

  @Test
  void value() {
    assertThat(testee.getValue()).isEqualTo("Silber");
  }

  @Test
  void value_noLocalization() {
    valueLabel = null;
    testee = new CustomFieldModel(customField);
    assertThat(testee.getValue()).isEqualTo("Silver");
  }

  @Test
  void value_noValue() {
    value = null;
    testee = new CustomFieldModel(customField);
    assertThat(testee.getValue()).isEqualTo("<null>");
  }

  @Test
  void valueDescription() {
    assertThat(testee.getValueDescription()).isEqualTo("Silber (Silver)");
  }

  @Test
  void valueDescription_noLocalization() {
    valueLabel = null;
    testee = new CustomFieldModel(customField);
    assertThat(testee.getValueDescription()).isEqualTo("Silver");
  }

  @Test
  void valueDescription_noValue() {
    value = null;
    testee = new CustomFieldModel(customField);
    assertThat(testee.getValueDescription()).isEqualTo("<null>");
  }

  @Test
  void description() {
    assertThat(testee.getDescription()).isEqualTo("Die Art des Kunden");
  }

  @Test
  void description_noLocalization() {
    description = null;
    testee = new CustomFieldModel(customField);
    assertThat(testee.getDescription()).isEqualTo("");
  }

  @Test
  void type() {
    assertThat(testee.getType()).isEqualTo("STRING");
  }

  private class CustomFieldMock implements ICustomField<String> {

    @Override
    public Optional<String> get() {
      return Optional.ofNullable(value);
    }

    @Override
    public String getOrNull() {
      return null;
    }

    @Override
    public String getOrDefault(String defaultValue) {
      return null;
    }

    @Override
    public boolean isPresent() {
      return false;
    }

    @Override
    public void set(String value) {

    }

    @Override
    public boolean compareAndSet(String expectedValue, String newValue) {
      return false;
    }

    @Override
    public void delete() {

    }

    @Override
    public String name() {
      return "CustomerKind";
    }

    @Override
    public CustomFieldType type() {
      return CustomFieldType.STRING;
    }

    @Override
    public ICustomFieldMeta meta() {
      return new CustomFieldMetaMock();
    }
  }

  private class CustomFieldMetaMock implements ICustomFieldMeta {

    @Override
    public String name() {
      return "CustomerKind";
    }

    @Override
    public String label() {
      if (label == null) {
        return name();
      }
      return label;
    }

    @Override
    public String description() {
      if (description == null) {
        return label();
      }
      return description;
    }

    @Override
    public String category() {
      return null;
    }

    @Override
    public CustomFieldType type() {
      return CustomFieldType.STRING;
    }

    @Override
    public boolean isHidden() {
      return false;
    }

    @Override
    public String attribute(String attributeName) {
      return null;
    }

    @Override
    public Iterable<String> attributeNames() {
      return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ICustomFieldValues<T> values() {
      return (ICustomFieldValues<T>) new CustomFieldValuesMock();
    }
  }

  private class CustomFieldValuesMock implements ICustomFieldValues<String> {

    @Override
    public Iterable<String> all() {
      return null;
    }

    @Override
    public Iterable<String> matching(String searchPattern) {
      return null;
    }

    @Override
    public String label(String val) {
      if (valueLabel != null) {
        return valueLabel;
      }
      return val;
    }

    @Override
    public List<ValueLabel<String>> labels() {
      return List.of();
    }
  }
}
