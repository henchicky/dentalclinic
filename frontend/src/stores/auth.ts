import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  // Initialize from localStorage
  const isAuthenticated = ref(localStorage.getItem('isAuthenticated') === 'true')
  const user = ref<string | null>(localStorage.getItem('user'))

  function login(username: string, password: string) {
    // TODO: Replace with actual API call
    return new Promise((resolve, reject) => {
      // Simulate API call
      if (username === 'admin' && password === 'admin') {
        isAuthenticated.value = true
        user.value = username
        // Persist to localStorage
        localStorage.setItem('isAuthenticated', 'true')
        localStorage.setItem('user', username)
        resolve(true)
      } else {
        reject(new Error('Invalid credentials'))
      }
    })
  }

  function logout() {
    isAuthenticated.value = false
    user.value = null
    // Remove from localStorage
    localStorage.removeItem('isAuthenticated')
    localStorage.removeItem('user')
  }

  return {
    isAuthenticated,
    user,
    login,
    logout
  }
}) 