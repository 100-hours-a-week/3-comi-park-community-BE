import { paintFooter } from './footer.js';

document.addEventListener('DOMContentLoaded', () => {
    const bodyElement = document.querySelector('body');
    const mainElement = bodyElement.querySelector('main');

    paintFooter(bodyElement, mainElement);

    bodyElement.querySelector('.header-backward-btn').addEventListener('click', () => {
        history.back();
    });
});