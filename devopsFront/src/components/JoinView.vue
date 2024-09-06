<template>
  <div class="signup-container">
    <h2>회원가입</h2>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="id">
          <i class="fas fa-user"></i> 아이디
        </label>
        <div class="input-group">
          <div class="input-wrapper">
            <input
              type="text"
              v-model="form.id"
              id="id"
              placeholder="아이디를 입력해주세요."
              required
              :disabled="usernameChecked"
              class="input-id"
            />
          </div>
          <div class="button-wrapper">
            <button
              id="btn-idChk"
              type="button"
              @click="checkUsername"
              class="btn-idChk"
            >
              <i class="fas fa-check"></i> 중복 확인
            </button>
          </div>
        </div>
        <span
          v-if="usernameCheckMessage"
          :class="{ 'text-success': !isUsernameDuplicate, 'text-danger': isUsernameDuplicate }"
        >
          {{ usernameCheckMessage }}
        </span>
      </div>

      <div class="form-group">
        <label for="pwd">
          <i class="fas fa-lock"></i> 비밀번호
        </label>
        <input
          type="password"
          v-model="form.pwd"
          id="pwd"
          placeholder="비밀번호를 입력해주세요."
          required
        />
      </div>

      <div class="form-group">
        <label for="email">
          <i class="fas fa-envelope"></i> 이메일 주소
        </label>
        <input
          type="email"
          v-model="form.email"
          id="email"
          placeholder="4stIT@4cari.com"
          required
        />
      </div>

      <div class="form-group">
        <label for="name">
          <i class="fas fa-user-tag"></i> 이름
        </label>
        <input
          type="text"
          v-model="form.name"
          id="name"
          placeholder="이름을 입력해주세요."
          required
        />
      </div>

      <div class="form-group">
        <label for="birth">
          <i class="fas fa-calendar-alt"></i> 생년월일
        </label>
        <input
          type="date"
          v-model="form.birth"
          id="birth"
          required
        />
      </div>

      <div class="form-group">
        <label>
          <i class="fas fa-venus"></i><i class="fas fa-mars"></i> 성별
        </label>
        <div>
          <label>
            <input
              type="radio"
              v-model="form.gender"
              value="M"
              required
            />
            남성
          </label>
          <label>
            <input
              type="radio"
              v-model="form.gender"
              value="F"
              required
            />
            여성
          </label>
        </div>
      </div>

      <div class="form-group">
        <label for="phone">
          <i class="fas fa-phone"></i> 휴대전화번호
        </label>
        <input
          type="tel"
          v-model="form.phone"
          id="phone"
          placeholder="01012345678"
          required
          maxlength="11"
        />
      </div>

      <div class="form-group">
        <label for="interest">
          <i class="fas fa-tags"></i> 관심사
        </label>
        <select
          name="interest"
          v-model="form.interest"
          id="interest"
          multiple
          size="5"
          required
        >
          <option
            v-for="category in categories"
            :key="category.categoryId"
            :value="category.categoryId"
          >
            {{ category.name }}
          </option>
        </select>
      </div>

      <button
        type="submit"
        :disabled="!usernameChecked"
        class="join-button"
      >
        <i class="fas fa-user-plus"></i> 회원가입
      </button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import Swal from 'sweetalert2';

export default {
  data() {
    return {
      form: {
        id: '',
        pwd: '',
        email: '',
        name: '',
        birth: '',
        gender: '',
        phone: '',
        interest: [],
      },
      categories: [],
      isUsernameDuplicate: false, // 아이디 중복 여부
      usernameCheckMessage: '', // 중복 확인 메시지
      usernameChecked: false, // 중복 확인 완료 여부
    };
  },
  created() {
    this.fetchCategories();
  },
  methods: {
    async fetchCategories() {
      try {
        const response = await axios.get('http://localhost:8080/api/categories');
        this.categories = response.data;
      } catch (error) {
        console.error('카테고리 데이터를 불러오는 중 오류가 발생했습니다:', error);
      }
    },
    async checkUsername() {
      if (!this.form.id) {
        this.usernameCheckMessage = '아이디를 입력해주세요.';
        return;
      }
      try {
        const response = await axios.get('http://localhost:8080/api/user/check-userid', {
          params: { userId: this.form.id },
        });
        this.isUsernameDuplicate = response.data;

        if (this.isUsernameDuplicate) {
          this.usernameCheckMessage = '이미 사용 중인 아이디입니다.';
          this.usernameChecked = false;
        } else {
          this.usernameCheckMessage = '사용 가능한 아이디입니다.';
          this.usernameChecked = true;
        }
      } catch (error) {
        console.error('아이디 중복 확인 중 오류가 발생했습니다:', error);
        this.usernameCheckMessage = '오류가 발생했습니다. 다시 시도해 주세요.';
      }
    },
    async submitForm() {
      if (!this.usernameChecked) {
        alert('아이디 중복 확인을 해주세요.');
        return;
      }

      try {
        const response = await axios.post('http://localhost:8080/api/join', this.form);

        if (response.status === 201) {
          Swal.fire({
            title: '회원가입이 완료 되었습니다.',
            text: '',
            icon: 'success',
            
            confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
            confirmButtonText: '확인', // confirm 버튼 텍스트 지정
            
            }).then(result => {
            // 만약 Promise리턴을 받으면,
            if (result.isConfirmed) { // 만약 모달창에서 confirm 버튼을 눌렀다면
                this.$router.push('/login'); // 로그인 페이지로 리디렉션
            }
          });
        }
      } catch (error) {
        console.error('회원가입 중 오류가 발생했습니다:', error);
        Swal.fire({
            title: '회원가입에 실패했습니다. 다시 시도해 주세요.',
            text: '',
            icon: 'warning',
            
            confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
            confirmButtonText: '확인', // confirm 버튼 텍스트 지정            
            })
      }
    },
  },
};
</script>

<style scoped>
.signup-container {
  max-width: 800px;
  min-width: 450px;
  margin: 100px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
}

h2 {
  text-align: center;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

label i {
  margin-right: 8px;
}

.input-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.input-wrapper {
  flex: 1;
  margin-right: 5px;
}

.button-wrapper {
  flex-shrink: 0;
}

input[type='text'],
input[type='password'],
input[type='email'],
input[type='date'],
input[type='tel'] {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

button[type='submit'],
button[type='button'] {
  display: flex;
  align-items: center;
  padding: 10px;
  background-color: #93bfcf;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  justify-content: center; /* 수평 중앙 정렬 */
  line-height: 1; /* 텍스트와 아이콘의 정렬을 더 정확하게 맞추기 위한 설정 */
}

button[type='button'] i,
button[type='submit'] i {
  margin-right: 5px;
}

button.join-button {
  width: 100%;
  padding: 15px;
}

button[type='submit']:hover,
button[type='button']:hover {
  background-color: #bdcdd6;
}

.text-success {
  color: green;
}

.text-danger {
  color: red;
}
</style>
