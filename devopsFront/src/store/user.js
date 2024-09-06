import { defineStore } from 'pinia';
import { useBookmarkStore } from './bookmark';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null, // 사용자 정보
    isLoggedIn: false, // 로그인 상태
  }),
  actions: {
    login(userData) {
      this.user = userData;
      this.isLoggedIn = true;

      localStorage.setItem('user', JSON.stringify(this.user));
      localStorage.setItem('isLoggedIn', 'true');
    },
    logout() {
      this.user = null;
      this.isLoggedIn = false;


      localStorage.removeItem('user');
      localStorage.removeItem('isLoggedIn');
      
      const bookmarkStore = useBookmarkStore();
      bookmarkStore.clearBookmarkList();
    },
    loadUserFromStorage() {
      const storedUser = localStorage.getItem('user');
      const storedIsLoggedIn = localStorage.getItem('isLoggedIn');

      if (storedUser && storedIsLoggedIn === 'true') {
        this.user = JSON.parse(storedUser);
        this.isLoggedIn = true;
      }
    },
  },
});
