<template>
    <footer>
        <div class="footer-container">
            <div class="footer-logo">
                <img src="../../image/logo.png" alt="4st IT Logo">
            </div>
            <div class="footer-links">
                <ul>
                    <li><a href="https://github.com/beyond-sw-camp/be08-3rd-4cari-4stIT">회사소개</a></li>
                    <li><a href="#">연락처</a></li>
                    <li><a href="#">개인정보처리방침</a></li>
                    <li><a href="#">이용약관</a></li>
                </ul>
            </div>
            <div class="footer-contact">
                <p>Contact:  <a :href="'mailto:' + contactEmail">{{ contactEmail }}</a></p>
            </div>
            <div class="footer-copyright">
                <p>&copy; 2024 4stIT. All rights reserved.</p>
            </div>
        </div>
    </footer>
</template>

<script>

export default {
    name: 'FooterIT',
    data() {
        return {
            contactEmail: 'contact@4stIT.com'
        };
    }
}
</script>
