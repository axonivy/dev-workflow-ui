[Ivy]
1750C5211D94569D 7.5.0 #module
>Proto >Proto Collection #zClass
Ta0 TestData Big #zClass
Ta0 B #cInfo
Ta0 #process
Ta0 @TextInP .type .type #zField
Ta0 @TextInP .processKind .processKind #zField
Ta0 @TextInP .xml .xml #zField
Ta0 @TextInP .responsibility .responsibility #zField
Ta0 @StartRequest f0 '' #zField
Ta0 @EndTask f1 '' #zField
Ta0 @TaskSwitchSimple f3 '' #zField
Ta0 @TkArc f2 '' #zField
Ta0 @PushWFArc f4 '' #zField
>Proto Ta0 Ta0 TestData #zField
Ta0 f0 outLink start.ivp #txt
Ta0 f0 inParamDecl '<> param;' #txt
Ta0 f0 requestEnabled true #txt
Ta0 f0 triggerEnabled false #txt
Ta0 f0 callSignature start() #txt
Ta0 f0 caseData businessCase.attach=true #txt
Ta0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start.ivp</name>
    </language>
</elementInfo>
' #txt
Ta0 f0 @C|.responsibility Everybody #txt
Ta0 f0 81 49 30 30 -25 17 #rect
Ta0 f0 @|StartRequestIcon #fIcon
Ta0 f1 337 49 30 30 0 15 #rect
Ta0 f1 @|EndIcon #fIcon
Ta0 f3 actionTable 'out=in1;
' #txt
Ta0 f3 caseData case.name=TestCase #txt
Ta0 f3 taskData 'TaskA.EXP=new Duration(0, 0, 3, 0, 0, 0)
TaskA.NAM=TestTask
TaskA.PRI=1' #txt
Ta0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>TestTask
</name>
    </language>
</elementInfo>
' #txt
Ta0 f3 209 49 30 30 -27 16 #rect
Ta0 f3 @|TaskSwitchSimpleIcon #fIcon
Ta0 f2 111 64 209 64 #arcP
Ta0 f4 239 64 337 64 #arcP
>Proto Ta0 .type workflow.uite.Data #txt
>Proto Ta0 .processKind NORMAL #txt
>Proto Ta0 0 0 32 24 18 0 #rect
>Proto Ta0 @|BIcon #fIcon
Ta0 f0 mainOut f2 tail #connect
Ta0 f2 head f3 in #connect
Ta0 f3 out f4 tail #connect
Ta0 f4 head f1 mainIn #connect
