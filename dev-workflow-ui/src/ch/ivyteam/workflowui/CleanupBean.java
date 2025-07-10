package ch.ivyteam.workflowui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ch.ivyteam.ivy.engine.cleanup.EngineCleanup;

@ManagedBean
@ViewScoped
public class CleanupBean {

  public List<String> cleanupIds = new ArrayList<>();

  public List<EngineCleanupDTO> getCleanups() {
    return EngineCleanup.all().stream()
        .map(EngineCleanupDTO::new)
        .collect(Collectors.toList());
  }

  public void cleanup() {
    EngineCleanup.clean(cleanupIds);
    var msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Data has been cleaned");
    FacesContext.getCurrentInstance().addMessage(null, msg);
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
      this.name = cleanup.name();
    }

    public String getId() {
      return id;
    }

    public String getName() {
      return name;
    }
  }
}
