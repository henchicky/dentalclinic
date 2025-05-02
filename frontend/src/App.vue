<script setup lang="ts">
import { RouterLink, RouterView, useRouter } from 'vue-router'
import { useAuthStore } from './stores/auth'
import { computed } from 'vue'
import router from './router'

const authStore = useAuthStore()
const isAuthenticated = computed(() => authStore.isAuthenticated)
const username = computed(() => authStore.user)

function logout() {
  authStore.logout()
  router.push('/')
}
</script>

<template>
  <el-container>
    <el-header class="custom-header">
      <div class="header-content">
        <router-link to="/" class="logo-link">
          <img src="@/assets/dental.png" alt="Smiling Dental" class="logo-img" />
          <span class="clinic-name">Smiling Dental</span>
        </router-link>
        <nav>
          <el-dropdown v-if="isAuthenticated" class="user-dropdown">
            <span class="user-link">
              <el-icon>
                <User />
              </el-icon>
              {{ username }}
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="logout">Logout</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </nav>
      </div>
    </el-header>

    <el-main>
      <router-view></router-view>
    </el-main>
  </el-container>
</template>

<style>
.el-header {
  padding: 0;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12);
  position: sticky;
  top: 0;
  z-index: 100;
}

.el-main {
  min-height: calc(100vh - 120px);
  padding: 0;
}

.logo-link {
  display: inline-block;
  vertical-align: middle;
  margin-right: 1rem;
}

.logo-img {
  height: 40px;
  width: auto;
  vertical-align: middle;
}

.custom-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  border-bottom: 1px solid #eaeaea;
  padding: 0;
  height: 70px;
  display: flex;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  height: 100%;
  padding: 0 2rem;
}

.logo-link {
  display: flex;
  align-items: center;
  text-decoration: none;
}

.logo-img {
  height: 48px;
  width: auto;
  margin-right: 12px;
}

.clinic-name {
  font-size: 1.3rem;
  font-weight: 700;
  color: #357abd;
  letter-spacing: 1px;
}

.active-link::after {
  color: #4a90e2;
}

.user-dropdown {
  margin-left: 2rem;
  cursor: pointer;
}

.user-link {
  color: #000000;
  font-weight: 700;
  font-size: 1rem;
  padding: 0 8px;
  transition: color 0.2s;
  outline: none !important;
  box-shadow: none !important;
}

.user-link:focus,
.user-link:active {
  outline: none !important;
  box-shadow: none !important;
}
</style>
