import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const isAuthenticated = ref(false)
  const user = ref<string | null>(null)

  function login(username: string, password: string) {
    // TODO: Replace with actual API call
    return new Promise((resolve, reject) => {
      // Simulate API call
      if (username === 'admin' && password === 'admin') {
        isAuthenticated.value = true
        user.value = username
        resolve(true)
      } else {
        reject(new Error('Invalid credentials'))
      }
    })
  }

  function logout() {
    isAuthenticated.value = false
    user.value = null
  }

  return {
    isAuthenticated,
    user,
    login,
    logout
  }
}) 