function iframeURLChange() {
  const iframe = document.getElementById("iFrame");
  if (!iframe) {
    return;
  }

  var lastDispatched = null;

  const redirectMainWindow = (newHref, iframe) => {
    const originPage = new URLSearchParams(window.location.search).get("originalUrl");
    if (iframe.contentWindow.location.pathname.match("/default/redirect.xhtml")) {
      const redirectedPage = new URLSearchParams(iframe.contentWindow.location.search).get("redirectPage");
      const newPage = checkAndReturnUrl(redirectedPage, originPage);
      if (newPage) {
        safeRedirect(newPage, "redirectMainWindow");
      } else {
        safeRedirect(redirectedPage, "redirectMainWindow");
      }
    } else {
      const newPage = checkAndReturnUrl(newHref, originPage);
      if (newPage) {
        safeRedirect(newPage, "redirectMainWindow");
      }
    }
  };

  const dispatchChange = () => {
    useTaskInIFrame([
      {
        name: "url",
        value: iframe.contentWindow.location.pathname,
      },
    ]);

    const newHref = iframe.contentWindow.location.href;

    if (newHref !== lastDispatched) {
      redirectMainWindow(newHref, iframe);
      lastDispatched = newHref;
      updateHistory(newHref);
      preview?.listenTo(newHref);
    }
  };

  const updateHistory = (newHref) => {
    const newHrefUrl = new URL(newHref);
    const historyUrl = new URL(window.location);
    historyUrl.searchParams.set(
      "taskUrl",
      newHrefUrl.pathname + newHrefUrl.search
    );
    history.replaceState({}, "", historyUrl);
  };

  const unloadHandler = () => {
    // Timeout needed because the URL changes immediately after
    // the `unload` event is dispatched.
    setTimeout(dispatchChange, 0);
    iframe.style.visibility = "hidden";
  };

  const attachUnload = () => {
    // Remove the unloadHandler in case it was already attached.
    // Otherwise, there will be two handlers, which is unnecessary.
    iframe.contentWindow.removeEventListener("unload", unloadHandler);
    iframe.contentWindow.addEventListener("unload", unloadHandler);
  };

  iframe.addEventListener("load", function () {
    try {
      // Try to access loaded iframe content
      iframe.contentWindow.content;
      attachUnload();
      // Just in case the change wasn't dispatched during the unload event...
      dispatchChange();
      iframe.style.visibility = "visible";
    } catch (e) {
      // Open iframe content in current window if it could not be loaded
      safeRedirect(iframe.src);
    }
  });

  attachUnload();
}

const allowedPages = Object.freeze([
  'home.xhtml',
  'starts.xhtml',
  'task.xhtml',
  'tasks.xhtml',
  'case.xhtml',
  'cases.xhtml',
  'login.xhtml',
  'switch-user.xhtml',
  'end.xhtml'
]);

function safeRedirect(newPage, source = 'unknown') {
  if (hasRedirected) {
    console.log("safeRedirect: already redirected once, ignoring further call to", newPage);
    return;
  }
  const pagePath = newPage.split('?')[0];
  if (allowedPages.includes(pagePath)) {
    console.log(`safeRedirect() navigating to: ${newPage} (triggered by ${source})`);
    hasRedirected = true;
    window.location = newPage;
  } else {
    console.warn(`Blocked unsafe redirect to: ${newPage} (triggered by ${source})`);
  }
}

function checkAndReturnUrl(newURL, originPage) {
  const rules = [
    {
      match: (url) => url.includes("task.xhtml"),
      target: (url) => url.substring(url.indexOf("task.xhtml")),
    },
    {
      match: (url) => url.includes("?endedTaskId="),
      target: () => originPage,
    },
    {
      match: (url) =>
        url.endsWith("/faces/home.xhtml") ||
        url.includes("DefaultApplicationHomePage.ivp") ||
        url.endsWith("/app/home.xhtml"),
      target: () => "home.xhtml",
    },
    {
      match: (url) =>
        url.endsWith("/faces/tasks.xhtml") ||
        url.includes("DefaultTaskListPage.ivp") ||
        url.endsWith("/app/tasks.xhtml"),
      target: () => "tasks.xhtml",
    },
    {
      match: (url) =>
        url.endsWith("/faces/starts.xhtml") ||
        url.includes("DefaultProcessStartListPage.ivp") ||
        url.endsWith("/app/starts.xhtml"),
      target: () => "starts.xhtml",
    },
    {
      match: (url) =>
        url.endsWith("/faces/login.xhtml") ||
        url.includes("DefaultLoginPage.ivp") ||
        url.endsWith("/app/login.xhtml"),
      target: () => "login.xhtml",
    },
    {
      match: (url) => url.endsWith("/faces/switch-user.xhtml"),
      target: () => "switch-user.xhtml",
    },
    {
      match: (url) =>
        url.endsWith("/faces/end.xhtml") ||
        url.includes("DefaultEndPage.ivp") ||
        url.endsWith("/app/end.xhtml"),
      target: () => originPage,
    },
  ];

  for (const rule of rules) {
    if (rule.match(newURL)) {
      const dest = rule.target(newURL);
      console.log("Redirect rule matched", { newURL, dest });
      return dest;
    }
  }

  return undefined;
}
