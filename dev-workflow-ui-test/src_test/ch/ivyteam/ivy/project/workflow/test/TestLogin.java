package ch.ivyteam.ivy.project.workflow.test;

import static ch.ivyteam.workflowui.util.UserUtil.getRoles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.environment.IvyTest;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.workflowui.login.LoginUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.RedirectUtil.RedirectHandler;

@IvyTest
public class TestLogin {

  @Test
  public void loginAndRedirect() {
    ISession.current().logoutSessionUser();
    assertNull(ISession.current().getSessionUser());
    TestHandler handler = new TestHandler();
    RedirectUtil.setHandler(handler);
    login();
    assertThat(handler.redirectUrl).isEqualTo("/home.xhtml");
    assertThat(ISession.current().getSessionUser().getName()).isEqualTo("testJunitUser");
  }

  @Test
  public void getUserRoles_commaSeparated() {
    login();
    assertThat(getRoles(ISession.current().getSessionUser()))
        .isEqualTo("Everybody, testRoleJunit");
  }

  @Test
  public void lockoutExternalRedirect() {
    ISession.current().logoutSessionUser();
    TestHandler handler = new TestHandler();
    RedirectUtil.setHandler(handler);
    for (var url : new String[] {"//www.google.com", "https://dev.axonivy.com", "\\evil.com",
        "/%2f%2fevil.com", "/%5cevil.com", "javascript:alert(1)"}) {
      assertThatThrownBy(() -> LoginUtil.login("testJunitUser", "testJunitUser", url))
          .isInstanceOf(RuntimeException.class)
          .hasMessage("Redirecting to external websites is not allowed. Tried to redirect to: " + url);
      assertThat(handler.redirectUrl).isNull();
    }
  }

  @Test
  public void loginRedirectsToRelativePage() {
    ISession.current().logoutSessionUser();
    TestHandler handler = new TestHandler();
    RedirectUtil.setHandler(handler);
    LoginUtil.login("testJunitUser", "testJunitUser", "tasks.xhtml");
    assertThat(handler.redirectUrl).isEqualTo("/tasks.xhtml");
    assertThat(ISession.current().getSessionUser().getName()).isEqualTo("testJunitUser");
  }

  private static void login() {
    LoginUtil.login("testJunitUser", "testJunitUser", "");
  }

  private static final class TestHandler implements RedirectHandler {

    private String redirectUrl;

    @Override
    public void redirect(String url) {
      this.redirectUrl = url;
    }

  }

}
