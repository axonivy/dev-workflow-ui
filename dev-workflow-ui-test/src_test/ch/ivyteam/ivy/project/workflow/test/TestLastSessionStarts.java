package ch.ivyteam.ivy.project.workflow.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.util.IAttributeStore;
import ch.ivyteam.workflowui.starts.StartableModel;
import ch.ivyteam.workflowui.util.LastSessionStarts;

public class TestLastSessionStarts {

  @Test
  void storeStartsOnSession() {
    var starts = new LastSessionStarts(new MapAttributeStore());

    assertThat(starts.getAll()).isEmpty();
    var startable = new StartableModel("myown", null, new WebLink("test"), null, null, true, null);

    starts.add(startable);
    assertThat(starts.getAll()).hasSize(1);
    // shouldn't add duplicates
    starts.add(startable);
    assertThat(starts.getAll()).hasSize(1);
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
