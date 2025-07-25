let hasRedirected = false;

function iframeURLChange() {
  const iframe = document.getElementById("iFrame");
  const loader = document.getElementById("iframeLoader");
  if (!iframe) {
    return;
  }
  console.log("iframeURLChange() called and iframe found");

  var lastDispatched = null;

  // Show loader when iframe starts loading
  function showLoader() {
    if (loader) loader.style.display = "flex";
    iframe.style.visibility = "hidden";
  }
  // Hide loader when iframe is ready
  function hideLoader() {
    if (loader) loader.style.display = "none";
    iframe.style.visibility = "visible";
  }

  // Initial loader state
  showLoader();

  const updateHistory = (newHref) => {
    console.log("in updateHistory() newHref is: ", newHref);
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
    setTimeout(() => {
      showLoader();
    }, 0);
  };

  const attachUnload = () => {
    iframe.contentWindow.removeEventListener("unload", unloadHandler);
    iframe.contentWindow.addEventListener("unload", unloadHandler);
  };

  console.log("attaching load event listener to iframe");
  iframe.addEventListener("load", function () {
    try {
      // Try to access loaded iframe content
      iframe.contentWindow.content;
      console.log("in iframe.addEventListener load event listener");
      attachUnload();
      // Hide loader when iframe is ready
      hideLoader();
      // Update history and preview
      const newHref = iframe.contentWindow.location.href;
      updateHistory(newHref);
      preview?.listenTo(newHref);
      // Trigger remote command to update task name
      if (typeof useTaskInIFrame === 'function') {
        useTaskInIFrame([{ name: "url", value: iframe.contentWindow.location.pathname }]);
      }
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

// Strict whitelist of allowed redirect destinations
const allowedPages = Object.freeze([
  'home.xhtml',
  'starts.xhtml',
  'task.xhtml',
  'tasks.xhtml',
  'cases.xhtml',
  'case.xhtml',
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
    // Optionally, log/report this event for security monitoring
  }
}

window.addEventListener('message', function (evt) {
  // Only accept messages from same origin
  if (evt.origin !== window.location.origin) {
    console.warn('Blocked postMessage from unexpected origin:', evt.origin);
    return;
  }
  if (evt.data?.type === 'ivy.redirect') {
    const originPage = new URLSearchParams(window.location.search).get('originalUrl');
    console.log("in frame.js dev-wf-ui. Got message from iframe. event data type is ivy.redirect, url is: ", evt.data.url);
    console.log("originPage is: ", originPage);
    const newPage = checkAndReturnUrl(evt.data.url, originPage);
    console.log("checkAndReturnUrl() returned: ", newPage);
    if (newPage) {
      console.log("calling safeRedirect() inside window.addEventListener message listener");
      safeRedirect(newPage, 'postMessage');
    } else {
      console.warn('Blocked unsafe redirect attempt via postMessage:', evt.data.url);
    }
  }
});
