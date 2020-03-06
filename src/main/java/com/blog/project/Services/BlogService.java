package com.blog.project.Services;

import com.blog.project.Model.Blog;
import com.blog.project.Repo.BlogRepositoryImpl;
import com.blog.project.Repo.CommentRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    @Autowired
    BlogRepositoryImpl blogRepo;
    @Autowired
    CommentRepositoryImpl commentRepo;

    public List fetchAllBlogs() {
        return blogRepo.findAll();
    }

    public void addBlog(Blog blog) {
        if(blogRepo.isExisting(blog)) {
            blogRepo.update(blog);
        } else {
            blogRepo.save(blog);
        }
    }

    public void deleteblog(Blog blog) {
        if(commentRepo.hasComment(blog.getId())) {
            commentRepo.deleteAll(blog.getId());
        }
        blogRepo.delete(blogRepo.findById(blog.getId()));
    }

    public Blog findBlogById(int id) {
        return blogRepo.findById(id);
    }
}
