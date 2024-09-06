import axios from 'axios';

const NEWS_API_BASE_URL = 'http://localhost:30031/api/main';

class NewsService {
  getNewsList() {
    return axios.get(NEWS_API_BASE_URL);
  }
  getNewsById(id) {
    return axios.get(`${NEWS_API_BASE_URL}/${id}`);
  }
  // 검색 메서드 추가
  searchNews(query) {
    return axios.get(`${NEWS_API_BASE_URL}/search`, {
      params: { query },
    });
  }
  // 상위 3개 뉴스 가져오기
  getTop3News() {
    return axios.get(`${NEWS_API_BASE_URL}/top`);
  }
}

export default new NewsService();
