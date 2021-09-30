[Ivy]
1750C5211D94569D 9.3.0 #module
>Proto >Proto Collection #zClass
Ta0 TestData Big #zClass
Ta0 B #cInfo
Ta0 #process
Ta0 @TextInP .type .type #zField
Ta0 @TextInP .processKind .processKind #zField
Ta0 @TextInP .xml .xml #zField
Ta0 @TextInP .responsibility .responsibility #zField
Ta0 @StartRequest f0 '' #zField
Ta0 @TaskSwitchSimple f3 '' #zField
Ta0 @TkArc f2 '' #zField
Ta0 @PushWFArc f4 '' #zField
Ta0 @EndTask f1 '' #zField
Ta0 @StartRequest f5 '' #zField
Ta0 @EndTask f6 '' #zField
Ta0 @UserDialog f7 '' #zField
Ta0 @PushWFArc f8 '' #zField
Ta0 @PushWFArc f9 '' #zField
Ta0 @StartRequest f10 '' #zField
Ta0 @EndTask f11 '' #zField
Ta0 @UserTask f12 '' #zField
Ta0 @TkArc f13 '' #zField
Ta0 @PushWFArc f14 '' #zField
Ta0 @SignalBoundaryEvent f15 '' #zField
Ta0 @SignalStartEvent f16 '' #zField
Ta0 @PushWFArc f17 '' #zField
Ta0 @StartRequest f18 '' #zField
Ta0 @IntermediateEvent f19 '' #zField
Ta0 @EndTask f20 '' #zField
Ta0 @PushWFArc f22 '' #zField
Ta0 @PushWFArc f21 '' #zField
Ta0 @EndTask f23 '' #zField
Ta0 @StartRequest f24 '' #zField
Ta0 @TaskSwitchSimple f25 '' #zField
Ta0 @PushWFArc f26 '' #zField
Ta0 @TkArc f27 '' #zField
Ta0 @StartRequest f28 '' #zField
Ta0 @EndTask f29 '' #zField
Ta0 @UserDialog f31 '' #zField
Ta0 @PushWFArc f32 '' #zField
Ta0 @PushWFArc f30 '' #zField
Ta0 @StartRequest f33 '' #zField
Ta0 @UserDialog f34 '' #zField
Ta0 @EndTask f36 '' #zField
Ta0 @UserTask f35 '' #zField
Ta0 @PushWFArc f37 '' #zField
Ta0 @TkArc f38 '' #zField
Ta0 @PushWFArc f39 '' #zField
Ta0 @StartRequest f40 '' #zField
Ta0 @EndTask f41 '' #zField
Ta0 @UserDialog f42 '' #zField
Ta0 @PushWFArc f43 '' #zField
Ta0 @PushWFArc f44 '' #zField
>Proto Ta0 Ta0 TestData #zField
Ta0 f0 outLink TestData.ivp #txt
Ta0 f0 inParamDecl '<> param;' #txt
Ta0 f0 requestEnabled true #txt
Ta0 f0 triggerEnabled false #txt
Ta0 f0 callSignature TestData() #txt
Ta0 f0 startDescription 'Process which generates some test task and case data' #txt
Ta0 f0 startCategory TestData #txt
Ta0 f0 startCustomFields 'cssIcon=si si-add' #txt
Ta0 f0 taskData 'TaskTriggered.NAM=First task of TestData' #txt
Ta0 f0 caseData 'businessCase.attach=true
case.name=First case of TestData
customFields.STRING.field\ 2="value"
customFields.STRING.test\ 3\ ="test string"
customFields.STRING.test\ field\ 1="test value"' #txt
Ta0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>TestData</name>
    </language>
