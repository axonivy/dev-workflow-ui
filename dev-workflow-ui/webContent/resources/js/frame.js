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
  console.log("checkAndReturnUrl() called", { newURL, originPage });

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

  console.warn("No redirect rule matched for", newURL);
  return undefined;
}

window.addEventListener('message', function (evt) {
  console.log("in frame.js dev-wf-ui");
  console.log(evt);
  if (evt.origin !== window.location.origin) { return; }
  if (evt.data?.type === 'ivy.redirect') {
    const originPage = new URLSearchParams(window.location.search).get('originalUrl');
    const newPage = checkAndReturnUrl(evt.data.url, originPage);
    if (newPage) {
      window.location = newPage;
    }
  }
});
