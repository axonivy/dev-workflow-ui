package ch.ivyteam.workflowui.casemap;

import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.casemap.runtime.repo.restricted.ICaseMapBusinessCase;
import ch.ivyteam.ivy.workflow.ICase;

@SuppressWarnings("restriction")
public class CaseMapModel
{
  private final List<CaseMapStageModel> stages;

  public static CaseMapModel create(ICase selectedCase)
  {
    var stageModels = new ArrayList<CaseMapStageModel>();
    var caseMapBusinessCase = ICaseMapBusinessCase.of(selectedCase.getBusinessCase());
    if (caseMapBusinessCase != null && selectedCase.isBusinessCase())
    {
      var stages = caseMapBusinessCase.getCaseMap().getStages();
      var runningStageIndex = stages.indexOf(caseMapBusinessCase.findCurrentStage());
      for (int i = 0; i < stages.size(); i++)
      {
        stageModels.add(new CaseMapStageModel(stages.get(i), i, runningStageIndex, stages.size()));
      }
    }
    return new CaseMapModel(stageModels);
  }

  private CaseMapModel(List<CaseMapStageModel> stages)
  {
    this.stages = stages;
  }

  public List<CaseMapStageModel> getStages()
  {
    return stages;
  }
}
