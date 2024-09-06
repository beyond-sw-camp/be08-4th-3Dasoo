<template>
  <div>
    <HeaderIT @search="handleSearchResults" />
    <section class="pt-4 custom-section">
      <div class="container px-lg-5">
        <div class="col-lg-6 mb-4" v-for="news in newsList" :key="news.newsId">
          <router-link :to="`/detail/${news.newsId}`" class="news-item-link">
            <div class="card bg-light border-0 h-100 custom-card1">
              <div class="category-label">{{ news.categoryName }}</div>
              <div class="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                <div class="card-news1">
                  
                  <img :src="news.image" alt="Image 4" class="card-image" />
                  <div class="card1-text">          
                    <h2>{{ news.title }}</h2>
                    <br />
                    <p>{{ news.content }}</p>
                  </div>
                </div>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { defineComponent, ref, onMounted, watch } from 'vue';
import NewsService from '@/services/NewsService';
import HeaderIT from './common/HeaderIT.vue';
import { useRoute } from 'vue-router';
import { useUserStore } from '@/store/user';

export default defineComponent({
  components: {
    HeaderIT,
  },
  setup() {
    const userStore = useUserStore();
    const newsList = ref([]);
    const route = useRoute();

    // Function to fetch news based on search query
    const searchNews = async (query) => {
      try {
        if (query) {
          const response = await NewsService.searchNews(query);
          newsList.value = response.data;
        } else {
          if (userStore.isLoggedIn) {
            const response = await NewsService.getNewsById(userStore.user.userNo);
            newsList.value = response.data;
          } else {
            const response = await NewsService.getNewsList();
            newsList.value = response.data;
          }
        }
      } catch (error) {
        console.error('Error fetching news:', error);
      }
    };

    // Handle search results from HeaderIT component
    const handleSearchResults = (query) => {
      searchNews(query);
    };

    // Fetch news based on the initial query or default to full list
    const searchQuery = ref(route.query.search || '');
    onMounted(() => {
      searchNews(searchQuery.value);
    });

    // Watch for changes in the search query to re-fetch search results
    watch(
      () => route.query.search,
      (newSearchQuery) => {
        searchQuery.value = newSearchQuery || '';
        searchNews(searchQuery.value);
      }
    );

    return {
      newsList,
      handleSearchResults, // Added method to handle search results
    };
  },
});
</script>

<style scoped>

.custom-section {
  margin-left: 20px;
  padding-right: 20px;
}

.custom-card1 {
  width: 100%;
  height: 350px; /* 고정된 높이 설정 */
  margin-bottom: 20px;
  padding: 20px;
  width: 100%;
  border-radius: 4px;
  background-color: #ffffff;
  border: 2px solid #e0e9ee;
  box-shadow: 0px 6px 20px rgba(96, 150, 180, 0.4);
  position: relative; /* 카테고리 라벨을 위치시키기 위해 상대적 위치 설정 */
}

.custom-card1:hover {
  transform: scale(1.02);
  box-shadow: 0px 8px 25px rgba(96, 150, 180, 0.5);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.card-news1 {
  position: relative; /* 카테고리 라벨을 상대적으로 위치시키기 위해 설정 */
}

.card-news1 img {
  width: 100%;
  height: 200px; /* 이미지의 높이 고정 */
  object-fit: cover;
}

.card1-text {
  padding: 20px;
  text-align: center;
  height: 150px; /* 고정된 높이 설정 */
  display: flex;
  flex-direction: column;
  justify-content: center;
}

/* 카테고리 버튼 스타일링 */
.category-label {
  background-color: #6096b4; /* 버튼 배경색 */
  color: #fff; /* 버튼 텍스트 색상 */
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 0.9em;
  position: absolute; /* 절대 위치 지정 */
  top: 10px; /* 상단 여백 */
  left: 10px; /* 좌측 여백 */
  text-align: center;
  transition: background-color 0.3s, transform 0.3s;
  z-index: 10;
}

.category-label:hover {
  background-color: #507d9a; /* 호버 시 버튼 배경색 */
  transform: scale(1.05);
}

.card1-text h2 {
  background: linear-gradient(to right, #007bff, #00c6ff);
  -webkit-background-clip: text;
  color: transparent;
  font-weight: bold;
}

.card1-text p {
  margin-bottom: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3; /* 표시할 줄 수 설정 */
  -webkit-box-orient: vertical;
}

.news-item-link {
  text-decoration: none; /* 밑줄 제거 */
  color: inherit; /* 기본 텍스트 색상 사용 */
}

.nav-tabs .nav-link {
  border: none;
}

.nav-tabs .nav-link.active {
  border-bottom: 2px solid #007bff;
}

</style>
