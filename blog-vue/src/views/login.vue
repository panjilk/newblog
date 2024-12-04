<template>
  <div id="title">
    <div id="login">
      <h1 style="margin-left: 0; margin-top: 20px;">登录</h1>
      <el-form ref="ruleFormRef" style="max-width: 600px" :model="ruleForm" status-icon :rules="rules"
               label-width="auto" class="demo-ruleForm">
        <el-form-item  label="账号" prop="username" style="margin-left: 10px; margin-top: 50px;">
          <el-input  prefix-icon="User" style="width: 350px;" v-model="ruleForm.username" autocomplete="off" />

        </el-form-item>
        <!--  -->
        <el-form-item   label="密码" prop="password" style="margin-left: 10px; margin-top: 20px;">
          <el-input show-password prefix-icon="Lock" style="width: 350px;" v-model="ruleForm.password" type="password" autocomplete="off" />
          <!--          <el-icon style="margin-left: -70px" @click ="trans"><View /></el-icon>-->
        </el-form-item>
        <!-- 验证码布局 -->
        <div style="display: flex; align-items: center; justify-content: center;">
          <el-form-item  label="验证码" prop="valid" style="margin-left: 10px; margin-top: 20px; width: 200px; flex: 1;">
            <el-input prefix-icon="Aim" style="width: 350px;" v-model="ruleForm.code"  autocomplete="off" />
          </el-form-item>

          <div style="flex:1; margin-left: 30px; border: 0.5px solid black; height: 40px; width: 20%;">
            <valid-code @update:value="getCode"></valid-code>
          </div>
        </div>
        <!-- 编辑框重置按钮 -->
        <el-button style="margin-left: 70%; margin-top: -20px;" @click="resetForm(ruleFormRef)">Reset</el-button>
        <!-- 登陆注册表单 -->
        <el-form-item>
          <el-button size="large" type="primary" @click="submitForm(ruleFormRef)" style="margin-left: 90px; margin-top: 0px;">
            登录
          </el-button>
          <el-button size="large" type="primary" @click="router.push('/register')" style="margin-left: 90px; margin-top: 0px;">
            注册
          </el-button>
          <div style="display: flex; width: 100%;margin-top: 15px;">
            <div style="margin-left: 30%;">想不起来密码?</div>
            <span @click="dialogFormVisible = true" style="color: blue; cursor: pointer; text-align: right;">忘记密码</span>
          </div>

        </el-form-item>
      </el-form>
    </div>
  </div>
  <el-dialog v-model="dialogFormVisible" title="找回密码" width="500">
    <el-form :model="forget">
      <el-form-item label="账号"  >
        <el-input v-model="forget.username" autocomplete="off" />
      </el-form-item>
      <el-form-item label="邮箱" >
        <el-input v-model="forget.email" autocomplete="off" />
      </el-form-item>
      <el-form-item label="新密码">
        <el-input v-model="forget.password" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="forgetPwd(); dialogFormVisible = false">
          修改
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import router from '../router/index';
import axios from 'axios';
import ValidCode from '../components/ValidCode.vue'
const ruleFormRef = ref<FormInstance>()
let show = ref(false) // ------------是否显示编辑框密码
let dialogFormVisible = ref(false)
let forget = reactive({ //-----------忘记密码
  username:"",
  email:"",
  password:""
})
function trans(){
  show.value = !show.value
}
function getCode(code:any) {
  //console.log(code)
  ruleForm.Valid = code.toLowerCase()
}
function forgetPwd(){
  axios.put("http://localhost:8080/forget",{
    username:forget.username,
    email:forget.email,
    password:forget.password
  }).then(res=>{
    //console.log(res.data)
    if(res.data.code === "102"){
      ElMessage.success("修改成功")
    }
    else{
      console.log(res.data.msg)
      ElMessage.error("修改失败")
    }
  }).catch(e=>{
    console.log("网络连接失败")
    ElMessage.error("网络连接失败")
  })
}
const validateusername = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入账号'))
  } else {
    if (ruleForm.username !== '') {
      if (!ruleFormRef.value) return
      ruleFormRef.value.validateField('checkPass')
    }
    callback()
  }
}
const validatePass2 = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('密码不能为空'))
  } else {
    callback()
  }
}
const validatevalid = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('验证码不能为空'))
  }
  else if(ruleForm.code.toLowerCase() !== ruleForm.Valid){
    ElMessage.error('验证码错误')
    callback(new Error('验证码错误'))
  }
  else {
    callback()
  }
}

const ruleForm = reactive({
  username:"",
  password: '',
  code:'',
  Valid:''
})

const rules = reactive<FormRules<typeof ruleForm>>({
  username: [{ validator: validateusername, trigger: 'blur' }],
  password: [{ validator: validatePass2, trigger: 'blur' }],
  code: [{ validator: validatevalid, trigger: 'blur' }]
})

const submitForm = (formEl: FormInstance | undefined) => {
  console.log("123验证码：", (ruleForm.code))
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid ) {
      console.log(ruleForm.username)
      console.log(ruleForm.password)
      axios.post('http://localhost:8080/login',{
        username:ruleForm.username,
        password:ruleForm.password
      }).then((res)=>{
        console.log(res.data)
        if(res.data.code == 102){
          router.push('/management')
          ElMessage.success("登陆成功")
          console.log(res.data.data)
          localStorage.setItem("honey-user",res.data.data.token)
          localStorage.setItem("role",res.data.data.role)
          localStorage.setItem("id",res.data.data.id)
          localStorage.setItem("username",res.data.data.username)
        }
        else{
          ElMessage.error("账号或密码错误")
        }
      }).catch((err)=>{
        console.log(err)
      })
    } else {
      console.log('error submit!')
      ElMessage.error("验证错误")
    }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
</script>

<style scoped>
*{

  box-sizing: border-box;
}

:deep(.el-form-item__label){
  font-size: 1.1em;
  color: #000000 !important;
}
/* Your styles go here */
#title {
  height: 100vh;
  width: 100vw;
  padding: 0;
  margin: 0;
  background-image: url("@/assets/pic/loginbk.jpg");
  background-repeat: no-repeat;
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
}

#login {
  background-color: transparent;
  backdrop-filter: blur(20px);
  height: 450px;
  width: 450px;
  border-radius: 20px;
  position: absolute;
  /* 可以保留 */
  box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.3);
  /* 添加阴影 */
}
</style>
