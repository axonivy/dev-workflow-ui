package ch.ivyteam.workflowui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import ch.ivyteam.ivy.engine.cleanup.EngineCleanup;
import ch.ivyteam.ivy.environment.Ivy;

@Named
@ViewScoped
public class CleanupBean implements Serializable {

  private List<String> cleanupIds = new ArrayList<>();

  public List<EngineCleanupDTO> getCleanups() {
    return EngineCleanup.all().stream()
        .map(EngineCleanupDTO::new)
        .collect(Collectors.toList());
  }

  public void cleanup() {
    EngineCleanup.clean(cleanupIds);
    if (!cleanupIds.isEmpty()) {
      var msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Data has been cleaned");
      FacesContext.getCurrentInstance().addMessage(null, msg);
    }
  }

  public List<String> getCleanupIds() {
    return cleanupIds;
  }

  public void setCleanupIds(List<String> cleanupIds) {
    this.cleanupIds = cleanupIds;
  }

  public static class EngineCleanupDTO {

    private final String id;
    private final String name;

    public EngineCleanupDTO(EngineCleanup cleanup) {
      this.id = cleanup.id();
      this.name = Ivy.cm().co("/cleanup/cleanups/" + cleanup.name());
    }

    public String getId() {
      return id;
    }

    public String getName() {
      return name;
    }
  }
}
