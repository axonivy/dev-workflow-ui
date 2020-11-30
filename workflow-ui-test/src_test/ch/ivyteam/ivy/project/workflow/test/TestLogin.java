package ch.ivyteam.ivy.project.workflow.test;

import static ch.ivyteam.workflowui.util.UserUtil.getRoles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.environment.IvyTest;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.workflowui.util.LoginUtil;
import ch.ivyteam.workflowui.util.RedirectUtil;
import ch.ivyteam.workflowui.util.RedirectUtil.RedirectHandler;

@IvyTest
public class TestLogin
{

  @Test
  public void loginAndRedirect()
  {
    ISession.current().logoutSessionUser();
    assertNull(ISession.current().getSessionUser());
    TestHandler handler = new TestHandler();
    RedirectUtil.setHandler(handler);
    login();
    assertThat(handler.url).isEqualTo("home.xhtml");
    assertThat(ISession.current().getSessionUser().getName()).isEqualTo("testJunitUser");
  }

  @Test
  public void getUserRoles_commaSeparated()
  {
    login();
    assertThat(getRoles(ISession.current().getSessionUser()))
      .isEqualTo("Everybody, testRoleJunit");
  }

  private static void login()
  {
    LoginUtil.login("testJunitUser", "testJunitUser");
  }

  private static final class TestHandler implements RedirectHandler
  {

    private String url;

    @Override
    public void redirect(String url)
    {
      this.url = url;
    }

  }

}
