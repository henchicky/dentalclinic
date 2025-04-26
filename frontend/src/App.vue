<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <el-container>
    <el-header class="custom-header">
      <div class="header-content">
        <router-link to="/" class="logo-link">
          <img src="@/assets/dental.png" alt="Smiling Dental Clinic Logo" class="logo-img" />
          <span class="clinic-name">Smiling Dental Clinic</span>
        </router-link>
        <el-menu mode="horizontal" :router="true" class="custom-menu">
          <el-menu-item index="/appointment">Appointment</el-menu-item>
          <el-menu-item index="/schedule">Schedule</el-menu-item>
        </el-menu>
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
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  border-bottom: 1px solid #eaeaea;
  padding: 0;
  height: 70px;
  display: flex;
  align-items: center;
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

.custom-menu {
  flex: 1;
  justify-content: flex-end;
  background: transparent;
  border-bottom: none;
}

.el-menu--horizontal > .el-menu-item {
  font-size: 1rem;
  padding: 0 24px;
  color: #333;
  transition: color 0.2s;
}

.el-menu--horizontal > .el-menu-item:hover,
.el-menu--horizontal > .el-menu-item.is-active {
  color: #4a90e2;
  background: #f5f7fa;
}
</style>
