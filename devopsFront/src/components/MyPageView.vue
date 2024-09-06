<template>
    <div class="my-page-container">
        <h2>MyPage</h2>

        <!-- 개인 정보 수정 섹션 -->
        <section class="profile-info-section">
            <h3>개인 정보</h3>
            <div v-if="!editProfileMode">
                <p><strong>이름:</strong> {{ form.name }}</p>
                <p><strong>이메일:</strong> {{ form.email }}</p>
                <p><strong>전화번호:</strong> {{ form.phone }}</p>
                <p><strong>생년월일:</strong> {{ form.birth }}</p>
                <p><strong>성별:</strong> {{ form.gender === 'M' ? '남성' : '여성' }}</p>
                <button class="edit-button" @click="editProfileMode = true">수정</button>
            </div>

            <form v-else @submit.prevent="updateProfile">
                <div class="form-group">
                    <label for="name">이름</label>
                    <input type="text" v-model="form.name" id="name" required />
                </div>

                <div class="form-group">
                    <label for="email">이메일 주소</label>
                    <input type="email" v-model="form.email" id="email" required />
                </div>

                <div class="form-group">
                    <label for="phone">휴대전화번호</label>
                    <input type="tel" v-model="form.phone" id="phone" required />
                </div>

                <div class="form-group">
                    <label for="birth">생년월일</label>
                    <input type="date" v-model="form.birth" id="birth" required />
                </div>

                <div class="form-group">
                    <label>성별</label>
                    <div>
                        <label>
                            <input type="radio" v-model="form.gender" value="M" required />
                            남성
                        </label>
                        <label>
                            <input type="radio" v-model="form.gender" value="F" required />
                            여성
                        </label>
                    </div>
                </div>

                <button type="submit" class="save-button">정보 수정</button>
                <button type="button" class="cancel-button" @click="editProfileMode = false">취소</button>
            </form>
        </section>

        <!-- 비밀번호 수정 섹션 -->
        <section class="password-change-section">
            <h3>비밀번호 변경</h3>
            <form @submit.prevent="changePassword">
                <div class="form-group">
                    <label for="current-password">현재 비밀번호</label>
                    <input type="password" v-model="passwordForm.currentPassword" id="current-password" required />
                </div>

                <div class="form-group">
                    <label for="new-password">새 비밀번호</label>
                    <input type="password" v-model="passwordForm.newPassword" id="new-password" required />
                </div>

                <div class="form-group">
                    <label for="confirm-password">새 비밀번호 확인</label>
                    <input type="password" v-model="passwordForm.confirmPassword" id="confirm-password" required />
                </div>

                <button type="submit" class="save-button">비밀번호 변경</button>
            </form>
        </section>

        <!-- 관심사 수정 섹션 -->
        <section class="interest-edit-section">
            <h3>관심사</h3>
            <div class="interest-buttons">
                <button 
                    v-for="category in sortedCategories" 
                    :key="category.id" 
                    @click="toggleInterest(category.id)" 
                    :class="{ 'selected': form.interest.includes(category.id) }">
                    {{ category.name }}
                </button>
            </div>
        </section>
    </div>
</template>

<script>
import axios from 'axios';
import router from '@/router';
import Swal from 'sweetalert2';

