<template>
  <div>
    <h1>博客内容</h1>
    <div v-if="loading">加载中...</div>
    <div v-if="error">{{ error }}</div>
    <div v-if="post">
      <h2>{{ post.title }}</h2>
      <p>{{ post.body }}</p>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const route = useRoute(); // 获取当前路由
    const post = ref(null); // 用于存储请求结果
    const loading = ref(true); // 加载状态
    const error = ref(null); // 错误信息

    const id = computed(() => route.params.id); // 获取路径参数

    // 调用接口
    const fetchPostById = async () => {
      loading.value = true; // 设置加载状态
      error.value = null; // 重置错误信息
      try {
        const response = await axios.get(`http://localhost:5173/posts/findById/${id.value}`);
        post.value = response.data; // 假设返回的数据结构符合要求
      } catch (err) {
        error.value = '获取内容失败'; // 处理错误
        console.error(err);
      } finally {
        loading.value = false; // 完成请求，更新加载状态
      }
    };

    // 在组件挂载时调用函数
    onMounted(() => {
      fetchPostById();
    });

    return {
      post,
      loading,
      error,
    };
  },
};
</script>

<style>
/* 这里可以添加一些样式 */
</style>
