package com.example.blogjava.service;

import com.example.blogjava.dao.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    Post findByTitle(String title);

    Post findById(int id);
    boolean update(Post notice);
    boolean delete(int id);
    boolean deletes(List<Integer> ids);
    List<Post> findAll();
    //page代表第几页，limit代表一页有几条数据

    Page<Post> findAll(String page, String name, String id);

    boolean save(Post post);


}
