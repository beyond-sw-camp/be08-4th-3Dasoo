<template>
  <HeaderIT />
  <router-view :key="$route.fullPath" />
  <FooterIT />
</template>

<script setup>
import { onMounted } from 'vue';
import { RouterView, useRouter } from 'vue-router';
import axios from 'axios';
import HeaderIT from './components/common/HeaderIT.vue';
import FooterIT from './components/common/FooterIT.vue';
import { useUserStore } from './store/user';

onMounted(() => {
  const userStore = useUserStore();
  const router = useRouter();

  axios
    .get('http://localhost:8080/api/server-status')
    .then((response) => {
      const previousUuid = localStorage.getItem('serverUuid');
      const currentUuid = response.data.serverUuid;

      // console.log(previousUuid + '@@@' + currentUuid);

      if (previousUuid && previousUuid !== currentUuid) {
        userStore.logout();
        router.push('/').then(() => {
          // 메인 페이지로 이동 후 새로고침
          location.reload();
        });
      }

      localStorage.setItem('serverUuid', currentUuid);
    })
    .catch((error) => {
      console.error('서버 상태 확인 중 에러 발생:', error);
    });
});
// export default {
//   name: 'App',
//   components: {
//     HeaderIT,
//     FooterIT,
//   },
// };
</script>

<style scoped></style>
