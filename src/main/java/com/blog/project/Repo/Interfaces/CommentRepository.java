package com.blog.project.Repo.Interfaces;

import com.blog.project.Model.Comment;

import java.sql.ResultSet;
import java.util.List;

public interface CommentRepository {

    void save(Comment comment);
    void delete(Comment comment);
    void deleteAll(int id);
    void update(Comment comment);
    boolean isExisting(Comment comment);
    boolean hasComment(int id);
    List findAll();
    Comment findById(int id);
    void fillComment(Comment comment, ResultSet rs);
}
