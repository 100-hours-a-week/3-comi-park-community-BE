const createAnchorElement = (href, content) => {
    const anchorElement = document.createElement('a');
    anchorElement.setAttribute('href', href);
    anchorElement.textContent = content;

    return anchorElement;
};

export const paintFooter = (parentElement, mainElement) => {
    const footerElement = document.createElement('footer');

    footerElement.appendChild(createAnchorElement('/terms', '이용약관'));
    footerElement.appendChild(createAnchorElement('/privacy', '개인정보처리방침'));

    parentElement.appendChild(footerElement);

    footerScrollHandler(mainElement, footerElement);
};

const footerScrollHandler = (mainElement, footerElement) => {
    const checkFooterVisibility = () => {
        if (!footerElement) return;

        const scrollPosition = window.scrollY + window.innerHeight;
        const documentHeight = document.documentElement.scrollHeight;

        // 페이지 전체 높이가 화면보다 짧으면 항상 footer 표시
        if (documentHeight <= window.innerHeight) {
            footerElement.classList.add('visible');
            // footer가 표시될 때 <main> 콘텐츠를 가리지 않도록 함
            mainElement.style.paddingBottom = '52px';
            return;
        }

        // 스크롤 끝에 도달하면 footer 표시
        if (scrollPosition >= documentHeight - 5) {
            footerElement.classList.add('visible');
            // footer가 표시될 때 <main> 콘텐츠를 가리지 않도록 함
            mainElement.style.paddingBottom = '52px';
        } else {
            footerElement.classList.remove('visible');
        }
    };

    window.addEventListener('scroll', checkFooterVisibility);
    window.addEventListener('resize', checkFooterVisibility);

    checkFooterVisibility();
};
