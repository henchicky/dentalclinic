<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <el-container>
    <el-header>
      <el-menu mode="horizontal" :router="true">
        <el-menu-item index="/">Home</el-menu-item>
        <el-menu-item index="/appointment">Appointment</el-menu-item>
        <el-menu-item index="/schedule">Schedule</el-menu-item>
      </el-menu>
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
}
</style>
