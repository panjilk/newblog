package com.example.blogjava.service.Impl;

import com.example.blogjava.dao.Tag;
import com.example.blogjava.dao.Repository.TagRepository;
import com.example.blogjava.exception.serviceException;
import com.example.blogjava.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service

public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }


    @Override
    public Optional<Tag> findById(int id) {
        return tagRepository.findById(id);
    }


    @Override
    public boolean delete(int id) {
        if(tagRepository.findById(id).isPresent()){
            tagRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deletes(List<Integer> ids) {
        for(int id:ids){
            if(tagRepository.findById(id).isEmpty()){
                return false;
            }
            tagRepository.deleteById(id);
        }
        return true;
    }

    @Override
    public List<Tag> findAll() {

        return tagRepository.findAll();
    }

    @Override
    public Page<Tag> findAll(String page, String name, String id) {
        int limit = 8;
        if(Integer.parseInt(page)<=0){
            throw new serviceException("404","不能小于1页");
        }
        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1, limit);
        Page<Tag> pages;
        if(!Objects.equals(name, " ") && Objects.equals(id, " ")){
            pages = tagRepository.findByName(name,pageable);
        }
        else if(Objects.equals(name, " ") && !Objects.equals(id, " ")){
            pages = tagRepository.findById(Integer.parseInt(id), pageable);
        }
        else if(!Objects.equals(name, " ") && !Objects.equals(id, " ")){
            throw new serviceException("多余参数");
        }
        else{
            pages = tagRepository.findAll(pageable);
        }
        return pages;

    }

    @Override
    public boolean save(Tag tag) {
        tagRepository.save(tag);
        return true;
    }


}
