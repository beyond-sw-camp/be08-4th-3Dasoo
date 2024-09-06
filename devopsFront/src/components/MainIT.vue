<template>
    <section class="main-section">
        <div class="container px-lg-5" style="margin-top: 80px;">
            <div class="p-4 p-lg-5 bg-light rounded-3 text-center">
                <swiper :space-between="30" :centeredSlides="true" :autoplay="autoplayOptions" :navigation="true"
                    :pagination="{
                        clickable: true
                    }" :modules="modules" class="swiper">
                    <swiper-slide v-for="(news, index) in newsTop3" :key="index">
                        <router-link :to="`/detail/${news.newsId}`" class="news-item-link">
                            <div class="slide-content">
                                <img :src="news.image" class="main-news-image" />
                                <div class="slide-text">
                                    <h2>{{ news.title }}</h2>
                                    <br>
                                    <p>{{ news.content }}</p>
                                </div>
                            </div>
                        </router-link>
                    </swiper-slide>
                </swiper>
            </div>
        </div>
    </section>
    <CardIT />
</template>

<script>
import { Swiper, SwiperSlide } from 'swiper/vue';
import { ref, onMounted } from 'vue';
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import { Autoplay, Navigation, Pagination } from 'swiper/modules';
import NewsService from '@/services/NewsService';
import CardIT from './CardIT.vue';

export default {
    components: {
        Swiper,
        SwiperSlide,
        CardIT
    },
    setup() {
        const newsTop3 = ref([]);
        const autoplayOptions = {
            delay: 3000,
            disableOnInteraction: false
        };
        const modules = [Autoplay, Navigation, Pagination];

        const getTop3News = async () => {
            try {
                const response = await NewsService.getTop3News();
                newsTop3.value = response.data;
                console.log(newsTop3.value);
            } catch (error) {
                console.error('Error fetching news:', error);
            }
        };

        

        onMounted(() => {
            getTop3News();
            
        });
        

        return {
            modules,
            autoplayOptions,
            newsTop3
        };
    }
};
</script>

<style scoped>

.main-section {
    display: flex;
    justify-content: center;
    align-items: center;
}

.container {
    width: 1230px;    
    margin-bottom: 40px;
    border-radius: 4px;
    background-color: #ffffff;
    border: 2px solid #e0e9ee;
    box-shadow: 0px 6px 20px rgba(96, 150, 180, 0.4);
}

.swiper {
    width: 100%;
    height: auto;
}

/* . . . 아래로 띄움 */
#swp { 
    margin-bottom: 50px;
}

.swiper-slide {
    text-align: center;
    font-size: 18px;
    background: #fff;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
}

.slide-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    width: 100%;
    padding: 10px;
    box-sizing: border-box;
    margin-bottom: 50px;

}

.main-news-image {
    display: block;
    width: 100%;
    height: 350px;
    object-fit: cover;
}

.slide-text {
    text-align: center;
    margin-top: 10px;
    width: 100%; 
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 3;
    overflow: hidden; 
    text-overflow: ellipsis;
}

.slide-text h2 {
    background: linear-gradient(to right, #007bff, #00c6ff);
    -webkit-background-clip: text;
    color: transparent;
    font-weight: bold;
}

.news-item-link {
    text-decoration: none; /* 밑줄 제거 */
    color: inherit; /* 기본 텍스트 색상 사용 */
}

.news-item-link:hover {
    color: inherit; /* 호버 시에도 기본 텍스트 색상 유지 */
}

.news-item-link h2, .news-item-link p {
    margin: 0;
}
</style>

