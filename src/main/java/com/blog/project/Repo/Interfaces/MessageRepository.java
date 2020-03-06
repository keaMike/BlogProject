package com.blog.project.Repo.Interfaces;

import com.blog.project.Model.Message;

import java.sql.ResultSet;
import java.util.List;

public interface MessageRepository {

    void save(Message message);
    void deleteAll(int id, int otherid);
    List findAll(int id);
}
