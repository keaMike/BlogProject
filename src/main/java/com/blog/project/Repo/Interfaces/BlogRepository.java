package com.blog.project.Repo.Interfaces;

import com.blog.project.Model.Blog;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface BlogRepository {

    void save(Blog blog);
    void delete(Blog blog);
    void update(Blog blog);
    boolean isExisting(Blog blog);

    Blog findById(int Id);
    List findAll();

}
