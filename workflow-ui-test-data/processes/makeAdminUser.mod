[Ivy]
175461E47A870BF8 9.3.0 #module
>Proto >Proto Collection #zClass
mr0 makeAdminUser Big #zClass
mr0 B #cInfo
mr0 #process
mr0 @TextInP .type .type #zField
mr0 @TextInP .processKind .processKind #zField
mr0 @TextInP .xml .xml #zField
mr0 @TextInP .responsibility .responsibility #zField
mr0 @StartRequest f0 '' #zField
mr0 @EndTask f1 '' #zField
mr0 @GridStep f2 '' #zField
mr0 @PushWFArc f3 '' #zField
mr0 @PushWFArc f4 '' #zField
>Proto mr0 mr0 makeAdminUser #zField
mr0 f0 outLink makeAdminUser.ivp #txt
mr0 f0 inParamDecl '<> param;' #txt
mr0 f0 requestEnabled true #txt
mr0 f0 triggerEnabled false #txt
mr0 f0 callSignature makeAdminUser() #txt
mr0 f0 startCategory actions #txt
mr0 f0 caseData businessCase.attach=true #txt
mr0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>makeAdminUser.ivp</name>
    </language>
</elementInfo>
' #txt
mr0 f0 @C|.responsibility Everybody #txt
mr0 f0 81 49 30 30 -55 23 #rect
mr0 f1 337 49 30 30 0 15 #rect
mr0 f2 actionTable 'out=in;
' #txt
mr0 f2 actionCode 'import ch.ivyteam.workflowuitestdata.TestUtil;
TestUtil.makeAdmin();' #txt
mr0 f2 security system #txt
mr0 f2 168 42 112 44 0 -7 #rect
mr0 f3 111 64 168 64 #arcP
mr0 f4 280 64 337 64 #arcP
>Proto mr0 .type workflow.uite.Data #txt
>Proto mr0 .processKind NORMAL #txt
>Proto mr0 0 0 32 24 18 0 #rect
>Proto mr0 @|BIcon #fIcon
mr0 f0 mainOut f3 tail #connect
mr0 f3 head f2 mainIn #connect
mr0 f2 mainOut f4 tail #connect
mr0 f4 head f1 mainIn #connect
