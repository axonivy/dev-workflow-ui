[Ivy]
17B167C2EEBFD5CF 9.3.0 #module
>Proto >Proto Collection #zClass
Te0 TestService Big #zClass
Te0 WS #cInfo
Te0 #process
Te0 @TextInP .webServiceName .webServiceName #zField
Te0 @TextInP .implementationClassName .implementationClassName #zField
Te0 @TextInP .authenticationType .authenticationType #zField
Te0 @AnnotationInP-0n ai ai #zField
Te0 @TextInP .type .type #zField
Te0 @TextInP .processKind .processKind #zField
Te0 @TextInP .xml .xml #zField
Te0 @TextInP .responsibility .responsibility #zField
Te0 @StartWS f0 '' #zField
Te0 @EndWS f1 '' #zField
Te0 @PushWFArc f2 '' #zField
>Proto Te0 Te0 TestService #zField
Te0 f0 inParamDecl '<> param;' #txt
Te0 f0 outParamDecl '<> result;' #txt
Te0 f0 callSignature call() #txt
Te0 f0 useUserDefinedException false #txt
Te0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>call()</name>
    </language>
</elementInfo>
' #txt
Te0 f0 81 49 30 30 -15 17 #rect
Te0 f1 337 49 30 30 0 15 #rect
Te0 f2 111 64 337 64 #arcP
>Proto Te0 .webServiceName ch.ivyteam.ivy.project.workflow.TestService #txt
>Proto Te0 .type ch.ivyteam.ivy.project.workflow.Data #txt
>Proto Te0 .processKind WEB_SERVICE #txt
>Proto Te0 -8 -8 16 16 16 26 #rect
Te0 f0 mainOut f2 tail #connect
Te0 f2 head f1 mainIn #connect
