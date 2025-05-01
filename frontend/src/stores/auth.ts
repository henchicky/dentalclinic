import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import router from '../router'
import { ElNotification } from 'element-plus'

export const useAuthStore = defineStore('auth', () => {
  // Initialize from localStorage
  const isAuthenticated = ref(localStorage.getItem('isAuthenticated') === 'true')
  const user = ref<string | null>(localStorage.getItem('user'))

  function login(username: string, password: string) {
    axios
      .post(`${import.meta.env.VITE_API_BASE_URL}/dentists/login`, { name: username, password: password })
      .then((response) => {
          isAuthenticated.value = true
          user.value = username
          // Persist to localStorage
          localStorage.setItem('isAuthenticated', 'true')
          localStorage.setItem('user', username)
          localStorage.setItem('userId', response.data)
          router.push('/schedule')
      })
      .catch((error) => {
        console.error('Login failed:', error)
        ElNotification({
          title: 'Error',
          message: 'Failed to login. Please check your credentials.',
          type: 'error',
        })
      })
  }

  function logout() {
    isAuthenticated.value = false
    user.value = null
    // Remove from localStorage
    localStorage.removeItem('isAuthenticated')
    localStorage.removeItem('user')
    localStorage.removeItem('userId')
  }

  return {
    isAuthenticated,
    user,
    login,
    logout
  }
}) 