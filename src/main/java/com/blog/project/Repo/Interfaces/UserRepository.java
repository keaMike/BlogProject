package com.blog.project.Repo.Interfaces;

import com.blog.project.Model.User;
import java.util.List;

public interface UserRepository {

    void save(User user);
    void delete(User user);
    void update(User user);

    User validateLogin(User user);

    User findById(int Id);
    List findAll();
}
