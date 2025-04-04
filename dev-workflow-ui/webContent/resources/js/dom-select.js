const initDomSelector = () => {
  const iframe = document.getElementById('iFrame');
  let isSelecting = false;
  let selectionOverlay = null;
  let hoveredElement = null;
  let originalOutline = '';

  const stopEvent = (event) => {
    event.preventDefault();
    event.stopPropagation();
  };

  const handleElementClick = (event) => {
    stopEvent(event);
    if (!hoveredElement) {
      console.log('No element with ID selected.');
      return;
    }
    console.log('Selected element:', hoveredElement);
    preview?.navigateTo(iframe.contentWindow.location.href, hoveredElement.id);
    endSelectionMode();
  };

  const handleMouseOver = (event) => {
    let targetElement = event.target;
    let elementWithId = null;

    while (targetElement) {
      if (targetElement.id && targetElement.classList.contains('ui-widget')) {
        elementWithId = targetElement;
        break;
      }
      targetElement = targetElement.parentElement;
    }

    if (elementWithId) {
      highlightElement(elementWithId);
    }
  };

  const handleMouseOut = () => clearHoveredElement();

  const highlightElement = (element) => {
    clearHoveredElement();
    hoveredElement = element;
    originalOutline = element.style.outline;
    element.style.outline = '2px solid red';
  };

  const clearHoveredElement = () => {
    if (hoveredElement) {
      hoveredElement.style.outline = originalOutline;
      hoveredElement = null;
    }
  };

  const startSelectionMode = () => {
    const doc = iframe.contentDocument;
    selectionOverlay = doc.createElement('div');
    selectionOverlay.style.cssText = `
        position: fixed;
        inset: 0;
        background-color: rgba(0, 0, 0, 0.2);
        z-index: 9999;
        pointer-events: none;
      `;
    doc.body.appendChild(selectionOverlay);

    doc.addEventListener('click', handleElementClick, { capture: true });
    doc.addEventListener('mousedown', stopEvent, { capture: true });
    doc.addEventListener('mouseup', stopEvent, { capture: true });
    doc.addEventListener('mouseover', handleMouseOver);
    doc.addEventListener('mouseout', handleMouseOut);

    console.log('Selection mode started');
  };

  const endSelectionMode = () => {
    const doc = iframe.contentDocument;
    doc.removeEventListener('click', handleElementClick, { capture: true });
    doc.removeEventListener('mousedown', stopEvent, { capture: true });
    doc.removeEventListener('mouseup', stopEvent, { capture: true });
    doc.removeEventListener('mouseover', handleMouseOver);
    doc.removeEventListener('mouseout', handleMouseOut);

    if (selectionOverlay) {
      doc.body.removeChild(selectionOverlay);
      selectionOverlay = null;
    }

    clearHoveredElement();

    isSelecting = false;
    console.log('Selection mode ended');
  };

  const toggleSelectionMode = () => {
    isSelecting = !isSelecting;
    if (isSelecting) {
      startSelectionMode();
    } else {
      endSelectionMode();
    }
  };

  return { toggleSelectionMode }
}
