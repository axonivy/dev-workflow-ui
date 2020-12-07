package ch.ivyteam.workflowui.util;

import ch.ivyteam.ivy.server.restricted.EngineMode;

@SuppressWarnings("restriction")
public class EngineModeUtil
{
  public static boolean isDemoOrDesigner()
  {
    return EngineMode.is(EngineMode.DEMO) || EngineMode.is(EngineMode.DESIGNER_EMBEDDED);
  }
}
