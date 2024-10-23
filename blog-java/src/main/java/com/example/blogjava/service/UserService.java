package com.example.blogjava.service;

import com.example.blogjava.dao.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);
    User findById(int id);

    User register(User user);

    User Login(String username, String password);

    boolean update(User user);
    boolean delete(int id);
    boolean deletes(List<Integer> ids);
    List<User> findAll();
    //page代表第几页，limit代表一页有几条数据

    Page<User> findAll(String page, String name,String id);

}
