package ch.ivyteam.ivy.project.workflow.test;

import static ch.ivyteam.workflowui.starts.StartableModel.evaluateEmbedInFrame;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TestStartableModel {

  @Test
  void embedInFrame() {
    assertThat(evaluateEmbedInFrame(null)).isTrue();
    assertThat(evaluateEmbedInFrame("")).isTrue();
    assertThat(evaluateEmbedInFrame(" ")).isTrue();
    assertThat(evaluateEmbedInFrame("true")).isTrue();
    assertThat(evaluateEmbedInFrame("false")).isFalse();
    assertThat(evaluateEmbedInFrame("'false'")).isFalse();
    assertThat(evaluateEmbedInFrame("\"false\"")).isFalse();
  }
}