</elementInfo>
' #txt
Ta0 f0 @C|.responsibility Everybody #txt
Ta0 f0 81 49 30 30 -25 17 #rect
Ta0 f3 actionTable 'out=in1;
' #txt
Ta0 f3 caseData 'case.name=Created case of TestData' #txt
Ta0 f3 taskData 'TaskA.EXP=new Duration(0, 0, 3, 0, 0, 0)
TaskA.NAM=Created task of TestData
TaskA.PRI=1
TaskA.SCRIPT=import ch.ivyteam.ivy.workflow.document.Path;\r\nimport java.util.Random;\r\n\r\nRandom random \= new Random();\r\nString number \= random.nextInt(100000).toString();\r\n\r\ntask.getCase().documents().add(new Path("Test" + number + "/test.txt")).write().withContentFrom("this is test document");\r\ntask.getCase().documents().add(new Path("Test" + number + "/documentTest.txt")).write().withContentFrom("document. you can read this correctly");
TaskA.customFields.STRING.field\ 2="test task string"
TaskA.customFields.STRING.test\ field\ 1="task test value"' #txt
Ta0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>TestTask
</name>
    </language>
</elementInfo>
' #txt
Ta0 f3 209 49 30 30 -27 16 #rect
Ta0 f2 111 64 209 64 #arcP
Ta0 f4 239 64 337 64 #arcP
Ta0 f1 337 49 30 30 0 15 #rect
Ta0 f5 outLink startTestDialog1.ivp #txt
Ta0 f5 inParamDecl '<> param;' #txt
Ta0 f5 requestEnabled true #txt
Ta0 f5 triggerEnabled false #txt
Ta0 f5 callSignature startTestDialog1() #txt
Ta0 f5 startDescription 'Dialog 1 for testing Developer Workflow-UI' #txt
Ta0 f5 startCategory exampleDialogs #txt
Ta0 f5 taskData 'TaskTriggered.NAM=Test Developer Workflow-UI Dialog 1' #txt
Ta0 f5 caseData 'businessCase.attach=true
case.name=Test Developer Workflow-UI Dialog Case 1' #txt
Ta0 f5 showInStartList 1 #txt
Ta0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>startTestDialog1.ivp</name>
        <desc>Test dialog for testing Developer Workflow UI</desc>
    </language>
</elementInfo>
' #txt
Ta0 f5 @C|.responsibility Everybody #txt
Ta0 f5 81 145 30 30 -63 16 #rect
Ta0 f6 337 145 30 30 0 15 #rect
Ta0 f7 dialogId ch.ivyteam.ivy.project.workflow.TestDialog1 #txt
Ta0 f7 startMethod start(ch.ivyteam.ivy.project.workflow.Data) #txt
Ta0 f7 requestActionDecl '<ch.ivyteam.ivy.project.workflow.Data data> param;' #txt
Ta0 f7 responseMappingAction 'out=in;
' #txt
Ta0 f7 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>TestDialog</name>
    </language>
</elementInfo>
' #txt
Ta0 f7 168 138 112 44 -33 -7 #rect
Ta0 f8 111 160 168 160 #arcP
Ta0 f9 280 160 337 160 #arcP
Ta0 f10 outLink startBoundarySignal.ivp #txt
Ta0 f10 inParamDecl '<> param;' #txt
Ta0 f10 requestEnabled true #txt
Ta0 f10 triggerEnabled false #txt
Ta0 f10 callSignature startBoundarySignal() #txt
Ta0 f10 startDescription 'Creates a waiting boundary signal' #txt
Ta0 f10 taskData 'TaskTriggered.NAM=First task of startBoundarySignal' #txt
Ta0 f10 caseData 'businessCase.attach=true
case.name=First case of startBoundarySignal' #txt
Ta0 f10 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>startBoundarySignal.ivp</name>
    </language>
</elementInfo>
' #txt
Ta0 f10 @C|.responsibility Everybody #txt
Ta0 f10 81 241 30 30 -72 18 #rect
Ta0 f11 337 241 30 30 0 15 #rect
Ta0 f12 dialogId ch.ivyteam.ivy.project.workflow.TestDialog1 #txt
Ta0 f12 startMethod start(ch.ivyteam.ivy.project.workflow.Data) #txt
Ta0 f12 requestActionDecl '<ch.ivyteam.ivy.project.workflow.Data data> param;' #txt
Ta0 f12 responseMappingAction 'out=in;
' #txt
Ta0 f12 caseData 'case.name=case created in UserTask during startBoundarySignal process' #txt
Ta0 f12 taskData 'TaskA.NAM=task created in UserTask during startBoundarySignal process' #txt
Ta0 f12 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>TestDialog</name>
    </language>
