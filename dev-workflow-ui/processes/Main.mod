[Ivy]
17425834F1CBA722 9.3.0 #module
>Proto >Proto Collection #zClass
Mn0 Main Big #zClass
Mn0 B #cInfo
Mn0 #process
Mn0 @TextInP .type .type #zField
Mn0 @TextInP .processKind .processKind #zField
Mn0 @TextInP .xml .xml #zField
Mn0 @TextInP .responsibility .responsibility #zField
Mn0 @StartRequest f0 '' #zField
Mn0 @EndRequest f1 '' #zField
Mn0 @PushWFArc f2 '' #zField
Mn0 @StartRequest f3 '' #zField
Mn0 @StartRequest f4 '' #zField
Mn0 @StartRequest f5 '' #zField
Mn0 @StartRequest f6 '' #zField
Mn0 @EndRequest f8 '' #zField
Mn0 @PushWFArc f7 '' #zField
Mn0 @EndRequest f10 '' #zField
Mn0 @EndRequest f15 '' #zField
Mn0 @PushWFArc f16 '' #zField
Mn0 @PushWFArc f11 '' #zField
Mn0 @StartRequest f12 '' #zField
Mn0 @EndRequest f13 '' #zField
Mn0 @PushWFArc f9 '' #zField
>Proto Mn0 Mn0 Main #zField
Mn0 f0 outLink DefaultApplicationHomePage.ivp #txt
Mn0 f0 inParamDecl '<> param;' #txt
Mn0 f0 requestEnabled true #txt
Mn0 f0 triggerEnabled false #txt
Mn0 f0 callSignature DefaultApplicationHomePage() #txt
Mn0 f0 caseData businessCase.attach=true #txt
Mn0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>DefaultApplicationHomePage</name>
    </language>
</elementInfo>
' #txt
Mn0 f0 @C|.responsibility Everybody #txt
Mn0 f0 81 113 30 30 -84 18 #rect
Mn0 f1 template "view/home.xhtml" #txt
Mn0 f1 209 113 30 30 0 15 #rect
Mn0 f2 111 128 209 128 #arcP
Mn0 f3 outLink DefaultProcessStartListPage.ivp #txt
Mn0 f3 inParamDecl '<> param;' #txt
Mn0 f3 requestEnabled true #txt
Mn0 f3 triggerEnabled false #txt
Mn0 f3 callSignature DefaultProcessStartListPage() #txt
Mn0 f3 caseData businessCase.attach=true #txt
Mn0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>DefaultProcessStartListPage</name>
    </language>
</elementInfo>
' #txt
Mn0 f3 @C|.responsibility Everybody #txt
Mn0 f3 81 305 30 30 -81 20 #rect
Mn0 f4 outLink DefaultTaskListPage.ivp #txt
Mn0 f4 inParamDecl '<> param;' #txt
Mn0 f4 requestEnabled true #txt
Mn0 f4 triggerEnabled false #txt
Mn0 f4 callSignature DefaultTaskListPage() #txt
Mn0 f4 caseData businessCase.attach=true #txt
Mn0 f4 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>DefaultTaskListPage</name>
    </language>
</elementInfo>
' #txt
Mn0 f4 @C|.responsibility Everybody #txt
Mn0 f4 81 177 30 30 -64 18 #rect
Mn0 f5 outLink DefaultEndPage.ivp #txt
Mn0 f5 inParamDecl '<Number endedTaskId> param;' #txt
Mn0 f5 requestEnabled true #txt
Mn0 f5 triggerEnabled false #txt
Mn0 f5 callSignature DefaultEndPage(Number) #txt
Mn0 f5 caseData businessCase.attach=true #txt
Mn0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>DefaultEndPage</name>
    </language>
</elementInfo>
' #txt
Mn0 f5 @C|.responsibility Everybody #txt
Mn0 f5 81 49 30 30 -50 20 #rect
Mn0 f6 outLink DefaultLoginPage.ivp #txt
Mn0 f6 inParamDecl '<String originalUrl> param;' #txt
Mn0 f6 requestEnabled true #txt
Mn0 f6 triggerEnabled false #txt
Mn0 f6 callSignature DefaultLoginPage(String) #txt
Mn0 f6 caseData businessCase.attach=true #txt
Mn0 f6 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>DefaultLoginPage</name>
    </language>
</elementInfo>
' #txt
Mn0 f6 @C|.responsibility Everybody #txt
Mn0 f6 81 241 30 30 -54 17 #rect
Mn0 f8 template "view/starts.xhtml" #txt
Mn0 f8 209 305 30 30 0 15 #rect
Mn0 f7 111 320 209 320 #arcP
Mn0 f10 template "view/tasks.xhtml" #txt
Mn0 f10 209 177 30 30 0 15 #rect
Mn0 f15 template "view/login.xhtml" #txt
Mn0 f15 209 241 30 30 0 15 #rect
Mn0 f16 111 256 209 256 #arcP
Mn0 f11 111 192 209 192 #arcP
Mn0 f12 outLink DefaultFramePage.ivp #txt
Mn0 f12 inParamDecl '<String relativeUrl,Number runningTaskId> param;' #txt
Mn0 f12 actionCode 'import ch.ivyteam.ivy.application.IApplication;

ch.ivyteam.ivy.request.IHttpResponse.current().sendRedirect(IApplication.current().getContextPath()+"/faces/view/workflow-ui/frame.xhtml?taskUrl="+param.relativeUrl);
' #txt
Mn0 f12 requestEnabled true #txt
Mn0 f12 triggerEnabled false #txt
Mn0 f12 callSignature DefaultFramePage(String,Number) #txt
Mn0 f12 caseData businessCase.attach=true #txt
Mn0 f12 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>DefaultFramePage.ivp</name>
    </language>
</elementInfo>
' #txt
Mn0 f12 @C|.responsibility Everybody #txt
Mn0 f12 80 368 32 32 -63 16 #rect
Mn0 f13 template "view/end.xhtml" #txt
Mn0 f13 209 49 30 30 0 15 #rect
Mn0 f9 111 64 209 64 #arcP
>Proto Mn0 .type ch.ivyteam.ivy.project.workflow.ui.Data #txt
>Proto Mn0 .processKind NORMAL #txt
>Proto Mn0 0 0 32 24 18 0 #rect
>Proto Mn0 @|BIcon #fIcon
Mn0 f0 mainOut f2 tail #connect
Mn0 f2 head f1 mainIn #connect
Mn0 f3 mainOut f7 tail #connect
Mn0 f7 head f8 mainIn #connect
Mn0 f6 mainOut f16 tail #connect
Mn0 f16 head f15 mainIn #connect
Mn0 f4 mainOut f11 tail #connect
Mn0 f11 head f10 mainIn #connect
Mn0 f5 mainOut f9 tail #connect
Mn0 f9 head f13 mainIn #connect
