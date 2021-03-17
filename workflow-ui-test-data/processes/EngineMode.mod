[Ivy]
1783B19164F69B78 9.2.0 #module
>Proto >Proto Collection #zClass
Ee0 EngineMode Big #zClass
Ee0 B #cInfo
Ee0 #process
Ee0 @AnnotationInP-0n ai ai #zField
Ee0 @TextInP .type .type #zField
Ee0 @TextInP .processKind .processKind #zField
Ee0 @TextInP .xml .xml #zField
Ee0 @TextInP .responsibility .responsibility #zField
Ee0 @StartRequest f10 '' #zField
Ee0 @EndTask f12 '' #zField
Ee0 @GridStep f11 '' #zField
Ee0 @PushWFArc f14 '' #zField
Ee0 @PushWFArc f13 '' #zField
Ee0 @GridStep f0 '' #zField
Ee0 @EndTask f1 '' #zField
Ee0 @StartRequest f2 '' #zField
Ee0 @PushWFArc f3 '' #zField
Ee0 @PushWFArc f4 '' #zField
>Proto Ee0 Ee0 EngineMode #zField
Ee0 f10 outLink designerEmbedded.ivp #txt
Ee0 f10 inParamDecl '<> param;' #txt
Ee0 f10 requestEnabled true #txt
Ee0 f10 triggerEnabled false #txt
Ee0 f10 callSignature designerEmbedded() #txt
Ee0 f10 caseData businessCase.attach=true #txt
Ee0 f10 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>designerEmbedded</name>
    </language>
</elementInfo>
' #txt
Ee0 f10 @C|.responsibility Everybody #txt
Ee0 f10 81 49 30 30 -60 18 #rect
Ee0 f12 337 49 30 30 0 15 #rect
Ee0 f11 actionTable 'out=in;
' #txt
Ee0 f11 actionCode 'import ch.ivyteam.ivy.server.restricted.EngineMode;


EngineMode.set(EngineMode.DESIGNER_EMBEDDED);' #txt
Ee0 f11 168 42 112 44 0 -7 #rect
Ee0 f14 280 64 337 64 #arcP
Ee0 f13 111 64 168 64 #arcP
Ee0 f0 actionTable 'out=in;
' #txt
Ee0 f0 actionCode 'import ch.ivyteam.ivy.server.restricted.EngineMode;


EngineMode.set(EngineMode.STANDARD);' #txt
Ee0 f0 168 138 112 44 0 -7 #rect
Ee0 f1 337 145 30 30 0 15 #rect
Ee0 f2 outLink standard.ivp #txt
Ee0 f2 inParamDecl '<> param;' #txt
Ee0 f2 requestEnabled true #txt
Ee0 f2 triggerEnabled false #txt
Ee0 f2 callSignature standard() #txt
Ee0 f2 caseData businessCase.attach=true #txt
Ee0 f2 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>standard</name>
    </language>
</elementInfo>
' #txt
Ee0 f2 @C|.responsibility Everybody #txt
Ee0 f2 81 145 30 30 -26 19 #rect
Ee0 f3 280 160 337 160 #arcP
Ee0 f4 111 160 168 160 #arcP
>Proto Ee0 .type ch.ivyteam.ivy.project.workflow.Data #txt
>Proto Ee0 .processKind NORMAL #txt
>Proto Ee0 0 0 32 24 18 0 #rect
>Proto Ee0 @|BIcon #fIcon
Ee0 f10 mainOut f13 tail #connect
Ee0 f13 head f11 mainIn #connect
Ee0 f11 mainOut f14 tail #connect
Ee0 f14 head f12 mainIn #connect
Ee0 f2 mainOut f4 tail #connect
Ee0 f4 head f0 mainIn #connect
Ee0 f0 mainOut f3 tail #connect
Ee0 f3 head f1 mainIn #connect
