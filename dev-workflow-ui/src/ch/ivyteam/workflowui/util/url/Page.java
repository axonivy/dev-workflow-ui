package ch.ivyteam.workflowui.util.url;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Page {
  API_BROWSER("api-browser.xhtml"),
  CASE("case.xhtml"),
  CASES("cases.xhtml"),
  CLEANUP("cleanup.xhtml"),
  END("end.xhtml"),
  FRAME("frame.xhtml"),
  HOME("home.xhtml"),
  INTERMEDIATE_EVENT("intermediate-event.xhtml"),
  INTERMEDIATE_EVENTS("intermediate-events.xhtml"),
  LOGIN("login.xhtml"),
  PROFILE("profile.xhtml"),
  SIGNALS("signals.xhtml"),
  STARTS("starts.xhtml"),
  STATISTICS("statistics.xhtml"),
  SWITCH_USER("switch-user.xhtml"),
  TASK("task.xhtml"),
  TASKS("tasks.xhtml"),
  WEBSERVICES("webservices.xhtml");

  private static final Map<String, Page> LOOKUP_BY_ORIGIN_NAME =
      Arrays.stream(values())
          .collect(Collectors.toMap(Page::getOriginName, Function.identity()));

  private final String view;
  private final String originName;

  Page(String view) {
    this.view = view;
    this.originName = calculateOriginName(view);
  }

  public String getView() {
    return view;
  }

  public String getOriginName() {
    return originName;
  }

  private static String calculateOriginName(String view) {
    int dotIndex = view.lastIndexOf('.');
    if (dotIndex == -1) {
      return view;
    }
    return view.substring(0, dotIndex);
  }

  public static Page fromString(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return LOOKUP_BY_ORIGIN_NAME.get(value);
  }

  public static Optional<Page> of(String value) {
    return Optional.ofNullable(LOOKUP_BY_ORIGIN_NAME.get(value));
  }
}
