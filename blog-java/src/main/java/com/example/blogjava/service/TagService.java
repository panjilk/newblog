package com.example.blogjava.service;

import com.example.blogjava.dao.Tag;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TagService {

    Tag findByName(String name);

    Optional<Tag> findById(int id);


    boolean delete(int id);
    boolean deletes(List<Integer> ids);
    List<Tag> findAll();
    //tag代表第几页，limit代表一页有几条数据

    Page<Tag> findAll(String tag, String name, String id);

    boolean save(Tag tag);
}
