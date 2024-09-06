  <template>
    <div id="content">
      <div class="container">
        <h3 class="bookmark-title">북마크한 뉴스</h3>
        <div v-if="bookmarkList.length === 0" class="no-bookmark">
          <p class="message">아직 북마크한 뉴스가 없습니다.</p>
          <p class="suggestion">관심 있는 뉴스를 북마크해보세요!</p>
          <router-link to="/news" class="explore-news">뉴스 보러 가기</router-link>
        </div>
        <div v-else class="news-grid">
          <div
            v-for="(item, index) in bookmarkList"
            :key="index"
            class="news-item"
          >
          <!-- 카테고리 이름 추가 -->
          <small class="category">{{ item.categoryName }}</small>

            <router-link :to="`/detail/${item.newsId}`">
              <img :src="item.newsImage" :alt="'뉴스 이미지 ' + (index + 1)" />
            </router-link>
            <div class="news-item-content">
              <h4>
                <router-link :to="`/detail/${item.newsId}`">{{ item.newsTitle }}</router-link>
              </h4>
              <p>{{ item.newsSummary }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>

  <script>
  import axios from "axios";
  import { useUserStore } from "@/store/user";

  export default {
    name: "Bookmark",
    data() {
      return {
        bookmarkList: [],
      };
    },
    methods: {
      async fetchBookmarkList() {
        try {
          const userStore = useUserStore();
          const userNo = userStore.user.userNo;
          const response = await axios.get(
            `http://localhost:8080/api/bookmark/${userNo}`
          );
          this.bookmarkList = response.data;
        } catch (error) {
          console.error("북마크를 가져오는 데 실패했습니다:", error);
        }
      },
    },
    created() {
      this.fetchBookmarkList();
    },
  };
  </script>

  <style scoped>
 /* 전체적인 레이아웃 스타일 */
html,
body {
  height: 100%;
  margin: 0;
  padding: 0;
  background-color: #eee9da;
}

body {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #eee9da;
}

#app {
  display: flex;
  flex-direction: column;
  width: 100%;
}

#content {
  flex: 1;
}

.container {
  background-color: #bdcdd6;
  padding: 20px;
  border-radius: 10px;
  width: 100%;
  max-width: 1050px;
  margin: 0 auto;
  margin-bottom: 20px;
  margin-top: 80px;
}

.bookmark-title {
  color: #333333;
  text-align: center; /* 제목 중앙 정렬 */
  margin: 10px 50px 25px 50px; /* 제목 상하 마진 (위쪽 간격 줄이기) */
  font-size: 32px; /* 제목 폰트 크기 */
  font-weight: bold; /* 제목 폰트 두께 */
  background: linear-gradient(to right, #000000, #434343);
  -webkit-background-clip: text; /* 텍스트에 그라디언트 적용 */
  color: transparent; /* 배경 그라디언트에 텍스트 색상 숨기기 */
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2); /* 텍스트 그림자 */
}

/* 카테고리 스타일 */
.category {
  background-color: #6096b4; /* 버튼 배경색 */
  color: #fff; /* 버튼 텍스트 색상 */
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 0.9em;
  position: absolute; /* 절대 위치 지정 */
  top: 10px; /* 상단 여백 */
  left: 10px; /*좌측 여백*/
  margin-left: 1px;
  text-align: center;
  transition: background-color 0.3s, transform 0.3s;
  z-index: 10;
}


h3 {
    color: #333333;
    text-align: center;
    margin: 25px 50px 25px 50px;
    font-size: 32px;
    font-weight: bold;
  }

/* 북마크가 없을 때의 스타일링 */
.no-bookmark {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  color: #555;
  margin: 50px auto;
  padding: 40px;
  border-radius: 10px;
  max-width: 800px;
  height: 400px;
}

.no-bookmark .message {
  font-size: 1.5em;
  font-weight: bold;
  margin-bottom: 10px;
}

.no-bookmark .suggestion {
  font-size: 1.2em;
  margin-bottom: 20px;
}

.explore-news {
  display: inline-block;
  padding: 10px 20px;
  background-color: #6096b4;
  color: #fff;
  border-radius: 5px;
  text-decoration: none;
}

.explore-news:hover {
  background-color: #507d9a;
}

/* 북마크된 뉴스 리스트 스타일링 */
.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 15px;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.news-item {
  position: relative;
  background: linear-gradient(145deg, #ffffff, #f0f0f0);
  border: 1px solid #bdc3c7;
  border-radius: 15px;
  padding: 15px;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  transition: box-shadow 0.3s ease-in-out, transform 0.3s ease-in-out;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  animation: fadeInUp 0.5s ease-out;
}

.news-item:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2); /* 호버 시 더 깊은 그림자 */
  transform: translateY(-5px); /* 호버 시 살짝 위로 올라오는 효과 */
}

.news-item img {
  width: 100%;
  height: auto;
  border-radius: 10px;
  margin-bottom: 15px;
  transition: transform 0.3s ease-in-out;
}

.news-item img:hover {
  transform: scale(1.05); /* 호버 시 이미지 확대 효과 */
}

.news-item-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.news-item-content h4 {
  background: linear-gradient(to right, #007bff, #00c6ff);
  -webkit-background-clip: text;
  color: transparent;
  font-weight: bold;
}

.news-item h4 {
  margin: 0;
  font-size: 1.2em;
  color: #333;
  transition: color 0.3s ease-in-out;
}

.news-item h4 a {
  text-decoration: none;
  color: inherit;
}

.news-item h4 a:hover {
  color: #6096b4; /* 제목 호버 시 색상 변화 */
}

.news-item p {
  flex: 1;
  margin: 10px 0 0 0;
  color: #555;
  line-height: 1.5; /* 가독성을 위한 줄 간격 조정 */
}

.news-item a {
  color: #6096b4;
  text-decoration: none;
}

.news-item a:hover {
  text-decoration: underline;
}

  </style>
