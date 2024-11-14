<template>
  <div id = "body">
    <el-form style="margin-left: 80px; margin-top: 100px">
      <el-form-item label="账号">
        <el-input style="width: 200px; margin-left: 13px" v-model="user.username" disabled/>
      </el-form-item>
      <el-form-item label="原密码">
        <el-input style="width: 200px" v-model="user.password"  />
      </el-form-item>
      <el-form-item label="新密码">
        <el-input style="width: 200px" v-model="user.newpwd" />
      </el-form-item>
      <el-form-item label="确认密码">
        <el-input style="width: 200px" v-model="user.newpwd1" />
      </el-form-item>
    </el-form>
      <el-button style="margin-top: 170px; margin-left: 100px" type="primary" @click="edit">修改</el-button>
  </div>

</template>
<script setup>
import {ref,reactive} from "vue";
import {onMounted} from "vue";
import axios from "axios";
import {ElMessage} from "element-plus";
let user= reactive({
    username:"",
    password:"",
    newpwd:"",
    newpwd1:""
})
let id = ref(localStorage.getItem("id"))
let token = ref(localStorage.getItem("honey-user"))
let username = ref(localStorage.getItem("username"))
console.log("id--------:",id.value)

axios.defaults.headers["Authorization"] = token.value;
let url = "http://localhost:8080/find?id="+id.value
let oriPwd =  ref("")
console.log(url)

onMounted(()=>{
  axios.get(url).then((res) => {
    console.log("查找数据成功", res.data)
    user.username = res.data.data.username
    oriPwd.value = res.data.data.password
    //console.log(Userlist.data)
  }).catch((e) => {
    ElMessage.error("出现了问题")
    console.log("出现了问题")
    console.log(e)
  })
})

function edit(){
  if(user.password !== oriPwd.value){
    ElMessage.error("原密码不正确")
    return
  }
   if(user.newpwd !== user.newpwd1){
    ElMessage.error("两次密码不一致")
  }
   else if(user.newpwd ==="" || user.newpwd1 === ""){
     ElMessage.error("新密码不能为空")
   }
  else{
    axios.put("http://localhost:8080/update",{
      id:id.value,
      password:user.newpwd
    }).then((res)=>{
      if(res.data.code === "102"){
        console.log(res)
        console.log("修改成功")
        ElMessage.success("修改成功")
        oriPwd = user.newpwd
        console.log("密码修改为：",oriPwd)
      }
      if(res.data.code === "404"){
        ElMessage.error("修改失败",res.data.msg)
      }
    }).catch(error=>{
      ElMessage.error("后端未响应！")
      console.log(error)
    })
  }
}

</script>

<style>
#body{
  background-color: #c6c7cc;
  height: 500px;
  width: 800px;
  border-radius: 10%;
  margin-left: 120px;
  margin-top: 50px;
  display: flex;
}
</style>