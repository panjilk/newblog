<template>
  <div class="container">
    <el-container>
      <el-header class="header">
        <h1>管理页面</h1>
      </el-header>
      <el-container>
<!--                               侧边栏-->
        <el-aside  class="side">
          <el-row class="tac">
            <el-col :span="30">
              <h5 class="mb-2">Default colors</h5>
              <el-menu
                default-active="2"
                class="el-menu-vertical-demo"
                @open="handleOpen"
                @close="handleClose"
              >
                <el-sub-menu index="1">
                  <template #title>
                    <el-icon><location /></el-icon>
                    <span>Navigator One</span>
                  </template>
                  <el-menu-item-group title="Group One">
                    <el-menu-item index="1-1">item one</el-menu-item>
                    <el-menu-item index="1-2">item two</el-menu-item>
                  </el-menu-item-group>
                </el-sub-menu>
                <el-menu-item index="2">
                  <el-icon><icon-menu /></el-icon>
                  <span>Navigator Two</span>
                </el-menu-item>
                <el-menu-item index="4">
                  <el-icon><setting /></el-icon>
                  <span>Navigator Four</span>
                </el-menu-item>
              </el-menu>
            </el-col>
          </el-row>
        </el-aside>
        <el-main class="main">
          Main
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<style scoped>
.container{
  height: 100vh;
  width: 100vw;
  padding: 0;
  margin: 0;

}
.header{
  text-align: center;
  width: 100vw;
  height: 80px;
}

.side{
  position: relative;
  height: 800px;
  width: 200px;
  border-right: 1px solid black;
}
.tac{
  width: 200px !important;
}

</style>
<script setup>
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
} from '@element-plus/icons-vue'
const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
  console.log(key, keyPath)
}
</script>
