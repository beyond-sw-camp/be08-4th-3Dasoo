import axios from 'axios';
import '@/components/newsdetails/Newsdetails.css';
import { useRouter, useRoute } from 'vue-router';
import { computed, onMounted } from 'vue';
import { useUserStore } from '@/store/user';
import { useBookmarkStore } from '@/store/bookmark';
import { useNavigatorStore } from '@/store/navigator';
import Swal from 'sweetalert2';

export default function useNewsdetails() {
  const userStore = useUserStore();
  const bookmarkStore = useBookmarkStore();
  const naviStore = useNavigatorStore();

  const router = useRouter();
  const route = useRoute();

  const curNewsNo = Number(route.params.newsNo);

  const bookmarkList = computed(() => bookmarkStore.bookmarkList);
  const isBookmarked = computed(() => bookmarkList.value.some((news) => news.newsId === curNewsNo));

  router.beforeEach((to, from) => {
    // console.log("to", to.name, "from, ", from.name);
    naviStore.setPrev(from.name);
  });

  const fetchBookmarkList = async () => {
    if (userStore.isLoggedIn) {
      try {
        const response = await axios.get(`http://localhost:8080/api/bookmark/${userStore.user.userNo}`);
        bookmarkStore.setBookmarkList(response.data);
      } catch (error) {
        console.error('북마크 로드 실패:', error);
      }
    }
  };

  const statusBookmark = async () => {
    if (!userStore.isLoggedIn) {
        Swal.fire({
          title: '로그인이 필요합니다. 로그인 하시겠습니까?',
          text: '',
          icon: 'warning',
          
          showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
          confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
          cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
          confirmButtonText: '확인', // confirm 버튼 텍스트 지정
          cancelButtonText: '취소', // cancel 버튼 텍스트 지정
          iconColor: 'red'
          
          // reverseButtons: true, // 버튼 순서 거꾸로
          
          }).then(result => {
          // 만약 Promise리턴을 받으면,
          if (result.isConfirmed) { // 만약 모달창에서 confirm 버튼을 눌렀다면
              router.push('/login'); // 로그아웃 후 로그인 페이지로 리디렉션
          }
      });
    }

    try {
      if (isBookmarked.value) {
        await axios.delete('http://localhost:8080/api/deletebookmark', {
          params: {
            userId: userStore.user.userNo,
            newsId: curNewsNo,
          },
        });
        Swal.fire('북마크 삭제 완료.', '', 'success');
        bookmarkStore.deleteBookmark(curNewsNo);
      } else {
        await axios.post('http://localhost:8080/api/createbookmark', {
          userId: userStore.user.userNo,
          newsId: curNewsNo,
        });
        Swal.fire('북마크 추가 완료.', '', 'success');
        bookmarkStore.addBookmark(curNewsNo);
      }
    } catch (error) {
      if (error.response && error.response.status === 409) {
        Swal.fire('이미 추가된 뉴스입니다.', '', 'warning');
      } else {
        console.error('북마크 추가 실패:', error);
      }
    }
  };

  onMounted(() => {
    fetchBookmarkList();
  });

  const backToList = computed(() => {
    // console.log("naviStore.prev, ", naviStore.prev);
    if (naviStore.prev === 'bookmark') {
      // 이전 페이지가 북마크면
      return '/bookmark';
    } else {
      return '/';
    }
  });

  return {
    isBookmarked,
    statusBookmark,
    backToList,
    bookmarkList,
  };
}
