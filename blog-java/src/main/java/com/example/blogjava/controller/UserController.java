package com.example.blogjava.controller;

import cn.hutool.core.util.StrUtil;
import com.example.blogjava.Utils.TokenUtils;
import com.example.blogjava.common.AuthAccess;
import com.example.blogjava.common.Result;
import com.example.blogjava.dao.User;
import com.example.blogjava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @AuthAccess
    @GetMapping("/find")
    public Result findByid( Integer id){
        return Result.success(userService.findById(id));
    }
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        User users = new User();
        user.setUsername(user.getUsername());
        user.setPassword(user.getPassword());
        user.setRole("stuff");
        if(StrUtil.isEmpty(user.getUsername()) || StrUtil.isEmpty(user.getPassword()) ){
            return Result.error("用户名或密码不能为空");
        }
        if(userService.findByUsername(user.getUsername())!=null){
            return Result.error("注册账号被占用");
        }
        else {
            userService.register(user);
            return Result.success("注册成功");
        }
    }
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        if(user.getUsername() == null || user.getPassword() == null || user.getUsername().isEmpty()|| user.getPassword().isEmpty() ){
            System.out.println(user.getUsername()+" "+user.getPassword());
            return Result.error("用户名或密码不能为空");
        }
        User users = userService.Login(user.getUsername(), user.getPassword());
        if(users == null){
            return Result.error("用户名或密码错误");
        }
        else{
            Map<String,Object> map = new HashMap<>();
            map.put("id",users.getId());
            map.put("username",users.getUsername());
            String token = TokenUtils.genToken(map);
            //---------------------------------redis键值对存id : token
            System.out.println(token);
            redisTemplate.opsForValue().set( token, token,12, TimeUnit.HOURS);
            System.out.println("token记录成功");
            return Result.success(users);
        }
    }
    @PutMapping("/update")
    public Result update(@RequestBody User user){

        User ori = userService.findById(user.getId());
        if(ori == null){
            return Result.error("未查询到账号，更新失败");
        }
        else{
            if(user.getPassword() != null)ori.setPassword(user.getPassword());
            if(user.getUsername() != null)ori.setUsername(user.getUsername());
            if(user.getRole() != null)ori.setRole(user.getRole());
            if(user.getAvatar() != null)ori.setAvatar(user.getAvatar());
            if(user.getEmail() != null)ori.setEmail(user.getEmail());
            if(userService.update(ori)){
                return Result.success("更新成功");
            }
            else return Result.error("已查询到账号，但是更新失败");
        }
    }
    @AuthAccess
    @PutMapping("/forget")
    public Result forget(@RequestBody User user){

        User ori = userService.findByUsername(user.getUsername());
        if(ori == null){
            return Result.error("未查询到账号，更新失败");
        }
        else{
            if(user.getPassword() != null)ori.setPassword(user.getPassword());
            if(user.getUsername() != null)ori.setUsername(user.getUsername());
            if(user.getRole() != null)ori.setRole(user.getRole());
            if(user.getAvatar() != null)ori.setAvatar(user.getAvatar());
            if(userService.update(ori)){
                return Result.success("更新成功");
            }
            else return Result.error("已查询到账号，但是更新失败");
        }
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        if(userService.delete(id)){
            return Result.success("删除成功");
        }
        else return Result.error("未查询到账号，删除失败");
    }
    @DeleteMapping("/deletes")
    public Result deletes(@RequestBody List<Integer> ids){
        if(userService.deletes(ids)){
            return Result.success("删除成功");
        }
        else return Result.error("未查询到某些账号，删除失败");
    }
    @GetMapping("/findAll")
    public Result findAll( ){
        return Result.success(userService.findAll());
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader("honey-user")String token){
        redisTemplate.delete(token);
        return Result.success("success");
    }
    @AuthAccess
    @GetMapping("/pages")
    public Page findAll(@RequestParam String page, @RequestParam(required = false,defaultValue = " ") String username,@RequestParam(required = false,defaultValue = " ") String id){
        return userService.findAll(page,username,id);
    }
    @GetMapping("/123")
    public String get(){
        return "123";
    }
}
