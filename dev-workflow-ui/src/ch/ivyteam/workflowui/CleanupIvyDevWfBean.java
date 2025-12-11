package ch.ivyteam.workflowui;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ch.ivyteam.ivy.business.data.store.restricted.BusinessDataPersistence;
import ch.ivyteam.ivy.data.cache.restricted.IDataCacheManager;
import ch.ivyteam.ivy.rest.client.oauth2.SessionTokenStore;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.workflow.IWorkflowContext;

@SuppressWarnings("restriction")
@ManagedBean
@ViewScoped
public class CleanupIvyDevWfBean {
  private boolean casesTasksAndDependent = true;
  private boolean businessDataAndSearchIndex = true;
  private boolean identityProviderTokens = true;
  private boolean dataCaches = true;

  public boolean isCasesTasksAndDependent() {
    return casesTasksAndDependent;
  }

  public void setCasesTasksAndDependent(boolean casesTasksAndDependent) {
    this.casesTasksAndDependent = casesTasksAndDependent;
  }

  public boolean isBusinessDataAndSearchIndex() {
    return businessDataAndSearchIndex;
  }

  public void setBusinessDataAndSearchIndex(boolean businessDataAndSearchIndex) {
    this.businessDataAndSearchIndex = businessDataAndSearchIndex;
  }

  public boolean isIdentityProviderTokens() {
    return identityProviderTokens;
  }

  public void setIdentityProviderTokens(boolean identityProviderTokens) {
    this.identityProviderTokens = identityProviderTokens;
  }

  public boolean isDataCaches() {
    return dataCaches;
  }

  public void setDataCaches(boolean dataCaches) {
    this.dataCaches = dataCaches;
  }

  public void cleanup() {
    var context = ISecurityContext.current();
    if (context == null) {
      return;
    }
    if (casesTasksAndDependent) {
      IWorkflowContext.of(context).cleanup();
      showMessage("All existing Cases and Tasks have been deleted");
    }
    if (businessDataAndSearchIndex) {
      BusinessDataPersistence.instance().clear(context);
      showMessage("All Business Data and the search index has been deleted");
    }
    if (identityProviderTokens) {
      SessionTokenStore.clear(context);
      showMessage("All identity provider tokens have been deleted");
    }
    if (dataCaches) {
      IDataCacheManager.instance().invalidate(context);
      showMessage("All data caches have been cleared");
    }
  }

  private void showMessage(String msg) {
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg));
  }

}
