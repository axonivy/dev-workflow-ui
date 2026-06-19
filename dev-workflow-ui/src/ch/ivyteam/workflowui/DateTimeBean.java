package ch.ivyteam.workflowui;

import java.io.Serializable;
import java.util.Date;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

import ch.ivyteam.workflowui.util.DateUtil;

@Named
@ViewScoped
public class DateTimeBean implements Serializable {

  public String getPrettyTime(Date date) {
    return DateUtil.getPrettyTime(date);
  }

  public String getDefault(Date date) {
    return DateUtil.getDefault(date);
  }
}
