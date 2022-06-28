package ch.ivyteam.workflowui.profile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.language.LanguageManager;
import ch.ivyteam.ivy.language.LanguageRepository;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.ISessionInternal;
import ch.ivyteam.ivy.security.IUser;

@ManagedBean
public class ProfileBean {

  private Locale contentLocale;
  private Locale formattingLocale;

  public ProfileBean() {
    contentLocale = user().getLanguage();
    formattingLocale = user().getFormattingLanguage();
  }

  public String getUserName() {
    return user().getName();
  }

  public String getFullName() {
    return user().getFullName();
  }

  public String getEmail() {
    return user().getEMailAddress();
  }

  public String getRoles() {
    return user().getAllRoles().stream().map(IRole::getName).collect(Collectors.joining(", "));
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
    return locales(LanguageRepository::allContent, contentLocale);
  }

  public List<Locale> getFormattingLanguages() {
    return locales(LanguageRepository::allFormatting, formattingLocale);
  }

  public String toDisplayName(Locale locale) {
    if (Locale.ROOT.equals(locale) || locale == null) {
      return "";
    }
    return locale.getDisplayName(getCurrentContentLocale()) + " (" + locale.toString() + ")";
  }

  public String getContentLanguageSource() {
    return "current language " + getCurrentContentLocale().toString() + " (" + session().getContentLocaleInfo().source() + ")";
  }

  public String getFormattingLanguageSource() {
    return "current formatting language " + getCurrentFormattingLocale().toString() + " (" + session().getFormattingLocaleInfo().source() + ")";
  }

  public void save() {
    user().setLanguage(contentLocale);
    user().setFormattingLanguage(formattingLocale);
  }

  private List<Locale> locales(Function<LanguageRepository, List<Locale>> loader, Locale currentLocale) {
    var locales = loader.apply(LanguageManager.instance().languages(session().getSecurityContext())).stream()
            .filter(l -> !l.equals(currentLocale))
            .sorted(Comparator.comparing(Locale::getDisplayName))
            .collect(Collectors.toList());
    var l = new ArrayList<Locale>();
    l.add(Locale.ROOT);
    if (currentLocale != null) {
      l.add(currentLocale);
    }
    l.addAll(locales);
    return l;
  }
}
