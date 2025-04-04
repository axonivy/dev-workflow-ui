class DOMSelector {
  isSelecting = false;
  selectionOverlay = null;
  hoveredElement = null;
  originalOutline = '';
  doc;
  nodeSelectedCallback;

  constructor(doc, callback) {
    this.doc = doc || document;
    this.nodeSelectedCallback = callback || null;
  }

  toggleSelectionMode = () => {
    this.isSelecting = !this.isSelecting;

    if (this.isSelecting) {
      this.startSelectionMode();
    } else {
      this.endSelectionMode();
    }
  };

  startSelectionMode = () => {
    this.selectionOverlay = this.doc.createElement('div');
    this.selectionOverlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        z-index: 9999;
        pointer-events: none;
      `;
    this.doc.body.appendChild(this.selectionOverlay);

    this.doc.addEventListener('click', this.handleElementClick, { capture: true });
    this.doc.addEventListener('mousedown', this.stopEvent, { capture: true });
    this.doc.addEventListener('mouseup', this.stopEvent, { capture: true });
    this.doc.addEventListener('mouseover', this.handleMouseOver);
    this.doc.addEventListener('mouseout', this.handleMouseOut);

    console.log('Selection mode started');
  };

  stopEvent = (event) => {
    event.preventDefault();
    event.stopPropagation();
  };

  endSelectionMode = () => {
    this.doc.removeEventListener('click', this.handleElementClick, { capture: true });
    this.doc.removeEventListener('mousedown', this.stopEvent, { capture: true });
    this.doc.removeEventListener('mouseup', this.stopEvent, { capture: true });
    this.doc.removeEventListener('mouseover', this.handleMouseOver);
    this.doc.removeEventListener('mouseout', this.handleMouseOut);

    if (this.selectionOverlay) {
      this.doc.body.removeChild(this.selectionOverlay);
      this.selectionOverlay = null;
    }

    this.clearHoveredElement();

    this.isSelecting = false;
    console.log('Selection mode ended');
  };

  handleElementClick = (event) => {
    this.stopEvent(event);

    if (this.hoveredElement) {
      if (this.nodeSelectedCallback) {
        this.nodeSelectedCallback(this.hoveredElement);
      } else {
        console.log('Selected element:', this.hoveredElement);
        alert(`Selected element: ${this.hoveredElement.tagName} #${this.hoveredElement.id}`);
      }

      this.endSelectionMode();
    } else {
      console.log('No element with ID selected.');
      alert('Please select an element with an ID.');
    }
  };

  handleMouseOver = (event) => {
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
      this.highlightElement(elementWithId);
    }
  };

  handleMouseOut = (event) => {
    this.clearHoveredElement();
  };

  highlightElement = (element) => {
    if (this.hoveredElement) {
      this.clearHoveredElement();
    }

    this.hoveredElement = element;
    this.originalOutline = element.style.outline;
    element.style.outline = '2px solid red';
  };

  clearHoveredElement = () => {
    if (this.hoveredElement) {
      this.hoveredElement.style.outline = this.originalOutline;
      this.hoveredElement = null;
    }
  };
}

var selector;
const toggleSelectionMode = () => {
  if (!selector) {
    selector = new DOMSelector(document.getElementById('iFrame').contentDocument, (node) => console.log("The selected node is:", node.id))
  }
  selector.toggleSelectionMode()
}
