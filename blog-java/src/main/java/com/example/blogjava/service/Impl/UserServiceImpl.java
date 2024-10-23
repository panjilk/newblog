package com.example.blogjava.service.Impl;

import com.example.blogjava.dao.Repository.UserRepository;
import com.example.blogjava.dao.User;
import com.example.blogjava.exception.serviceException;
import com.example.blogjava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,password);
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User register(User user) {
        if(userRepository.findByUsername(user.getUsername())!=null){
            throw new serviceException("注册账号被占用");
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public User Login(String username, String password) {
        if(userRepository.findByUsernameAndPassword(username,password) != null){
            return userRepository.findByUsernameAndPassword(username,password);
        }
        else{
            throw new serviceException("用户名或密码错误");
        }
    }

    @Override
    public boolean update(User user) {
        if(userRepository.findById(user.getId()).isPresent()){
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        if(userRepository.findById(id)!=null){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deletes(List<Integer> ids) {
        for(int id:ids){
            if(userRepository.findById(id)== null){
                return false;
            }
            userRepository.deleteById(id);
        }
        return true;
    }

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(String page,String name,String id) {
        int limit = 8;
        if(Integer.parseInt(page)<=0){
            throw new serviceException("404","不能小于1页");
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1, limit);
        Page<User> pages;
        if(!Objects.equals(name, " ") && Objects.equals(id, " ")){
            pages = userRepository.findByUsername(name, pageable);
        }
        else if(Objects.equals(name, " ") && !Objects.equals(id, " ")){
            pages = userRepository.findById(Integer.parseInt(id), pageable);
        }
        else if(!Objects.equals(name, " ") && !Objects.equals(id, " ")){
            throw new serviceException("多余参数");
        }
        else{
            pages = userRepository.findAll(pageable);
        }
        return pages;

    }


}
