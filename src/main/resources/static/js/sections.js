import { paintFooter } from './footer.js';

document.addEventListener('DOMContentLoaded', () => {
    const bodyElement = document.querySelector('body');
    const mainElement = bodyElement.querySelector('main');

    paintFooter(bodyElement, mainElement);
});
