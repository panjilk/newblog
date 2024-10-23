package com.example.blogjava.controller;

import com.example.blogjava.common.AuthAccess;
import com.example.blogjava.common.Result;
import com.example.blogjava.dao.Post;
import com.example.blogjava.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    PostService postService;
    /**
     * 新增信息
     */
    @PostMapping("/add")
    public Result add(@RequestBody Post posts) {
        if(postService.findByTitle(posts.getTitle())!=null){
            return Result.error("文章已存在");
        }
        else{
            if(postService.save(posts)){
                return Result.success();
            }
            return Result.error("文章保存失败");
        }

    }
    /**
     * 修改信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody Post posts) {
        if(postService.update(posts)){
            return Result.success("更新成功");
        }
        else{
            return Result.error("更新失败");
        }
    }

    /**
     * 删除信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        if(postService.delete(id)){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }


    /**
     * 批量删除信息
     */
    @DeleteMapping("/deletes")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        postService.deletes(ids);
        return Result.success();
    }

    /**
     * 查询全部信息
     */
    @GetMapping("/findAll")
    public Result selectAll() {
        List<Post> userList = postService.findAll();
        return Result.success(userList);
    }

    /**
     * 根据ID查询信息
     */
    @GetMapping("/findById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Post posts = postService.findById(id);
        if(posts!=null){
            return Result.success(posts);
        }
        return Result.error("未查询到相关信息");
    }


    /**
     * 多条件模糊查询信息
     * pageNum 当前的页码
     * pageSize 每页查询的个数
     */
    @AuthAccess
    @GetMapping("/pages")
    public Page findAll(@RequestParam String page, @RequestParam(required = false,defaultValue = " ") String poststitle,@RequestParam(required = false,defaultValue = " ") String id){
        return postService.findAll(page,poststitle,id);
    }


}
