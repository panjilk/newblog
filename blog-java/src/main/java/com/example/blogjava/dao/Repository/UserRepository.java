package com.example.blogjava.dao.Repository;

import com.example.blogjava.dao.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    User findById(int id);
//@Query("select u from user u where u.username like %:name%")
    Page<User> findByUsername(String name, Pageable pageable);

    Page<User> findById(Integer id, Pageable pageable);
}
