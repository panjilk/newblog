package com.example.blogjava.dao.Repository;

import com.example.blogjava.dao.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Post findByTitle(String title);

    Post findById(int id);
    //@Query("select u from user u where u.username like %:name%")
    Page<Post> findByTitle(String name, Pageable pageable);

    Page<Post> findById(Integer id, Pageable pageable);
}
