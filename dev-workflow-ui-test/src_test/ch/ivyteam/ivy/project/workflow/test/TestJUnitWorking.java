package ch.ivyteam.ivy.project.workflow.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class TestJUnitWorking {

  @Test
  public void useIvy() {
    Ivy.log().info("hi from JUnit");
    assertTrue(true, "I can use Ivy API facade in tests");
  }

}
