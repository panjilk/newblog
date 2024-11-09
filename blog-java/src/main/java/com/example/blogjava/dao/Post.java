package com.example.blogjava.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "posts")//使用 @Entity 注解可以将一个普通的Java类标识为一个持久化实体（即与数据库表对应的对象）。
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "user_id")
    private Integer userid;
    @Column(name = "created_time")
    private String created_time;
    @Column(name = "updated_time")
    private String updated_time;

    @Column(name = "good_number")
    private Integer good_number;

    @Column(name = "watch_number")
    private Integer watch_number;

}
