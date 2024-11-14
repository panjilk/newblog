// stores/baseurl.ts
import { defineStore } from 'pinia';

export const useBaseUrlStore = defineStore('baseUrl', {
    state: () => ({
        URL: 'http://localhost:8080'  // 确保这个属性使用大写字母
    })
});