[Ivy]
1750C5211D94569D 9.2.0 #module
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
Ta0 f12 responseMappingAction 'out=in;
' #txt
Ta0 f12 168 234 112 44 0 -7 #rect
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
