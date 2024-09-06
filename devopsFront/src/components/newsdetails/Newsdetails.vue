<template>
<link rel="stylesheet" 
href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
  <div class="main-container">
    <div class="article-title-section">
      <h1>{{ news.title }}</h1>
    </div>
    <br>
    <br>
    <div class="meta-container">
      <div class="left">
        <span class="date">작성 {{ news.createdDate }}</span>
        <span class="author">{{ news.author }} 기자</span>
        
      </div>
      <div class="right">
        <span class="views">조회수 {{ news.views }}</span>
        <button @click="statusBookmark" class="bookmark">
          <span class="material-symbols-outlined">
              {{ isBookmarked ? 'bookmark_remove' : 'bookmark_add' }}
          </span>
          {{ isBookmarked ? '북마크 삭제' : '북마크에 추가' }}
        </button>
      </div>
    </div>

    <div class="content-wrapper">
      <span class="category">{{ news.categoryName }}</span> <!-- 카테고리 추가 -->
      <div class="image-section">
        <img :src="news.image" alt="기사 이미지" />
      </div>

      <div class="article-content-section">
        <p v-for="(sentence, index) in splitContent" :key="index">
          {{ sentence }}
        </p>
      </div>
    </div>
    <br />

    <!-- <div class="navigation-arrows">
        <div v-if="goBack"class="go-back">
            <img src="@/assets/go_back.png" @click="backTopage" class="goback_img" alt="뒤로 가기" width="20px"/>
        </div>
        <div class="go-forward">
            <img src="@/assets/go.png" class="go_img" alt="앞으로 가기" width="20px"/>
        </div>
      </div> 
    -->

    <div class="back-to-list">
      <router-link :to="backToList" class="back-button">목록으로 돌아가기</router-link>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import useNewsdetails from './Newsdetails.js';

export default {
  name: 'DetailNews',
  setup() {
    return useNewsdetails();
  },
  data() {
    return {
      news: {},
    };
  },
  created() {
    this.fetchNewsDetail();
  },

  methods: {
    async fetchNewsDetail() {
      try {
        const newsNo = this.$route.params.newsNo;
        const response = await axios.get(`http://localhost:8080/api/detail/${newsNo}`);
        console.log('News data:', response.data);
        this.news = response.data;
      } catch (error) {
        console.error('존재하지 않는 뉴스입니다. : ', error);
      }
    },

    getNewsById(id) {
      NewsService.getNewsById(id)
        .then((response) => {
          this.newsList = [response.data];
        })
        .catch((error) => {
          console.error('There was an error!', error);
        });
    },
  },

  computed: {
    splitContent() {
      // 문장마다 나누기
      return this.news.content ? this.news.content.split(/(?<=[.])[ ]+/) : [];
      // 문단마다 나누기
      // return this.news.content ? this.news.content.split('\n\n') : [];
    },
  },
};
</script>
