import { paintFooter } from './footer.js';

document.addEventListener('DOMContentLoaded', () => {
    const bodyElement = document.querySelector('body');
    const mainElement = bodyElement.querySelector('main');

    paintFooter(bodyElement, mainElement);

    bodyElement.querySelector('.header-backward-btn').addEventListener('click', () => {
        if (ALLOWED_ORIGINS.some(origin => document.referrer.includes(origin))) {
            history.back();
        }
    });
});