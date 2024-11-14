<template>
    <div>
        账号:<el-input v-model="username" placeholder="查询账号" style="width: 300px"></el-input>
      <span style=" margin-left: 50px">
        id:<el-input v-model="id"  placeholder="查询ID" style="width: 300px;"></el-input>
      </span>
        <el-button style="margin-left: 50px" type="primary" @click="load">查询</el-button>
      <el-button type="primary" @click="reset">重置</el-button>
      <div style="margin: 10px 0">
        <el-button @click="addclick" type="warning" >新增</el-button>
        <el-button @click="delallclick" type="danger">批量删除</el-button>
      </div>
<!--      -----------------------------------表单---------------------------------->
        <el-table
            :data="Userlist.data"
            style=" min-height: 600px; width: 100%; border: 1px solid black;border-radius: 3%"
            header-cell-class-name="custom-header-cell"
            @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
            <el-table-column prop="id" label="ID" width="50" />
            <el-table-column prop="username" label="账号" width="180" />
            <el-table-column prop="email" label="邮箱" />
          <el-table-column prop="role" label="权限" />
            <el-table-column  label="用户头像" >
                <template v-slot = "scope">
                    <el-image :src="scope.row.avatar" style="width: 50px; height: 50px;" />
                </template >
            </el-table-column>
            <el-table-column label="操作">
                <template v-slot="scope">
                    <el-button size=default type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button size=default type="danger" @click="handleDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

    </div>
  <el-pagination
      page-size="7"
      layout="total, prev, pager, next,jumper"
      :page-count="pagesize"
      size="large"
      v-model:current-page="currentpage"
      @current-change="currentChange"
      :total="total"
  />
<!--  page-size == 一页有几个数据
      pager-count == 设置最大页码按钮数。 页码按钮的数量，当总页数超过该值时会折叠
      total: 总数据数量-->

  <el-dialog v-model="addvisible" :show-close="false" width="500px">
    <div>
      <el-form style=" margin: 25px" >
        <el-form-item label="账号" style="font-weight: bold">
          <el-input style="width: 65%" v-model="adduser.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input style="width: 65%" v-model="adduser.password"  />
        </el-form-item>
        <el-form-item label="权限">
          <el-radio-group v-model="adduser.role" style="margin-top: 10px">
            <el-radio value="stuff" size="large">普通用户</el-radio>
            <el-radio value="admin" size="large">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-button type="primary" @click="add">保存</el-button>
        <el-button type="primary" @click="addvisible = false">取消</el-button>
      </el-form>
    </div>

  </el-dialog>
<!--  ================================用户编辑弹窗----------------------->
  <el-dialog v-model="editvisible" :show-close="false" width="500px">
    <div>
      <el-form style=" margin: 25px" >
        <el-form-item label="账号" style="font-weight: bold">
          <el-input style="width: 65%" v-model="edituser.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input style="width: 65%" v-model="edituser.password"  />
        </el-form-item>
        <el-form-item label="邮箱" style="font-weight: bold">
          <el-input style="width: 65%" v-model="edituser.email" />
        </el-form-item>
        <el-form-item label="权限">
          <el-radio-group v-model="edituser.role" style="margin-top: 10px">
            <el-radio value="stuff" size="large">普通用户</el-radio>
            <el-radio value="admin" size="large">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-button type="primary" @click="editsave">保存</el-button>
        <el-button type="primary" @click="editvisible = false">取消</el-button>
      </el-form>
    </div>

  </el-dialog>

<!--  ===============二次确认删除弹窗================-->
  <el-dialog
      v-model="confirmVisible"
      title="Warning"
      width="500"
      align-center
  >
    <span>您确定要删除吗</span>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="confirmVisible = false">取消</el-button>
        <el-button type="primary" @click="del()">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>

<!--  =======================用户列表批量删除弹窗-->
  <el-dialog
      v-model="confirmdelall"
      title="Warning"
      width="500"
      align-center
  >
    <span>您确定要删除吗</span>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="confirmdelall = false">取消</el-button>
        <el-button type="primary" @click="delAll">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { onMounted, ref, reactive, } from 'vue';
