[Ivy]
17D95C54B57821A9 9.3.1 #module
>Proto >Proto Collection #zClass
Ne0 NotEmbeddInFrame Big #zClass
Ne0 B #cInfo
Ne0 #process
Ne0 @AnnotationInP-0n ai ai #zField
Ne0 @TextInP .type .type #zField
Ne0 @TextInP .processKind .processKind #zField
Ne0 @TextInP .xml .xml #zField
Ne0 @TextInP .responsibility .responsibility #zField
Ne0 @StartRequest f0 '' #zField
Ne0 @EndTask f1 '' #zField
Ne0 @UserDialog f3 '' #zField
Ne0 @PushWFArc f2 '' #zField
Ne0 @TaskSwitchSimple f5 '' #zField
Ne0 @PushWFArc f4 '' #zField
Ne0 @UserDialog f7 '' #zField
Ne0 @PushWFArc f8 '' #zField
Ne0 @TkArc f6 '' #zField
>Proto Ne0 Ne0 NotEmbeddInFrame #zField
Ne0 f0 outLink start.ivp #txt
Ne0 f0 inParamDecl '<> param;' #txt
Ne0 f0 requestEnabled true #txt
Ne0 f0 triggerEnabled false #txt
Ne0 f0 callSignature start() #txt
Ne0 f0 startName 'Do not embed in Frame' #txt
Ne0 f0 startCustomFields embedInFrame=false #txt
Ne0 f0 caseData businessCase.attach=true #txt
Ne0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>notEmbedInFrameStart.ivp</name>
    </language>
</elementInfo>
' #txt
Ne0 f0 @C|.responsibility Everybody #txt
Ne0 f0 81 49 30 30 -23 17 #rect
Ne0 f1 593 49 30 30 0 15 #rect
Ne0 f3 dialogId ch.ivyteam.ivy.project.workflow.TestDialog1 #txt
Ne0 f3 startMethod start(ch.ivyteam.ivy.project.workflow.Data) #txt
Ne0 f3 requestActionDecl '<ch.ivyteam.ivy.project.workflow.Data data> param;' #txt
Ne0 f3 responseMappingAction 'out=in;
' #txt
Ne0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>TestDialog1</name>
    </language>
</elementInfo>
' #txt
Ne0 f3 424 42 112 44 -34 -8 #rect
Ne0 f2 536 64 593 64 #arcP
Ne0 f5 actionTable 'out=in1;
' #txt
Ne0 f5 caseData case.name=notEmbedCase #txt
Ne0 f5 taskData 'TaskA.NAM=notEmbedTask
TaskA.customFields.STRING.embedInFrame="false"' #txt
Ne0 f5 337 49 30 30 0 16 #rect
Ne0 f4 367 64 424 64 #arcP
Ne0 f7 dialogId ch.ivyteam.ivy.project.workflow.TestDialog1 #txt
Ne0 f7 startMethod start(ch.ivyteam.ivy.project.workflow.Data) #txt
Ne0 f7 requestActionDecl '<ch.ivyteam.ivy.project.workflow.Data data> param;' #txt
Ne0 f7 responseMappingAction 'out=in;
' #txt
Ne0 f7 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>TestDialog1</name>
    </language>
</elementInfo>
' #txt
Ne0 f7 168 42 112 44 -34 -8 #rect
Ne0 f8 111 64 168 64 #arcP
Ne0 f6 280 64 337 64 #arcP
>Proto Ne0 .type ch.ivyteam.ivy.project.workflow.Data #txt
>Proto Ne0 .processKind NORMAL #txt
>Proto Ne0 0 0 32 24 18 0 #rect
>Proto Ne0 @|BIcon #fIcon
Ne0 f3 mainOut f2 tail #connect
Ne0 f2 head f1 mainIn #connect
Ne0 f5 out f4 tail #connect
Ne0 f4 head f3 mainIn #connect
Ne0 f0 mainOut f8 tail #connect
Ne0 f8 head f7 mainIn #connect
Ne0 f7 mainOut f6 tail #connect
Ne0 f6 head f5 in #connect
