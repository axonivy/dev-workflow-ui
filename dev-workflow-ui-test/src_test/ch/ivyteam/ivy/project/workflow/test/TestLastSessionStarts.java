package ch.ivyteam.ivy.project.workflow.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.application.restricted.di.ILocalizedTextResolver;
import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.workflow.category.Category;
import ch.ivyteam.ivy.workflow.category.CategoryPath;
import ch.ivyteam.util.IAttributeStore;
import ch.ivyteam.workflowui.starts.StartableModel;
import ch.ivyteam.workflowui.util.LastSessionStarts;

public class TestLastSessionStarts {

  @Test
  void storeStartsOnSession() {
    var starts = new LastSessionStarts(new  MapAttributeStore());

    assertThat(starts.getAll()).isEmpty();
    CategoryPath path = new CategoryPath("tara");
    var link = new WebLink("http://localhost/ivy/nowhere");

    starts.add(new StartableModel("myown", "myDesc", link, Category.createFor(path, new LocalizedTextAdapter()), "",""));
    assertThat(starts.getAll()).hasSize(1);
  }

  private static class MapAttributeStore implements IAttributeStore<List<StartableModel>>  {

    private Map<String, List<StartableModel>> starts = new HashMap<>();

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
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Set<String> getAttributeNames() {
      // TODO Auto-generated method stub
      return null;
    }
  }

  private static class LocalizedTextAdapter implements ILocalizedTextResolver {

    @Override
    public String getAbsolutePath(String path) {
      return null;
    }

    @Override
    public String getLocalizedText(String arg0, Locale arg1) {
      return null;
    }

  }

}
