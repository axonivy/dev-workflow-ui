package ch.ivyteam.ivy.project.workflow.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import ch.ivyteam.util.IAttributeStore;
import ch.ivyteam.workflowui.starts.StartableModel;
import ch.ivyteam.workflowui.util.LastSessionStarts;

public class TestLastSessionStarts {

  @Test
  void storeStartsOnSession() {
    var starts = new LastSessionStarts(new MapAttributeStore());

    assertThat(starts.getAll()).isEmpty();

    starts.add(new StartableModel("myown", null, null, null, null, null));
    assertThat(starts.getAll()).hasSize(1);
  }

  private static class MapAttributeStore implements IAttributeStore<List<StartableModel>>  {

    private final Map<String, List<StartableModel>> starts = new HashMap<>();

    @Override
    public List<StartableModel> setAttribute(String attributeName, List<StartableModel> attributeValue) {
      return starts.put(attributeName, attributeValue);
    }

    @Override
    public List<StartableModel> getAttribute(String attributeName) {
      return starts.get(attributeName);
    }

    @Override
    public List<StartableModel> removeAttribute(String attributeName) {
      return starts.remove(attributeName);
    }

    @Override
    public Set<String> getAttributeNames() {
      return starts.keySet();
    }
  }

}
