<template>
  <div class="background">
    <div class="title">
      <router-link class="navigation" to="/firstpage">首页</router-link>
      <router-link  class="navigation" style="width: 100px" to="/">排行榜</router-link>
      <router-link class="navigation" to="/">热点</router-link>
      <router-link class="navigation" to="/management">管理页面</router-link>
      <router-link class='login-btn' to="/login">登录</router-link>
    </div>
    <router-view />

  </div>
</template>
<style scoped>


</style>

<script setup>
import "../assets/main1.css"
</script>
