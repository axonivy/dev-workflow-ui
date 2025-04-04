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
        window.location = newPage;
      } else {
        window.location = redirectedPage;
      }
    } else {
      const newPage = checkAndReturnUrl(newHref, originPage);
      if (newPage) {
        window.location = newPage;
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
      window.location = iframe.src;
    }
  });

  attachUnload();
}

function checkAndReturnUrl(newURL, originPage) {
  if (newURL.includes("task.xhtml")) {
    return newURL.substring(newURL.indexOf("task.xhtml"));
  }
  if (newURL.includes("?endedTaskId=")) {
    return originPage;
  }
  if (
    newURL.endsWith("/faces/home.xhtml") ||
    newURL.includes("DefaultApplicationHomePage.ivp") ||
    newURL.endsWith("/app/home.xhtml")
  ) {
    return "home.xhtml";
  }
  if (
    newURL.endsWith("/faces/tasks.xhtml") ||
    newURL.includes("DefaultTaskListPage.ivp") ||
    newURL.endsWith("/app/tasks.xhtml")
  ) {
    return "tasks.xhtml";
  }
  if (
    newURL.endsWith("/faces/starts.xhtml") ||
    newURL.includes("DefaultProcessStartListPage.ivp") ||
    newURL.endsWith("/app/starts.xhtml")
  ) {
    return "starts.xhtml";
  }
  if (
    newURL.endsWith("/faces/login.xhtml") ||
    newURL.includes("DefaultLoginPage.ivp") ||
    newURL.endsWith("/app/login.xhtml")
  ) {
    return "login.xhtml";
  }
  if (newURL.endsWith("/faces/loginTable.xhtml")) {
    return "loginTable.xhtml";
  }
  if (
    newURL.endsWith("/faces/end.xhtml") ||
    newURL.includes("DefaultEndPage.ivp") ||
    newURL.endsWith("/app/end.xhtml")
  ) {
    return originPage;
  }
  return undefined;
}
