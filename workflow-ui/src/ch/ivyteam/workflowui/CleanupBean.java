package ch.ivyteam.workflowui;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.di.restricted.DiCore;
import ch.ivyteam.ivy.business.data.store.restricted.BusinessDataPersistence;
import ch.ivyteam.ivy.rest.client.oauth2.SessionTokenStore;
import ch.ivyteam.ivy.workflow.IWorkflowContext;

@SuppressWarnings("restriction")
@ManagedBean
@ViewScoped
public class CleanupBean
{
  private boolean casesTasksAndDependent = true;
  private boolean businessDataAndSearchIndex = true;
  private boolean identityProviderTokens = true;

  public boolean isCasesTasksAndDependent()
  {
    return casesTasksAndDependent;
  }

  public void setCasesTasksAndDependent(boolean casesTasksAndDependent)
  {
    this.casesTasksAndDependent = casesTasksAndDependent;
  }

  public boolean isBusinessDataAndSearchIndex()
  {
    return businessDataAndSearchIndex;
  }

  public void setBusinessDataAndSearchIndex(boolean businessDataAndSearchIndex)
  {
    this.businessDataAndSearchIndex = businessDataAndSearchIndex;
  }

  public boolean isIdentityProviderTokens()
  {
    return identityProviderTokens;
  }

  public void setIdentityProviderTokens(boolean identityProviderTokens)
  {
    this.identityProviderTokens = identityProviderTokens;
  }

  public void cleanup()
  {
    String msg = "";
    if (casesTasksAndDependent)
    {
      IWorkflowContext.current().cleanup();
      msg += "Cleaned all Cases and Tasks;";
    }
    if (businessDataAndSearchIndex)
    {
      DiCore.getGlobalInjector().getInstance(BusinessDataPersistence.class).clearAll();
      msg += "Cleaned all Business Data and the search index;";
    }
    if (identityProviderTokens)
    {
      SessionTokenStore.clear();
      msg += "Cleaned all identity provider tokens;";
    }
    if (!StringUtils.isBlank(msg))
    {
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg));
    }
  }

}