</elementInfo>
' #txt
Ta0 f12 168 234 112 44 -33 -7 #rect
Ta0 f13 111 256 168 256 #arcP
Ta0 f14 280 256 337 256 #arcP
Ta0 f15 actionTable 'out=in;
' #txt
Ta0 f15 signalCode test:data:signal #txt
Ta0 f15 attachedToRef 1750C5211D94569D-f12 #txt
Ta0 f15 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>test:data:signal</name>
    </language>
</elementInfo>
' #txt
Ta0 f15 241 273 30 30 0 15 #rect
Ta0 f16 signalCode test:signal:complete #txt
Ta0 f16 attachToBusinessCase true #txt
Ta0 f16 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>test:signal:complete</name>
    </language>
</elementInfo>
' #txt
Ta0 f16 81 337 30 30 0 15 #rect
Ta0 f17 111 352 352 271 #arcP
Ta0 f17 1 352 352 #addKink
Ta0 f17 1 0.17787910602871868 0 0 #arcLabel
Ta0 f18 outLink testIntermediateEventProcess.ivp #txt
Ta0 f18 inParamDecl '<> param;' #txt
Ta0 f18 actionCode 'import java.util.Random;
Random random = new Random();
out.eventID = random.nextInt();' #txt
Ta0 f18 requestEnabled true #txt
Ta0 f18 triggerEnabled false #txt
Ta0 f18 callSignature testIntermediateEventProcess() #txt
Ta0 f18 startDescription 'Creates test intermediate event process' #txt
Ta0 f18 taskData 'TaskTriggered.NAM=testIntermediateEventProcess task' #txt
Ta0 f18 caseData 'businessCase.attach=true
case.name=testIntermediateEventProcess case' #txt
Ta0 f18 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>testIntermediateEventProcess.ivp</name>
    </language>
</elementInfo>
' #txt
Ta0 f18 @C|.responsibility Everybody #txt
Ta0 f18 81 433 30 30 -91 20 #rect
Ta0 f19 actionTable 'out=in;
' #txt
Ta0 f19 eventIdConfig "in.eventID.toString()" #txt
Ta0 f19 timeoutConfig 'ACTION_AFTER_TIMEOUT=NOTHING
EXCEPTION_PROCESS_START=
TIMEOUT_SCRIPT=' #txt
Ta0 f19 eventBeanClass "ch.ivyteam.workflowuitestdata.TestIntermediateClass" #txt
Ta0 f19 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>TestIntermediateEvent</name>
        <desc>intermediate event description</desc>
    </language>
</elementInfo>
' #txt
Ta0 f19 209 433 30 30 0 16 #rect
Ta0 f20 337 433 30 30 0 15 #rect
Ta0 f22 239 448 337 448 #arcP
Ta0 f21 111 448 209 448 #arcP
Ta0 f23 665 49 30 30 0 15 #rect
Ta0 f24 outLink HomePageTestData.ivp #txt
Ta0 f24 inParamDecl '<> param;' #txt
Ta0 f24 requestEnabled true #txt
Ta0 f24 triggerEnabled false #txt
Ta0 f24 callSignature HomePageTestData() #txt
Ta0 f24 startDescription 'Different process which generates test data. Mostly used for CI tests' #txt
Ta0 f24 startCategory TestData #txt
Ta0 f24 startCustomFields 'cssIcon=si si-house-1' #txt
Ta0 f24 taskData 'TaskTriggered.NAM=First task of HomePageTestData' #txt
Ta0 f24 caseData 'businessCase.attach=true
case.name=First case of HomePageTestData' #txt
Ta0 f24 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>HomePageTestData</name>
    </language>
