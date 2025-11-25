package ch.ivyteam.workflowui.casemap;

import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.casemap.runtime.ICaseMapService;
import ch.ivyteam.ivy.workflow.ICase;

public class CaseMapModel {
  private final List<CaseMapStageModel> stages;

  public static CaseMapModel create(ICase selectedCase) {
    var stageModels = new ArrayList<CaseMapStageModel>();
    var caseMapService = ICaseMapService.current().getCaseMapService(selectedCase.getBusinessCase());
    var caseMap = caseMapService.find().current();
    if (caseMap != null) {
      var stages = caseMap.getStages();
      var currentCaseMap = caseMapService.findCurrentStage();
      var runningStageIndex = currentCaseMap != null ? stages.indexOf(currentCaseMap) : -1;
      for (int i = 0; i < stages.size(); i++) {
        stageModels.add(new CaseMapStageModel(stages.get(i), i, runningStageIndex, stages.size()));
      }
    }
    return new CaseMapModel(stageModels);
  }

  private CaseMapModel(List<CaseMapStageModel> stages) {
    this.stages = stages;
  }

  public List<CaseMapStageModel> getStages() {
    return stages;
  }
}
