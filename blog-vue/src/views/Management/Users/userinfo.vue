<template>

  <div id = "body">
    <div style="margin-left: 150px; margin-top: 150px;">
      <el-upload
          class="avatar-uploader"
          action="http://localhost:8080/file/upload"
          :headers="{'Authorization':token}"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
      >
        <img style="width: 180px;height: 180px; border-radius: 50%" v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      </el-upload>
      <el-button type="primary" style="margin-top: 20px; margin-left: 50px" @click="save">保存</el-button>
    </div>

    <div style="margin-left: 150px; margin-top: 70px">-------------个人中心----------</div>
    <el-form style="margin-left: -220px; margin-top: 150px" >
      <el-form-item label="账号" style="font-weight: bold">
        <el-input v-model="data.user.username" disabled/>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="data.user.password" disabled />
      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="data.user.name" />
      </el-form-item>
      <el-form-item label="权限">
        <el-input v-model="data.user.role" disabled/>
      </el-form-item>
    </el-form>
  </div>

</template>
<script setup>
 import {ref,reactive} from "vue";
 import {onMounted} from "vue";
 import axios from "axios";
 import {ElMessage} from "element-plus";
 import {Plus} from "@element-plus/icons-vue";
 import {useBaseUrlStore} from "@/stores/baseurl.ts";
 const baseurl = useBaseUrlStore()
 let url = baseurl.URL

 let data= reactive({
   user:[]
 })
 const id = ref(localStorage.getItem("id"))
 let token = ref(localStorage.getItem("honey-user"))
 axios.defaults.headers["Authorization"] = token.value;
let findurl = url+"/find?id="+id.value.toString()
console.log("URLLLLL:",findurl)
onMounted(()=>{
  axios.get(findurl).then((res) => {
    console.log("查找数据成功", res.data)
    data.user = res.data.data
    //console.log(Userlist.data)
  }).catch((e) => {
    ElMessage.error("出现了问题")
    console.log("出现了问题")
    console.log(e)
  })
})

function  handlerError(){
  data.user.avatar = "http://localhost:8080/file/download/origin.jpg"
}

function handleAvatarSuccess(res,file,fileList){
  console.log(res.data)
  data.user.avatar = res.data

}
function save(){
  axios.defaults.headers["Authorization"] = token.value;
  let urlnow = url +"/update"
  axios.put(urlnow,{
    id: data.user.id,
    username: data.user.username,
    password: data.user.password,
    name: data.user.name,
    role: data.user.role,
    avatar: data.user.avatar
  }).then((res)=>{
    ElMessage.success("更新成功!")
    console.log(res.data)
  }).catch((e)=>{
    ElMessage.error("出现了问题")
    console.log(e)
  })
}
</script>

<style setup>
  #body{
    background-color: #c6c7cc;
    height: 500px;
    width: 800px;
    border-radius: 10%;
    margin-left: 120px;
    margin-top: 50px;
    display: flex;
  }
  *, *::before, *::after {
    font-weight: bold;
  }
</style>