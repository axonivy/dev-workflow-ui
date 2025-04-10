const initPreview = (baseUrl = 'http://localhost:8081') => {
  let previewAutoRefresh = JSON.parse(window.localStorage.getItem('preview-autorefresh')) ?? true;
  const iframe = document.getElementById('iFrame');

  const enableButton = (element) => {
    element?.removeAttribute('disabled');
    element?.removeAttribute('aria-disabled');
    element?.classList.remove('ui-state-disabled');
  }
  const enableElementPicker = () => enableButton(document.querySelector("#iFrameForm\\:previewElementPicker"));
  const enableAutoRefresh = () => enableButton(document.querySelector("#iFrameForm\\:previewAutoReload"))
  const toggleSpinAutoRefresh = () => document.querySelector("#iFrameForm\\:previewAutoReload .ui-icon").classList.toggle('si-is-spinning');
  const isJsonRpcMessage = (message) => typeof message === 'object' && 'jsonrpc' in message && message.jsonrpc === "2.0" && 'method' in message;
  const isRefreshMessage = (message) => isJsonRpcMessage(message) && message.method === 'refresh';

  const connection = new Promise(resolve => {
    const webSocket = new WebSocket(`${baseUrl}/ivy-hd-preview-lsp`);
    webSocket.onopen = () => {
      console.log('Preview WebSocket connection established');
      resolve(webSocket);
      enableElementPicker();
      enableAutoRefresh();
      if (previewAutoRefresh) {
        toggleSpinAutoRefresh();
      }
    };
    webSocket.onclose = () => console.log('Preview WebSocket connection closed');
    webSocket.onerror = (event) => console.log('Error occurred:', event);
    webSocket.onmessage = (event) => {
      console.log('Received message from server:', event.data);
      if (isRefreshMessage(JSON.parse(event.data)) && previewAutoRefresh) {
        const iframe = document.getElementById('iFrame');
        if (iframe) {
          iframe.contentWindow.location.reload();
        } else {
          window.location.reload();
        }
      }
    };
    return webSocket;
  })

  const sendNotification = async (method, params) => {
    connection.then(ws => ws.send(JSON.stringify({ jsonrpc: '2.0', method, params })));
  }

  const listenTo = async (url) => sendNotification('listenTo', url);

  const navigateTo = async (url, id) => sendNotification('navigateTo', { url, id });

  const toggleAutoRefresh = () => {
    previewAutoRefresh = !previewAutoRefresh;
    toggleSpinAutoRefresh();
    window.localStorage.setItem('preview-autorefresh', previewAutoRefresh);
  }

  return { listenTo, navigateTo, toggleAutoRefresh };
}
