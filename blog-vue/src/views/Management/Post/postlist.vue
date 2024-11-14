<template>
  <div>
    用户名:<el-input v-model="poststitle" placeholder="查询文章名称" style="width: 300px"></el-input>
    <span style=" margin-left: 50px">
        id:<el-input v-model="id"  placeholder="查询文章ID" style="width: 300px;"></el-input>
    </span>
    <el-button style="margin-left: 50px" type="primary" @click="load">查询</el-button>
    <el-button type="primary" @click="reset">重置</el-button>
    <div style="margin: 10px 0">
      <el-button @click="addclick" type="warning" >新增</el-button>
      <el-button @click="delallclick" type="danger">批量删除</el-button>
    </div>
    <!--      -----------------------------------表单---------------------------------->
    <el-table
        :data="Newslist.data"
        style=" min-height: 600px; width: 100%; border: 1px solid black;border-radius: 3%"
        header-cell-class-content="custom-header-cell"
        @selection-change="handleSelectionChange"
    >

      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="id" label="ID" width="50" />
        <el-table-column  label="文章头像" >
            <template v-slot = "scope">
                <el-image :src="scope.row.avatar" style="width: 50px; height: 50px;" />
            </template >
        </el-table-column>
      <el-table-column prop="title" label="文章标题" width="180" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="time" label="修改时间" />

      <el-table-column label="预览">
        <template v-slot="scope">
          <el-button plain  size=large type="primary" @click="router.push('/newcontent/'+scope.row.id)">点击前往详情页</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button plain circle size=large type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button plain circle size=large type="danger" @click="handleDelete(scope.row)">删除</el-button>
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

  <el-dialog v-model="addvisible" :show-close="false" width="60%" @close="editclose">
    <div>
      <el-form style=" margin: 25px" >
        <el-form-item label="添加文章标题" style="font-weight: bold">
          <el-input style="width: 65%" v-model="addposts.title" />
        </el-form-item>
        <el-form-item label="添加内容" style="font-weight: bold;">
          <div style="border: 1px solid #ccc">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :defaultConfig="toolbarConfig"
                mode="default"
            />
            <Editor
                style="height: 500px; overflow-y: hidden;"
                v-model="valueHtml"
                :defaultConfig="editorConfig"
                mode="default"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
        <el-button type="primary" @click="add">保存</el-button>
        <el-button type="primary" @click="addvisible = false">取消</el-button>
      </el-form>
    </div>

  </el-dialog>
  <!--  ================================用户编辑弹窗----------------------->
  <el-dialog v-model="editvisible" :show-close="false" width="60%">
    <div>
      <el-form style=" margin: 25px" >
        <el-form-item label="标题" style="font-weight: bold">
          <el-input style="width: 65%" v-model="editposts.title" />
        </el-form-item>
        <el-form-item label="内容" style="font-weight: bold">
          <div style="border: 1px solid #ccc;width: 60%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef2"
                :defaultConfig="toolbarConfig"
                mode="default"
            />
            <Editor
                style="height: 500px; overflow-y: hidden;"
                v-model="editposts.content"
                :defaultConfig="editorConfig"
                mode="default"
                @onCreated="handleCreated2"
            />
          </div>
        </el-form-item>
        <el-form-item label="作者" style="font-weight: bold">
          <el-input style="width: 65%" v-model="editposts.author" />
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
import {onMounted, ref, reactive, nextTick, shallowRef, onBeforeUnmount} from 'vue';
import {useBaseUrlStore} from "@/stores/baseurl.ts"
import router from '@/router/index'
import '@wangeditor/editor/dist/css/style.css' // 引入 css

import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
//=================富文本编辑器===========
const editorRef = shallowRef()
const editorRef2 = shallowRef()
// 内容 HTML
let valueHtml = ref('<p>这里输入文章</p>')
let editvalueHtml = ref('<p>这里输入文章</p>')
// 模拟 ajax 异步获取内容

