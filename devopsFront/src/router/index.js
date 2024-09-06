import { createRouter, createWebHistory } from 'vue-router';
// import HomeView from '../views/HomeView.vue'
import Bookmark from '@/components/Bookmark.vue';
import MainIT from '@/components/MainIT.vue';
import Newsdetails from '@/components/newsdetails/Newsdetails.vue';
import JoinView from '@/components/JoinView.vue';
import MyPageView from '@/components/MyPageView.vue';
import Login from '@/components/Login.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: MainIT,
    },
    {
      path: '/bookmark',
      name: 'bookmark',
      component: Bookmark,
    },
    {
      path: '/detail/:newsNo',
      name: 'Newsdetails',
      component: Newsdetails,
    },
    {
      path: '/join',
      name: 'JoinView',
      component: JoinView,
    },
    {
      path: '/mypage', 
      name: 'MyPageView',
      component: MyPageView,
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/:catchAll(.*)', // 와일드카드 라우트
      redirect: '/', // 없는 페이지로 접근 시 '/' 경로로 리디렉션
    },
  ],
});

// 경로 대문자로 받아도 소문자로 바꾸는 로직
router.beforeEach((to, from, next) => {
  const path = to.path.toLowerCase();
  if (to.path !== path) {
    next({ path });
  } else {
    next();
  }
});

export default router;
