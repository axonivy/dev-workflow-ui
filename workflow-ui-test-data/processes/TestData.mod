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
>Proto Ta0 Ta0 TestData #zField
Ta0 f0 outLink TestData.ivp #txt
Ta0 f0 inParamDecl '<> param;' #txt
Ta0 f0 requestEnabled true #txt
Ta0 f0 triggerEnabled false #txt
Ta0 f0 callSignature TestData() #txt
Ta0 f0 caseData 'businessCase.attach=true
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
Ta0 f3 caseData case.name=TestCase #txt
Ta0 f3 taskData 'TaskA.EXP=new Duration(0, 0, 3, 0, 0, 0)
TaskA.NAM=TestTask
TaskA.PRI=1
TaskA.SCRIPT=import ch.ivyteam.ivy.workflow.document.Path;\r\ntask.getCase().documents().add(new Path("Test/test.txt")).write().withContentFrom("this is test document");\r\ntask.getCase().documents().add(new Path("Test/documentTest.txt")).write().withContentFrom("document. you can read this correctly");' #txt
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
Ta0 f5 outLink startTestDialog.ivp #txt
Ta0 f5 inParamDecl '<> param;' #txt
Ta0 f5 requestEnabled true #txt
Ta0 f5 triggerEnabled false #txt
Ta0 f5 callSignature startTestDialog() #txt
Ta0 f5 startCategory exampleDialogs #txt
Ta0 f5 caseData businessCase.attach=true #txt
Ta0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>startTestDialog.ivp</name>
    </language>
</elementInfo>
' #txt
Ta0 f5 @C|.responsibility Everybody #txt
Ta0 f5 81 145 30 30 -63 16 #rect
Ta0 f6 337 145 30 30 0 15 #rect
Ta0 f7 dialogId ch.ivyteam.ivy.project.workflow.TestDialog #txt
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
Ta0 f10 caseData businessCase.attach=true #txt
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
Ta0 f12 dialogId ch.ivyteam.ivy.project.workflow.TestDialog #txt
Ta0 f12 startMethod start(ch.ivyteam.ivy.project.workflow.Data) #txt
Ta0 f12 requestActionDecl '<ch.ivyteam.ivy.project.workflow.Data data> param;' #txt
Ta0 f12 responseMappingAction 'out=in;
' #txt
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
Ta0 f18 caseData businessCase.attach=true #txt
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
Ta0 f23 337 529 30 30 0 15 #rect
Ta0 f24 outLink HomePageTestData.ivp #txt
Ta0 f24 inParamDecl '<> param;' #txt
Ta0 f24 requestEnabled true #txt
Ta0 f24 triggerEnabled false #txt
Ta0 f24 callSignature HomePageTestData() #txt
Ta0 f24 caseData businessCase.attach=true #txt
Ta0 f24 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>HomePageTestData</name>
    </language>
</elementInfo>
' #txt
Ta0 f24 @C|.responsibility Everybody #txt
Ta0 f24 81 529 30 30 -63 17 #rect
Ta0 f25 actionTable 'out=in1;
' #txt
Ta0 f25 caseData case.name=HomePageTestCase #txt
Ta0 f25 taskData 'TaskA.EXP=new Duration(0, 0, 3, 0, 0, 0)
TaskA.NAM=HomePageTestTask
TaskA.PRI=1' #txt
Ta0 f25 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>HomePageTestTask</name>
    </language>
</elementInfo>
' #txt
Ta0 f25 209 529 30 30 -50 17 #rect
Ta0 f26 239 544 337 544 #arcP
Ta0 f27 111 544 209 544 #arcP
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
