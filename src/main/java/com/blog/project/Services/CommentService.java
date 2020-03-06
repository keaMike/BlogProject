package com.blog.project.Services;

import com.blog.project.Model.Comment;
import com.blog.project.Repo.Interfaces.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepo;

    public void addComment(Comment comment) {
        if(commentRepo.isExisting(comment)) {
            commentRepo.update(comment);
        } else {
            commentRepo.save(comment);
        }
    }

    public void deleteComment(Comment comment) {
        commentRepo.delete(comment);
    }

    public void deleteAllComments(int blogid) {
        commentRepo.deleteAll(blogid);
    }

    public List fetchAllComments() {
        return commentRepo.findAll();
    }

    public Comment findCommentById(int id) {
        return commentRepo.findById(id);
    }
}