</elementInfo>
' #txt
Ta0 f24 @C|.responsibility Everybody #txt
Ta0 f24 409 49 30 30 -63 17 #rect
Ta0 f25 actionTable 'out=in1;
' #txt
Ta0 f25 caseData 'case.name=Created case of HomePageTestData' #txt
Ta0 f25 taskData 'TaskA.EXP=new Duration(0, 0, 3, 0, 0, 0)
TaskA.NAM=Created task of HomePageTestData
TaskA.PRI=1' #txt
Ta0 f25 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>HomePageTestTask</name>
    </language>
</elementInfo>
' #txt
Ta0 f25 537 49 30 30 -50 17 #rect
Ta0 f26 567 64 665 64 #arcP
Ta0 f27 439 64 537 64 #arcP
Ta0 f28 outLink testDefaultPages.ivp #txt
Ta0 f28 inParamDecl '<> param;' #txt
Ta0 f28 requestEnabled true #txt
Ta0 f28 triggerEnabled false #txt
Ta0 f28 callSignature testDefaultPages() #txt
Ta0 f28 startDescription 'Dialog with buttons that redirect to Default Pages using ivy.html' #txt
Ta0 f28 taskData 'TaskTriggered.NAM=First task of testDefaultPages' #txt
Ta0 f28 caseData 'businessCase.attach=true
case.name=First case of testDefaultPages' #txt
Ta0 f28 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>testDefaultPages.ivp</name>
    </language>
</elementInfo>
' #txt
Ta0 f28 @C|.responsibility Everybody #txt
Ta0 f28 409 241 30 30 -58 21 #rect
Ta0 f29 665 241 30 30 0 15 #rect
Ta0 f31 dialogId ch.ivyteam.ivy.project.workflow.TestDefaultPage #txt
Ta0 f31 startMethod start() #txt
Ta0 f31 requestActionDecl '<> param;' #txt
Ta0 f31 responseMappingAction 'out=in;
' #txt
Ta0 f31 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>TestDefaultPage</name>
    </language>
</elementInfo>
' #txt
Ta0 f31 496 234 112 44 -52 -7 #rect
Ta0 f32 439 256 496 256 #arcP
Ta0 f30 608 256 665 256 #arcP
Ta0 f33 outLink doubleDialog.ivp #txt
Ta0 f33 inParamDecl '<> param;' #txt
Ta0 f33 requestEnabled true #txt
Ta0 f33 triggerEnabled false #txt
Ta0 f33 callSignature doubleDialog() #txt
Ta0 f33 startDescription 'Process with 2 dialogs. Used for testing frame header bar' #txt
Ta0 f33 taskData 'TaskTriggered.NAM=First task of doubleDialog' #txt
Ta0 f33 caseData 'businessCase.attach=true
case.name=First case of doubleDialog' #txt
Ta0 f33 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>doubleDialog.ivp</name>
    </language>
</elementInfo>
' #txt
Ta0 f33 @C|.responsibility Everybody #txt
Ta0 f33 409 337 30 30 -25 17 #rect
Ta0 f34 dialogId ch.ivyteam.ivy.project.workflow.TestDialog1 #txt
Ta0 f34 startMethod start(ch.ivyteam.ivy.project.workflow.Data) #txt
Ta0 f34 requestActionDecl '<ch.ivyteam.ivy.project.workflow.Data data> param;' #txt
Ta0 f34 responseMappingAction 'out=in;
' #txt
Ta0 f34 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>TestDialog</name>
    </language>
</elementInfo>
' #txt
Ta0 f34 496 330 112 44 -33 -7 #rect
Ta0 f36 825 337 30 30 0 15 #rect
Ta0 f35 dialogId ch.ivyteam.ivy.project.workflow.TestDialog2 #txt
Ta0 f35 startMethod start() #txt
Ta0 f35 requestActionDecl '<> param;' #txt
Ta0 f35 responseMappingAction 'out=in;
' #txt
Ta0 f35 caseData 'case.name=Case created in UserTask during doubleDialog process' #txt
Ta0 f35 taskData 'TaskA.NAM=User task created during doubleDialog process
TaskA.SKIP_TASK_LIST=true' #txt
Ta0 f35 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>TestDialog2</name>
    </language>
