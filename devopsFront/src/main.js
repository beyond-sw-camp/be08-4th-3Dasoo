import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import axios from 'axios';
import { createPinia } from 'pinia'; // Pinia를 사용하기 위해 import
import { useUserStore } from '@/store/user'; // User Store import

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

import HeaderIT from './components/common/HeaderIT.vue';
import './assets/main.css';
import './assets/HFcss.css';

const app = createApp(App);
const pinia = createPinia(); // Pinia 인스턴스 생성

app.use(router);
app.use(pinia); // Pinia를 Vue 애플리케이션에 등록
app.config.globalProperties.$axios = axios;
app.component('HeaderIT', HeaderIT);
app.mount('#app');

// 사용자 정보를 로컬 스토리지에서 복원
const userStore = useUserStore();

userStore.loadUserFromStorage(); // 사용자 정보를 로컬 스토리지에서 로드