const toolbarConfig = {}
const toolbarConfig2 = {}
const editorConfig = { placeholder: '请输入内容...' }
const editorConfig2 = { placeholder: '请输入内容...' }


const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}
const handleCreated2 = (editor) => {
  editorRef2.value = editor // 记录 editor 实例，重要！
}
//================pinia=============
const baseurl  = useBaseUrlStore()
let url = baseurl.URL

let total = ref(100)
let pagesize= ref(7)
let currentpage = ref(1)
let Newslist = reactive({
  data: []
})
//============分页===============
let id = ref("")
let poststitle = ref("")
onMounted(() => {
  //console.log("token=", token.value)
  load()
})
function editclose(){ // 关闭编辑器，在dialog关闭时执行
  let editor = editorRef.value
  console.log(editor)
  valueHtml.value="这里输入文章"
}
function load(){
  let urlnow = url+"/posts/pages"
  axios.get(urlnow,{
    params:{
      page:currentpage.value,
      poststitle:poststitle.value,
      id:id.value
    }
  }).then((res) => {
    console.log("查找数据成功", res.data)
    total.value = Number(res.data.totalElements)
    pagesize.value = Number(res.data.totalPages)
    Newslist.data = res.data.content
    console.log(total.value)
    console.log(pagesize.value)
    //console.log(Userlist.data)
  }).catch((e) => {
    ElMessage.warning("出现了问题")
    console.log("出现了问题")
    console.log(e)
  })
}
function currentChange(val){
  currentpage.value = val;
  console.log("当前页数:",currentpage.value)
  load()
}
//-----------------载入数据------------------
let token = ref(localStorage.getItem("honey-user"))
axios.defaults.headers["Authorization"] = token.value;

//---------------------------------添加人物数据----------------------------
let addvisible = ref(false)
// Your script setup code goes here
let addposts = reactive({
  title: "",
  content:"",
  authorid:"",
  author:"",
  avatar:""
})
function addclick(){
  addvisible.value = true

}

//重置按钮
function reset(){
  title.value = ""
  id.value = ""
}
// ----------------- 文章列表编辑方法
let editvisible = ref(false)
let editposts = reactive([])
function handleEdit(row){
  const time = new Date();
  editvisible.value = true
  let form = JSON.parse(JSON.stringify(row))
  editvalueHtml.value = form.content
  editposts.id = form.id,
      editposts.title=form.title,
      editposts.author= form.author,
      editposts.avatar= form.avatar,
      editposts.content = editvalueHtml.value,
      editposts.time = time.toLocaleString()
  console.log("EDITVALUE=",editvalueHtml.value)
}
function editsave(){
  let urlnow = url+"/posts/update"
  axios.put(urlnow,{
    id:editposts.id,
    title: editposts.title,
    content: editposts.content,
    authorid: editposts.id,
    author:editposts.author,
    time:editposts.time
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

//====================添加文章==============
function add() {
  let token = ref(localStorage.getItem("honey-user"))
  let author = ref(localStorage.getItem("username"))
  axios.defaults.headers["Authorization"] = token.value;
  let urlnow = url+"/posts/add"
  let text = valueHtml.value
  axios.post(urlnow, {
    title: addposts.title,
    content: text,
    author: author.value,
    time:new Date().toLocaleString()
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
//--------------------删除文章--------------
let confirmVisible = ref(false)
let delid =ref(0)
function handleDelete(row){
  delid.value = row.id
  confirmVisible.value = true
}
function del(){
  let urlnow = url+"/posts/delete/" + delid.value
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
function delAll() {
  let urlnow = url + "/posts/deletes"
  if (ids.length === 0) {
    ElMessage.warning("请选择要删除的数据")
  } else {
    axios.delete(urlnow, {
      data: ids
    }).then((res) => {
      console.log(res.data)
      if (res.data.code === "102") {
        ElMessage.success("删除成功")
        confirmdelall.value = false
        load()
      } else {
        ElMessage.error(res.data.msg)
      }
    }).catch((e) => {
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
