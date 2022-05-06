package ch.ivyteam.workflowui.starts;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.model.value.WebLink;
import ch.ivyteam.ivy.workflow.start.IWebStartable;
import ch.ivyteam.workflowui.util.UrlUtil;

public class CaseMapStartableModel extends StartableModel {
  private final WebLink viewerLink;
  private final String caseMapUuid;

  public CaseMapStartableModel(IWebStartable startable, String pmvVersionName) {
    super(startable);
    this.caseMapUuid = getCaseMapUuid(startable.getLink());
    this.viewerLink = createCaseMapLink(caseMapUuid, pmvVersionName);
  }

  private static WebLink createCaseMapLink(String caseMapUuid, String pmvVersionName) {
    UUID uuid = UUID.fromString(caseMapUuid);
    String caseMapLink = UrlUtil.generateCaseMapUrl(pmvVersionName, uuid);
    return new WebLink(caseMapLink);
  }

  private static String getCaseMapUuid(WebLink link) {
    String icm = StringUtils.substringAfterLast(link.getAbsolute(), "/");
    return StringUtils.substringBefore(icm, ".icm");
  }

  @Override
  public boolean isProcessStart() {
    return false;
  }

  public String getCaseMapUuid() {
    return caseMapUuid;
  }

  @Override
  public WebLink getViewerLink() {
    return viewerLink;
  }

}
