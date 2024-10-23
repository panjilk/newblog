package com.example.blogjava.service.Impl;

import com.example.blogjava.dao.Post;
import com.example.blogjava.dao.Repository.PostRepository;
import com.example.blogjava.exception.serviceException;
import com.example.blogjava.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service

public class PostserviceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public Post findByTitle(String title) {
        return postRepository.findByTitle(title);
    }


    @Override
    public Post findById(int id) {
        return postRepository.findById(id);
    }


    @Override
    public boolean update(Post post) {
        Post ori = postRepository.findById(post.getId()).orElse(null);
        if(ori!=null){
            LocalDateTime Time = LocalDateTime.now();
            // 定义自定义格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDateTime = Time.format(formatter);

            post.setUpdated_time(formattedDateTime);
            post.setCreated_time(ori.getCreated_time());
            post.setUserid(ori.getUserid());

            postRepository.save(post);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        if(postRepository.findById(id)!=null){
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deletes(List<Integer> ids) {
        for(int id:ids){
            if(postRepository.findById(id)== null){
                return false;
            }
            postRepository.deleteById(id);
        }
        return true;
    }

    @Override
    public List<Post> findAll() {

        return postRepository.findAll();
    }

    @Override
    public Page<Post> findAll(String page, String name, String id) {
        int limit = 8;
        if(Integer.parseInt(page)<=0){
            throw new serviceException("404","不能小于1页");
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1, limit);
        Page<Post> pages;
        if(!Objects.equals(name, " ") && Objects.equals(id, " ")){
            pages = postRepository.findByTitle(name, pageable);
        }
        else if(Objects.equals(name, " ") && !Objects.equals(id, " ")){
            pages = postRepository.findById(Integer.parseInt(id), pageable);
        }
        else if(!Objects.equals(name, " ") && !Objects.equals(id, " ")){
            throw new serviceException("多余参数");
        }
        else{
            pages = postRepository.findAll(pageable);
        }
        return pages;

    }

    @Override
    public boolean save(Post post) {
        LocalDateTime Time = LocalDateTime.now();
        // 定义自定义格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = Time.format(formatter);

        post.setCreated_time(formattedDateTime);
        postRepository.save(post);
        return true;
    }


}
