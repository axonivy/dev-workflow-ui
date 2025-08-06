package ch.ivyteam.workflowui.profile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ch.ivyteam.ivy.language.LanguageManager;
import ch.ivyteam.ivy.language.LanguageRepository;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.ISessionInternal;
import ch.ivyteam.ivy.security.IUser;

@ManagedBean
@ViewScoped
public class ProfileBean {

  private String fullName;
  private String email;
  private Locale contentLocale;
  private Locale formattingLocale;
  private final NotificationChannelDataModel notificationChannelDataModel;

  public ProfileBean() {
    fullName = user().getFullName();
    email = user().getEMailAddress();
    contentLocale = user().getLanguage();
    formattingLocale = user().getFormattingLanguage();
    notificationChannelDataModel = NotificationChannelDataModel.instance(user());
    notificationChannelDataModel.onload();
  }

  public String getUserName() {
    return user().getName();
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<String> getRoles() {
    return user().getAllRoles().stream().map(IRole::getName).toList();
  }

  public Locale getCurrentContentLocale() {
    return session().getContentLocale();
  }

  public Locale getCurrentFormattingLocale() {
    return session().getFormattingLocale();
  }

  public void setContentLanguage(Locale locale) {
    this.contentLocale = locale;
  }

  public Locale getContentLanguage() {
    return user().getLanguage();
  }

  public Locale getFormattingLanguage() {
    return user().getFormattingLanguage();
  }

  public void setFormattingLanguage(Locale locale) {
    this.formattingLocale = locale;
  }

  public IUser user() {
    return session().getSessionUser();
  }

  public ISessionInternal session() {
    return (ISessionInternal) ISession.current();
  }

  public List<Locale> getContentLanguages() {
    return locales(LanguageRepository::allContent);
  }

  public List<Locale> getFormattingLanguages() {
    return locales(LanguageRepository::allFormatting);
  }

  public String toDisplayName(Locale locale) {
    if (Locale.ROOT.equals(locale) || locale == null) {
      return "";
    }
    return locale.getDisplayLanguage(getCurrentContentLocale()) + " (" + locale.toString() + ")";
  }

  public String getContentLanguageSource() {
    return "<b>" + getCurrentContentLocale().toString() + "</b> (" + session().getContentLocaleInfo().source() + ")";
  }

  public String getFormattingLanguageSource() {
    return "<b>" + getCurrentFormattingLocale().toString() + "</b> (" + session().getFormattingLocaleInfo().source() + ")";
  }

  public NotificationChannelDataModel getNotificationChannels() {
    return notificationChannelDataModel;
  }

  public void save() {
    user().setFullName(fullName);
    user().setEMailAddress(email);
    user().setLanguage(contentLocale);
    user().setFormattingLanguage(formattingLocale);
    notificationChannelDataModel.save();
    var msg = new FacesMessage("Profile saved");
    FacesContext.getCurrentInstance().addMessage("info", msg);
  }

  private List<Locale> locales(Function<LanguageRepository, List<Locale>> loader) {
    var locales = loader.apply(LanguageManager.instance().languages(session().getSecurityContext())).stream()
        .sorted(Comparator.comparing(this::toDisplayName, String.CASE_INSENSITIVE_ORDER))
        .collect(Collectors.toList());
    var l = new ArrayList<Locale>();
    l.add(Locale.ROOT);
    l.addAll(locales);
    return l;
  }
}
