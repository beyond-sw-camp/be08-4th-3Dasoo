<template>
  <div id="content">
    <div class="login-wrapper">
      <h2>Login</h2>
      <form @submit.prevent="handleSubmit" id="login-form">
        <div class="input-group">
          <div class="input-wrapper">
            <i class="fas fa-user"></i>
            <input type="text" v-model="id" placeholder="아이디" required />
          </div>
        </div>
        <div class="input-group">
          <div class="input-wrapper">
            <i class="fas fa-lock"></i>
            <input type="password" v-model="password" placeholder="비밀번호" required />
          </div>
        </div>
        <input type="submit" value="Login" />
      </form>
    </div>
  </div>
</template>


<script setup>
import axios from 'axios'; // axios import 추가
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/store/user';
import Swal from 'sweetalert2';


// Router
const router = useRouter();

// User Store
const userStore = useUserStore();

// Form fields
const id = ref('');
const password = ref('');

// Load user from storage on component mount
onMounted(() => {
  userStore.loadUserFromStorage();
});

const handleSubmit = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/login', {
      id: id.value,
      pwd: password.value,
    });

    const userData = response.data;
    userStore.login(userData);
    // userNo를 로컬 스토리지에 저장
    localStorage.setItem('userNo', userData.userNo);

    // 메인 페이지로 이동
    router.push('/');
  } catch (error) {
    console.error('로그인 실패:', error);
    Swal.fire({
            title: '로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.',
            text: '',
            icon: 'error',
            
            confirmButtonColor: '#6096b4', // confrim 버튼 색깔 지정
            confirmButtonText: '확인', // confirm 버튼 텍스트 지정
            iconColor: 'red'            
            })
  }
};
</script>

<style scoped>
#content {
  flex: 1;
}

.login-wrapper {
  width: 400px;
  margin: 160px auto 80px;
  padding: 40px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 5px 4px 20px rgba(0, 0, 0, 0.3);
}

.login-wrapper > h2 {
  font-size: 24px;
  color: #6096b4;
  margin-bottom: 50px;
  text-align: center;
}

.input-group {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.input-wrapper {
  position: relative;
  width: 100%;
}

.input-wrapper i {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #909090;
}

.input-wrapper input {
  width: 100%;
  height: 48px;
  padding: 0 10px 0 40px;
  border-radius: 6px;
  background-color: #f8f8f8;
  border: 1px solid #ddd;
  font-size: 14px;
}

.input-wrapper input::placeholder {
  color: #d2d2d2;
}

#login-form > input[type='submit'] {
  width: 100%;
  height: 48px;
  color: #fff;
  font-size: 16px;
  background-color: #6096b4;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: 20px;
  border-radius: 6px;
}

#login-form > input[type='submit']:hover {
  background-color: #5076a5;
}
</style>
