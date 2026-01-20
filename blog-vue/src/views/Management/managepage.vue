<template>
  <div class="common-layout">
    <el-container>
      <el-header class="title"
        style="">
        <el-button v-model="flag" @click="toggleMenu" type="primary" :icon="Menu" />
        <h1 style="color: #000000; font-size: 30px">管理页面</h1>
        <el-dropdown>
          <el-icon color="black" size=25px>
            <Avatar />
          </el-icon>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/management/userinfo')">个人中心</el-dropdown-item>
              <el-dropdown-item @click="router.push('/management/changepwd')"> 修改密码</el-dropdown-item>
              <el-dropdown-item @click="router.push('/')"> 返回主页面</el-dropdown-item>
              <el-dropdown-item @click="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-container>
        <div>
          <el-aside style="min-height: 800px; max-width: 200px; ">
            <el-menu active-background-color="#A6B3B5" active-text-color="#ffd04b" background-color="#D7E3E9" class="el-menu-vertical-demo"
              :collapse="flag" default-active="0" text-color="black" @open="handleOpen" @close="handleClose"
              style="min-height: 100vh;">
              <el-button  style="width: 60%;height: 40px;background-color:green ;color: #ffffff;border-radius: 10%;
                  font-size: 1.1em;font-weight: 500;">
                  发布
              </el-button>
              <el-sub-menu index="1">
                <template #title>
                  <el-icon>
                    <User />
                  </el-icon>
                  <span>账号管理</span>
                </template>
                <el-menu-item-group>
                  <el-menu-item index="1-1" @click="router.push('/management/userlist')">账号列表</el-menu-item>
                  <el-menu-item index="1-2">角色列表</el-menu-item>
                </el-menu-item-group>
              </el-sub-menu>
              <el-menu-item index="2" @click="router.push('/management/postlist')">
                <el-icon>
                  <Notification />
                </el-icon>
                <span>管理</span>
              </el-menu-item>
              <el-menu-item index="3">
                <el-icon>
                  <document />
                </el-icon>
                <span>系统公告</span>
              </el-menu-item>
              <el-menu-item index="4">
                <el-icon>
                  <CreditCard />
                </el-icon>
                <span>订单管理</span>
              </el-menu-item>
              <el-menu-item index="5">
                <el-icon>
                  <PieChart />
                </el-icon>
                <span>数据统计</span>
              </el-menu-item>
            </el-menu>

          </el-aside>
        </div>

        <el-container>
          <el-main>
            <!-- ----------------------------------------router-view--------------------------- -->
            <router-view></router-view>
          </el-main>
          <el-footer>Footer</el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>
<script setup>
import router from "@/router";
import {Avatar, CreditCard, Document, Menu, Notification, PieChart} from "@element-plus/icons-vue";
import { ref, reactive } from 'vue'

//菜单栏判断
let flag = ref(false) // 菜单栏是否展开
function toggleMenu() {
  flag.value = !flag.value
}

function logout(){
  localStorage.removeItem("honey-user")
  localStorage.removeItem("role")
  localStorage.clear()
  router.push('/login')
}
//菜单动画处理
const handleOpen = (key, keyPath) => {
  console.log(key, keyPath);
};
const handleClose = (key, keyPath) => {
  console.log(key, keyPath);
};
</script>

<style scoped>
.el-container {
  height: 100vh;
  overflow: hidden;
}

.common-layout {
  position: fixed;
  /* 或者使用 absolute */
  top: 0;
  /* 距离顶部0像素 */
  left: 0;
  /* 距离左边0像素 */
  width: 100%;
  /* 使组件宽度全屏 */
}

.title {
  background-color: white;
    border-bottom: 1px solid black;
    align-items: center;
    justify-content: space-between;
    display: flex;
    //border-radius: 5%;
    height: 60px;
}


.el-menu-item:hover,
el-sub-menu__title:hover {
  color: aqua !important;
}

.el-sub-menutitle:hover {
  color: aqua !important;
}

.el-menu-item.is-active {
  color: white;
  background-color: rgb(8, 140, 140);
  border-radius: 10%;
  border: 1px solid black;
  margin: 4px;
}
</style>
