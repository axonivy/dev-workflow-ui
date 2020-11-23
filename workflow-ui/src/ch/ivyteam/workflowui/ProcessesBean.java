package ch.ivyteam.workflowui;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.workflowui.util.RedirectUtil;

@ManagedBean
@ViewScoped
public class ProcessesBean
{
  private StartsDataModel startsDataModel;

  public ProcessesBean()
  {
    startsDataModel = new StartsDataModel();
    startsDataModel.setFilter("");
  }

  public StartsDataModel getStartsDataModel()
  {
    return startsDataModel;
  }

  public List<CustomPMV> getPMVs()
  {
    return startsDataModel.getPMVs();
  }

  public String getFilter()
  {
    return this.startsDataModel.getFilter();
  }

  public void setFilter(String filter)
  {
    this.startsDataModel.setFilter(filter);
  }

  public void redirect()
  {
    RedirectUtil.redirect("home.xhtml");
  }
}
