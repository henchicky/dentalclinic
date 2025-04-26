<template>
  <div class="login-container">
    <div class="login-box">
      <h2>Login</h2>
      <el-form @submit.prevent="handleLogin" :model="{ username, password }" label-position="top">
        <el-form-item label="Username" prop="username">
          <el-input
            id="username"
            v-model="username"
            placeholder="Enter your username"
            prefix-icon="User"
            autocomplete="username"
            clearable
          />
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input
            id="password"
            v-model="password"
            type="password"
            placeholder="Enter your password"
            prefix-icon="Lock"
            autocomplete="current-password"
            show-password
            clearable
          />
        </el-form-item>
        <div v-if="error" class="error-message">
          {{ error }}
        </div>
        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading" style="width: 100%">
            {{ loading ? 'Logging in...' : 'Login' }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const authStore = useAuthStore()
const router = useRouter()

const username = ref('')
const password = ref('')
const loading = ref(false)
const error = ref('')

const handleLogin = async () => {
  try {
    loading.value = true
    error.value = ''
    await authStore.login(username.value, password.value)
    router.push({ name: 'schedule' })
  } catch {
    error.value = 'Invalid username or password'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 91vh;
  background-color: transparent;
}

.login-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #333;
}

.error-message {
  color: #dc3545;
  margin-bottom: 1rem;
  text-align: center;
}
</style>