</elementInfo>
' #txt
Ta0 f35 656 330 112 44 -37 -7 #rect
Ta0 f37 439 352 496 352 #arcP
Ta0 f38 608 352 656 352 #arcP
Ta0 f39 768 352 825 352 #arcP
Ta0 f40 outLink startTestDialog2.ivp #txt
Ta0 f40 inParamDecl '<> param;' #txt
Ta0 f40 requestEnabled true #txt
Ta0 f40 triggerEnabled false #txt
Ta0 f40 callSignature startTestDialog2() #txt
Ta0 f40 startDescription 'Dialog 2 for testing Developer Workflow-UI' #txt
Ta0 f40 startCategory exampleDialogs #txt
Ta0 f40 taskData 'TaskTriggered.NAM=Test Developer Workflow-UI Dialog 2' #txt
Ta0 f40 caseData 'businessCase.attach=true
case.name=Test Developer Workflow-UI Dialog Case 2' #txt
Ta0 f40 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>startTestDialog2.ivp</name>
        <desc>Test dialog for testing Developer Workflow UI</desc>
    </language>
</elementInfo>
' #txt
Ta0 f40 @C|.responsibility Everybody #txt
Ta0 f40 409 145 30 30 -50 21 #rect
Ta0 f41 665 145 30 30 0 15 #rect
Ta0 f42 dialogId ch.ivyteam.ivy.project.workflow.TestDialog2 #txt
Ta0 f42 startMethod start() #txt
Ta0 f42 requestActionDecl '<> param;' #txt
Ta0 f42 responseMappingAction 'out=in;
' #txt
Ta0 f42 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>TestDialog</name>
    </language>
</elementInfo>
' #txt
Ta0 f42 496 138 112 44 -33 -7 #rect
Ta0 f43 439 160 496 160 #arcP
Ta0 f44 608 160 665 160 #arcP
>Proto Ta0 .type workflow.uite.Data #txt
>Proto Ta0 .processKind NORMAL #txt
>Proto Ta0 0 0 32 24 18 0 #rect
>Proto Ta0 @|BIcon #fIcon
Ta0 f0 mainOut f2 tail #connect
Ta0 f2 head f3 in #connect
Ta0 f3 out f4 tail #connect
Ta0 f4 head f1 mainIn #connect
Ta0 f5 mainOut f8 tail #connect
Ta0 f8 head f7 mainIn #connect
Ta0 f7 mainOut f9 tail #connect
Ta0 f9 head f6 mainIn #connect
Ta0 f10 mainOut f13 tail #connect
Ta0 f13 head f12 in #connect
Ta0 f12 out f14 tail #connect
Ta0 f14 head f11 mainIn #connect
Ta0 f16 mainOut f17 tail #connect
Ta0 f17 head f11 mainIn #connect
Ta0 f19 mainOut f22 tail #connect
Ta0 f22 head f20 mainIn #connect
Ta0 f18 mainOut f21 tail #connect
Ta0 f21 head f19 mainIn #connect
Ta0 f24 mainOut f27 tail #connect
Ta0 f27 head f25 in #connect
Ta0 f25 out f26 tail #connect
Ta0 f26 head f23 mainIn #connect
Ta0 f28 mainOut f32 tail #connect
Ta0 f32 head f31 mainIn #connect
Ta0 f31 mainOut f30 tail #connect
Ta0 f30 head f29 mainIn #connect
Ta0 f33 mainOut f37 tail #connect
Ta0 f37 head f34 mainIn #connect
Ta0 f34 mainOut f38 tail #connect
Ta0 f38 head f35 in #connect
Ta0 f35 out f39 tail #connect
Ta0 f39 head f36 mainIn #connect
Ta0 f40 mainOut f43 tail #connect
Ta0 f43 head f42 mainIn #connect
Ta0 f42 mainOut f44 tail #connect
Ta0 f44 head f41 mainIn #connect