import {useBaseUrlStore} from "@/stores/baseurl.ts"
//================pinia=============
const baseurl  = useBaseUrlStore()
let url = baseurl.URL
//--------------------分页------------------------
let total = ref(100)
let pagesize= ref(7)
let currentpage = ref(1)
let Userlist = reactive({
    data: []
})
onMounted(() => {
  //console.log("token=", token.value)
  load()
})
function currentChange(val){
  currentpage.value = val;
  console.log("当前页数:",currentpage.value)
  load()
}
//-----------------载入数据------------------
let token = ref(localStorage.getItem("honey-user"))
axios.defaults.headers["Authorization"] = token.value;
// ---------------------------------搜索框------------------------------
let username = ref("")
let id = ref("")
//---------------------------------添加人物数据----------------------------
let addvisible = ref(false)
// Your script setup code goes here
let adduser = reactive({
    username: "",
    password:"",
    name:"",
    role:"",
    time:"",
})
function addclick(){
  addvisible.value = true
}

//重置按钮
function reset(){
  username.value = ""
  id.value = ""
}
// ----------------- 用户列表编辑方法
let editvisible = ref(false)
let edituser = reactive([])
function handleEdit(row){
  editvisible.value = true
  let form = JSON.parse(JSON.stringify(row))
      edituser.id = form.id,
      edituser.username=form.username,
      edituser.password=form.password,
      edituser.role= form.role,
      edituser.name= form.name,
      edituser.email= form.email
  console.log(form)
}
function editsave(){
  let urlnow = url+"/update"
  axios.put(urlnow,{
    id:edituser.id,
    username: edituser.username,
    password: edituser.password,
    name: edituser.name,
    role: edituser.role,
    email: edituser.email
  }).then((res)=>{
    console.log(res.data)
    if(res.data.code === "102"){
      ElMessage.success("更新成功!")
      editvisible.value = false
      load()
    }
    else{
      ElMessage.error(res.data.msg)
    }
  })
}
//添加用户
function add() {
  let now = ref(new Date())
  now = now.toLocaleString()
  console.log("当前时间为:",now.value)
  let token = ref(localStorage.getItem("honey-user"))
  axios.defaults.headers["Authorization"] = token.value;
  let urlnow = url+"/register"
  axios.post(urlnow, {
    username: adduser.username,
    password: adduser.password,
    name: adduser.name,
    emain: adduser.emain,
    role: adduser.role,
    time:now.value
  }).then((res) => {
    console.log("当前校验码:",res.data.code)
    if (res.data.code === "102") {
      ElMessage.success(res.data.data)
      addvisible.value = false
      load()
      console.log(res.data)
    } else {
      ElMessage.warning(res.data.msg)
      console.log(res.data)
    }
  }).catch((err) => {
    console.log(err)
    ElMessage.warning("连接不到互联网")
  })

  load()
}
function load(){
  let urlnow = url+"/pages"
  axios.get(urlnow,{
    params:{
      page:currentpage.value,
      username:username.value,
      id:id.value
    }
  }).then((res) => {
    console.log("查找数据成功", res.data)
    total.value = Number(res.data.totalElements)
    pagesize.value = Number(res.data.totalPages)
    Userlist.data = res.data.content
    console.log(total.value)
    console.log(pagesize.value)
    //console.log(Userlist.data)
  }).catch((e) => {
    ElMessage.warning("出现了问题")
    console.log("出现了问题")
    console.log(e)
  })
}

//--------------------删除用户--------------
let confirmVisible = ref(false)
let delid =ref(0)
function handleDelete(row){
  delid.value = row.id
  confirmVisible.value = true
}
function del(){
  let urlnow = url+"/delete/" + delid.value
  axios.defaults.headers["Authorization"] = token.value;
    axios.delete(urlnow).then((res)=>{
      if(res.data.code === "102"){
        ElMessage.success("删除成功")
        confirmVisible.value = false
        load()
      }
      else{
        console.log(res.data.msg)
        ElMessage.error(res.data.msg)
      }
    }).catch((e)=>{
      ElMessage.error(e)
    })
}
//=================批量删除=================
let confirmdelall = ref(false)
let ids = reactive([])
function handleSelectionChange(val){
  ids = val.map(v =>v.id)
  console.log(ids)
}
function delallclick(){
  confirmdelall.value = true
}
function delAll(){
  let urlnow = url+"/deletes"
  if(ids.length === 0){
    ElMessage.warning("请选择要删除的数据")
  }
  else{
    axios.delete(urlnow,{
      data:ids
    }).then((res)=>{
      console.log(res.data)
      if(res.data.code === "102"){
        ElMessage.success("删除成功")
        confirmdelall.value = false
        load()
      }
      else{
        ElMessage.error(res.data.msg)
      }
    }).catch((e)=>{
      ElMessage.error(e)
    })
  }

}
</script>

<style>
/* Your styles go here */
.custom-header-cell {
  background-color: #b3d4fc !important;
  font-weight: bold !important;
  color:#333333 !important;
}
</style>
