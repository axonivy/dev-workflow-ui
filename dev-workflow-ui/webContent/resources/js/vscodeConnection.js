window.addEventListener("load", (event) => {
  window.postMessage({ type: 'frame-onload', url: window.location.href }, '*');
});
