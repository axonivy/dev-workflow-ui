// Define a type for the callback function
class DOMSelector {
  isSelecting = false;
  selectionOverlay = null;
  hoveredElement = null;  // Track hovered element
  originalOutline = ''; // Store the original outline style
  doc;
  nodeSelectedCallback;

  constructor(doc, callback) {
    this.doc = doc || document;
    this.nodeSelectedCallback = callback || null; // Use the passed callback or null
    //  triggerElement.addEventListener('click', this.toggleSelectionMode);
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
    // Create and attach the overlay
    this.selectionOverlay = this.doc.createElement('div');
    this.selectionOverlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5); /* Semi-transparent overlay */
        z-index: 9999; /* Ensure it's on top */
        pointer-events: none; /* Allow clicks to pass through */
      `;
    this.doc.body.appendChild(this.selectionOverlay);

    this.doc.addEventListener('click', this.handleElementClick);
    this.doc.addEventListener('mouseover', this.handleMouseOver); // Add mouseover listener
    this.doc.addEventListener('mouseout', this.handleMouseOut);   // Add mouseout listener

    console.log('Selection mode started');
  };

  endSelectionMode = () => {
    this.doc.removeEventListener('click', this.handleElementClick);
    this.doc.removeEventListener('mouseover', this.handleMouseOver); // Remove mouseover listener
    this.doc.removeEventListener('mouseout', this.handleMouseOut);   // Remove mouseout listener

    // Remove the overlay
    if (this.selectionOverlay) {
      this.doc.body.removeChild(this.selectionOverlay);
      this.selectionOverlay = null;
    }

    this.clearHoveredElement(); // remove the highlight of a hovered element if present

    this.isSelecting = false;
    console.log('Selection mode ended');
  };

  handleElementClick = (event) => {
    event.preventDefault();
    event.stopPropagation();

    if (this.hoveredElement) {
      if (this.nodeSelectedCallback) {
        this.nodeSelectedCallback(this.hoveredElement);
      } else {
        console.log('Selected element:', this.hoveredElement);
        alert(`Selected element: ${this.hoveredElement.tagName} #${this.hoveredElement.id}`);
      }

      // this.endSelectionMode();
    } else {
      console.log('No element with ID selected.');
      alert('Please select an element with an ID.'); // Or some other feedback.
    }
  };

  handleMouseOver = (event) => {
    let targetElement = event.target;
    let elementWithId = null;

    // Traverse up the DOM tree to find an element with an ID.
    while (targetElement) {
      if (targetElement.id && targetElement.classList.contains('ui-widget')) {
        elementWithId = targetElement;
        break;
      }
      targetElement = targetElement.parentElement; // Move to the parent
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
    this.originalOutline = element.style.outline;  // Store original outline
    element.style.outline = '2px solid red'; // Highlight the element
  };

  clearHoveredElement = () => {
    if (this.hoveredElement) {
      this.hoveredElement.style.outline = this.originalOutline; // Restore original outline
      this.hoveredElement = null;
    }
  };
}

// Usage (assuming you have a button with id "toggleButton")
//  const toggleButton = document.getElementById('toggleButton');
//  if (toggleButton) {
//   const selector = new DOMSelector(toggleButton, (node) => {
//    console.log("The selected node is:", node);
//    // do something with the node here
//   });
//  } else {
//   console.error('Toggle button not found');
//  }

var selector;
const toggleSelectionMode = () => {
  if (!selector) {
    selector = new DOMSelector(document.getElementById('iFrame').contentDocument, (node) => console.log("The selected node is:", node))
  }
  selector.toggleSelectionMode()
}
