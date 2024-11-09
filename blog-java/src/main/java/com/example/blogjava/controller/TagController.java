package com.example.blogjava.controller;

import com.example.blogjava.common.AuthAccess;
import com.example.blogjava.common.Result;
import com.example.blogjava.dao.Tag;
import com.example.blogjava.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    TagService tagService;
    /**
     * 新增信息
     */
    @PostMapping("/add")
    public Result add(@RequestBody Tag tags) {
        if(tagService.findByName(tags.getName())!=null){
            return Result.error("标签已存在");
        }
        else{
            if(tagService.save(tags)){
                return Result.success("标签新增成功");
            }
            return Result.error("标签保存失败");
        }

    }

    /**
     * 删除信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        if(tagService.delete(id)){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }


    /**
     * 批量删除信息
     */
    @DeleteMapping("/deletes")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        tagService.deletes(ids);
        return Result.success("删除成功");
    }

    /**
     * 查询全部信息
     */
    @GetMapping("/findAll")
    public Result selectAll() {
        List<Tag> userList = tagService.findAll();
        return Result.success(userList);
    }

    /**
     * 根据ID查询信息
     */
    @GetMapping("/findById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Optional<Tag> tags = tagService.findById(id);
        if(tags.isPresent()){
            return Result.success(tags);
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
    public Page findAll(@RequestParam String page, @RequestParam(required = false,defaultValue = " ") String tagstitle,@RequestParam(required = false,defaultValue = " ") String id){
        return tagService.findAll(page,tagstitle,id);
    }


}
