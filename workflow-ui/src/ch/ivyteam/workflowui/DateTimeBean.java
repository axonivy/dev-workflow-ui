package ch.ivyteam.workflowui;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.workflowui.util.DateUtil;

@ManagedBean
@ViewScoped
public class DateTimeBean
{
  public String getPrettyTime(Date date)
  {
    return DateUtil.getPrettyTime(date);
  }

  public String getDateTime(Date date)
  {
    return DateUtil.getDateAndTime(date);
  }

  public String getDetailTime(Date date)
  {
    return date == null ? "N/A" : DateUtil.getDateAndTime(date);
  }
}
