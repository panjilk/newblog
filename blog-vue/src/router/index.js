import { createRouter, createWebHistory } from 'vue-router'
import main from '../views/main.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: () => import('../views/main.vue'),
      children: [
        {
          path: '/firstpage',
          name: 'firstpage',
          component: () => import('../views/firstpage.vue'),
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/login.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/register.vue')
    },
    {
      path: '/management',
      name: '旧版管理页面',
      component: () => import('../views/Management/managepage.vue'),
      children: [
        {
          path: 'changepwd',
          name: 'changepwd',
          component: () => import('../views/Management/Users/changepwd.vue')
        },
        {
          path: 'userlist',
          name: 'userlist',
          component: () => import('../views/Management/Users/userlist.vue')
        },
        {
          path: 'userinfo',
          name: 'userinfo',
          component: () => import('../views/Management/Users/userinfo.vue')
        },
        {
          path: 'postlist',
          name: 'postlist',
          component: () => import('../views/Management/Post/postlist.vue')
        },
      ]
    },
    {
      path: '/managetry',
      name: '新版管理页面',
      component: () => import('../views/Management/manage_try.vue')
    },

  ],
})

export default router
