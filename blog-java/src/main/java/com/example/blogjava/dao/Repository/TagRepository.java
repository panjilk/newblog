package com.example.blogjava.dao.Repository;

import com.example.blogjava.dao.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Tag findByName(String name);

    Page<Tag> findByName(String name, Pageable pageable);

    Page<Tag> findById(int i, Pageable pageable);
}
