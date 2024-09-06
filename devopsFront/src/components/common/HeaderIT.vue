<template>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
    <nav class="navbar navbar-expand-lg bg-navbar fixed-top">
        <div class="container px-lg-5">
            <RouterLink class="navbar-brand custom-navbar-brand" :to="{ name: 'main' }">4ST-IT</RouterLink>
            <button
                class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent"
                aria-expanded="false"   
                aria-label="Toggle navigation"
            >
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <div class="d-flex flex-grow-1 justify-content-center" id="search">
                    <form class="search-box" @submit.prevent="onSearch">
                        <input class="search-txt" v-model="searchQuery" type="text" placeholder="검색어를 입력하세요." />
                        <button class="search-button" type="submit">
                            <span class="material-symbols-outlined">search</span>
                        </button>
                    </form>
                </div>
            </div>
            <div class="navbar-nav-wrapper">
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                    <li v-if="!isLoggedIn" class="nav-item">
                        <RouterLink class="nav-link custom-nav-link" :to="{ name: 'JoinView' }">회원가입</RouterLink>
                    </li>
                    <li v-if="!isLoggedIn" class="nav-item">
                        <RouterLink class="nav-link custom-nav-link" :to="{ name: 'login' }">로그인</RouterLink>
                    </li>
                    <li v-if="isLoggedIn" class="nav-item">
                        <RouterLink class="nav-link custom-nav-link" :to="{ name: 'bookmark' }">북마크</RouterLink>
                    </li>
                    <li v-if="isLoggedIn" class="nav-item">
                        <RouterLink class="nav-link custom-nav-link" :to="{ name: 'MyPageView' }">마이페이지</RouterLink>
                    </li>
                    <li v-if="isLoggedIn" class="nav-item">
                        <a class="nav-link custom-nav-link" href="#!" @click="logout">로그아웃</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>
    <script>
    import { defineComponent, ref, computed, onMounted } from 'vue';
    import { useRouter } from 'vue-router';
    import { useUserStore } from '@/store/user';
    import Swal from 'sweetalert2';

    export default defineComponent({
    name: 'HeaderIT',
    setup() {
    const userStore = useUserStore();
    const router = useRouter();
    const searchQuery = ref('');

    // 로그인 상태를 Pinia의 상태로부터 계산
    const isLoggedIn = computed(() => userStore.isLoggedIn);

    const onSearch = async () => {
        try {
        await router.push({
            name: 'main',
            query: { search: searchQuery.value },
        });
        } catch (error) {
        console.error('검색 중 오류가 발생했습니다:', error);
        }
    };

    const logout = () => {

        // if (confirm('로그아웃 하시겠습니까?')) {
        // userStore.logout(); // Pinia 스토어의 logout 메서드 호출
        // router.push('/'); // 로그아웃 후 로그인 페이지로 리디렉션
        // }
        Swal.fire({
            title: '로그아웃 하시겠습니까?',
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
                userStore.logout(); // Pinia 스토어의 logout 메서드 호출
                router.push('/'); // 로그아웃 후 로그인 페이지로 리디렉션
            }
        });
    };

    return {
        searchQuery,
        isLoggedIn,
        onSearch,
        logout,
    };
    },
    });
    </script>

<style scoped>
.navbar {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 1030;
    background-color: #6096B4;
}

.navbar-brand {
    display: flex;
    align-items: center;
    height: 100%;
    margin-bottom: 0;
    padding: 0;
    position: absolute;
    left: 10%;
    top: 50%;
    transform: translateY(-50%);
}


.navbar-brand-custom {
    left: 10%;
}

.search-box {
    display: flex;
    align-items: center;
    position: absolute;
    top: 50%; 
    left: 50%;
    width: 50%;
    transform: translate(-50%, -50%);
}

.search-txt {
    flex: 1;
    padding: 5px;
    font-size: 14px;
}

.search-button {
    background: none;
    border: none;
    cursor: pointer;
}

.custom-navbar-brand {
    font-size: 20px; 
    color: white;
}

.navbar-nav-wrapper {
    position: absolute;
    top: 50%;
    right: 5%;
    transform: translateY(-50%);
}

.custom-nav-link {
    font-size: 15px;
    color: white;
}

</style>