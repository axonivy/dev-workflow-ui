package ch.ivyteam.ivy.project.workflow.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.environment.IvyTest;
import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.util.IAttributeStore;
import ch.ivyteam.workflowui.starts.StartableModel;
import ch.ivyteam.workflowui.util.LastSessionStarts;

@IvyTest
class TestLastSessionStarts {

  @Test
  void storeStartsOnSession() {
    var starts = new LastSessionStarts(new MapAttributeStore());

    assertThat(starts.storedStarts()).isNull();
    var startable = new StartableModel("myown", null, WebLink.of("test"), null, null, null, null, true, null, false);

    starts.add(startable);
    assertThat(starts.storedStarts()).hasSize(1);

    starts.add(startable);
    assertThat(starts.storedStarts()).as("shouldn't add duplicates").hasSize(1);
  }

  private static class MapAttributeStore implements IAttributeStore<Set<StartableModel>>  {

    private final Map<String, Set<StartableModel>> starts = new HashMap<>();

    @Override
    public Set<StartableModel> setAttribute(String attributeName, Set<StartableModel> attributeValue) {
      return starts.put(attributeName, attributeValue);
    }

    @Override
    public Set<StartableModel> getAttribute(String attributeName) {
      return starts.get(attributeName);
    }

    @Override
    public Set<StartableModel> removeAttribute(String attributeName) {
      return starts.remove(attributeName);
    }

    @Override
    public Set<String> getAttributeNames() {
      return starts.keySet();
    }
  }
}
