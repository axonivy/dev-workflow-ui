package ch.ivyteam.workflowuitestdata;

import static ch.ivyteam.ivy.security.IPermission.CASE_READ_ALL;
import static ch.ivyteam.ivy.security.IPermission.TASK_READ_ALL;
import static ch.ivyteam.ivy.security.IPermission.WORKFLOW_EVENT_READ_ALL;

import java.util.Random;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.security.exec.Sudo;
import ch.ivyteam.ivy.workflow.ITask;

public class TestUtil {

  public static void makeAdmin() {
    var name = "DeveloperTest";
    Sudo.run(() -> {
      var securityContext = ISecurityContext.current();
      var users = securityContext.users();
      var user = users.find(name);
      if (user == null) {
        user = users.create(name, name);
      }
      var securityDescriptor = securityContext.securityDescriptor();
      securityDescriptor.grantPermission(TASK_READ_ALL, user);
      securityDescriptor.grantPermission(CASE_READ_ALL, user);
      securityDescriptor.grantPermission(WORKFLOW_EVENT_READ_ALL, user);
    });
  }

  public static void generateTaskAndCaseNotes(ITask task) {
    var random = new Random();
    var caze = task.getCase();
    var user = notesUser();
    task.notes().add().author(user).content("this is a test note").execute();
    caze.notes().add().author(user).content("this is a test note").execute();

    String longNote = """
       I can't do that as Bruce Wayne... as a man. I'm flesh and blood. I can be ignored, destroyed. But as a symbol, I can be incorruptible, I can be everlasting.
       I seek the means to fight injustice. To turn fear against those who prey on the fearful. This isn't a car. Someone like you. Someone who'll rattle the cages.
       No guns, no killing. I'm not wearing hockey pads. Bruce Wayne, eccentric billionaire.
       I'll be standing where l belong. Between you and the peopIe of Gotham. I'm not wearing hockey pads. I seek the means to fight injustice. To turn fear against those who prey on the fearful.
      """;
    task.notes().add().author(user).content(longNote).execute();

    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    for (int i = 0; i < 6; i++) {
      int number = random.nextInt(100000);
      task.notes().add().author(user).content(String.valueOf(number)).execute();
      String character = String.valueOf(alphabet.charAt(number % alphabet.length()));
      caze.notes().add().author(user).content(character).execute();
    }
  }

  private static IUser notesUser() {
    var session = Ivy.session();
    if (session != null) {
      return session.getSessionUser();
    }
    return Ivy.security().users().find("DeveloperTest");
  }
}