export default {
    name: 'MyPage',
    data() {
        return {
            editProfileMode: false,
            form: {
                id: '',
                name: '',
                email: '',
                phone: '',
                birth: '',
                gender: '',
                interest: [], // 관심사 배열, category_id로 저장
            },
            passwordForm: {
                currentPassword: '',
                newPassword: '',
                confirmPassword: '',
            },
            categories: [
                { id: 1, name: '클라우드' },
                { id: 2, name: '인공지능' },
                { id: 3, name: '데이터/애널리틱스' },
                { id: 4, name: '블록체인' },
                { id: 5, name: '보안' },
                { id: 6, name: 'IT테크놀로지' },
            ],
        };
    },
    computed: {
        sortedCategories() {
            // category_id 순서로 정렬된 카테고리 반환
            return this.categories.sort((a, b) => a.id - b.id);
        }
    },
    methods: {
        async fetchUserInfo(userNo) {
            try {
                const response = await axios.get(`http://localhost:8080/api/myinfo/${userNo}`);
                this.form = response.data; // 응답받은 데이터를 form에 저장
                this.form.id = userNo; // 숫자형 userNo를 저장
                this.form.interest = this.form.interest.map(Number); // 관심사를 숫자형으로 변환
            } catch (error) {
                console.error('사용자 정보를 불러오는 중 오류가 발생했습니다:', error);
                alert('사용자 정보를 로드하는 데 실패했습니다. 다시 로그인해 주세요.');
                router.push('/login');
            }
        },
        async updateProfile() {
            try {
                const userNo = parseInt(this.form.id, 10); // form.id를 숫자형으로 변환
                if (!isNaN(userNo)) {
                    await axios.put(`http://localhost:8080/api/myinfo/updateUserInfo/${userNo}`, {
                        name: this.form.name,
                        email: this.form.email,
                        phone: this.form.phone,
                        birth: this.form.birth,
                        gender: this.form.gender
                    });
                    Swal.fire('개인 정보가 성공적으로 수정되었습니다.', '', 'success');
                    this.editProfileMode = false;
                } else {
                    console.error('유효하지 않은 userNo:', this.form.id);
                }
            } catch (error) {
                console.error('개인 정보 수정 중 오류가 발생했습니다:', error.response.data);
                Swal.fire('정보 수정에 실패했습니다. 다시 시도해 주세요.', '', 'warning');
            }
        },
        async toggleInterest(categoryId) {
            const index = this.form.interest.indexOf(categoryId);
            if (index === -1) {
                this.form.interest.push(categoryId);
            } else {
                this.form.interest.splice(index, 1);
            }

            try {
                const userNo = parseInt(this.form.id, 10); // form.id를 숫자형으로 변환
                if (!isNaN(userNo)) {
                    await axios.put(`http://localhost:8080/api/myinfo/updateInterest/${userNo}`, this.form.interest);
                    // 알람을 표시하지 않고 바로 저장합니다.
                } else {
                    console.error('유효하지 않은 userNo:', this.form.id);
                }
            } catch (error) {
                console.error('관심사 수정 중 오류가 발생했습니다:', error.response.data);
            }
        },
        async changePassword() {
            if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
                Swal.fire('새 비밀번호와 비밀번호 확인이 일치하지 않습니다.', '', 'warning');
                return;
            }

            try {
                const userNo = parseInt(this.form.id, 10); // form.id를 숫자형으로 변환
                if (!isNaN(userNo)) {
                    await axios.put(`http://localhost:8080/api/myinfo/updatePwd/${userNo}`, null, {
                        params: {
                            currentPassword: this.passwordForm.currentPassword,
                            newPassword: this.passwordForm.newPassword,
                        }
                    });
                    Swal.fire('비밀번호가 성공적으로 변경되었습니다.', '', 'success');
                } else {
                    console.error('유효하지 않은 userNo:', this.form.id);
                }
            } catch (error) {
                console.error('비밀번호 변경 중 오류가 발생했습니다:', error.response.data);
                Swal.fire('비밀번호 변경에 실패했습니다. 다시 시도해 주세요.', '', 'warning');
            }
        },
    },
    created() {
        const storedUserNo = localStorage.getItem('userNo');
        if (storedUserNo) {
            const userNo = parseInt(storedUserNo, 10); // userNo를 숫자형으로 변환
            if (!isNaN(userNo)) {
                this.fetchUserInfo(userNo);
            } else {
                console.error('유효하지 않은 userNo:', storedUserNo);
                alert('로그인이 필요합니다.');
                router.push('/login');
            }
        } else {
            console.error('사용자 정보가 로드되지 않았습니다.');
            alert('로그인이 필요합니다.');
            router.push('/login');
        }
    },
};
</script>

<style scoped>
.my-page-container {
    max-width: 800px;
    margin: 100px auto;
    padding: 20px;
    font-size: 18px; /* 기본 폰트 사이즈 */
}

h2 {
    text-align: center;
    font-size: 24px;
}

.profile-info-section,
.password-change-section,
.interest-edit-section {
    margin-top: 30px;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 10px;
    background-color: #f9f9f9; /* 배경색 추가 */
}

.profile-info-section h3,
.password-change-section h3,
.interest-edit-section h3 {
    font-size: 20px;
    margin-bottom: 20px;
}

.form-group {
    margin-bottom: 15px;
    font-size: 18px;
}

label {
    display: block;
    margin-bottom: 5px;
    font-size: 16px;
}

input[type='text'],
input[type='email'],
input[type='tel'],
input[type='date'],
input[type='password'] {
    width: 100%;
    padding: 10px;
    box-sizing: border-box;
    font-size: 16px;
}

button {
    width: 100%;
    padding: 12px;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin-top: 10px;
    font-size: 18px;
}

.save-button {
    background-color: #93bfcf;
}

.cancel-button {
    background-color: #f08080;
}

.edit-button {
    background-color: #93bfcf;
}

.interest-buttons {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
}

.interest-buttons button {
    flex: 1 1 30%;
    margin: 5px;
    background-color: #93bfcf;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    padding: 12px;
    font-size: 16px;
}

.interest-buttons button.selected {
    background-color: #6096B4;
}
</style>
