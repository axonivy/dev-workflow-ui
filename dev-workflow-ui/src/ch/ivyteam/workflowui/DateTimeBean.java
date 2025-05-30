package ch.ivyteam.workflowui;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.workflowui.util.DateUtil;

@ManagedBean
@ViewScoped
public class DateTimeBean {

  public String getPrettyTime(Date date) {
    return DateUtil.getPrettyTime(date);
  }

  public String getDefault(Date date) {
    return DateUtil.getDefault(date);
  }
}
