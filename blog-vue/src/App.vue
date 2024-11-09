<script setup>
import { RouterLink, RouterView } from 'vue-router'
import main from './views/main.vue'
</script>

<template>
  <RouterView />
</template>

<style scoped>
* {
  margin: 0;
  padding: 0;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
}
html {
  height: 100%;
}
body {
  transition: background 0.25s ease-in-out;
  min-height: 100%;
}
#app {
  max-height: 100%;
  min-width: 100%;
  position: relative;
}
</style>
