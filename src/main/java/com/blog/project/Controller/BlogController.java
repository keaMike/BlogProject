package com.blog.project.Controller;

import com.blog.project.Model.Blog;
import com.blog.project.Model.Comment;
import com.blog.project.Model.User;
import com.blog.project.Services.BlogService;
import com.blog.project.Services.CommentService;
import com.blog.project.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class BlogController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //USER PAGES
    private final String VIEWBLOG = "viewblog";

    //ADMIN PAGES
    private final String HOME = "home";
    private final String ADDBLOG = "addblog";
    private final String EDITBLOG = "editblog";

    //MISC
    private final String REDIRECT = "redirect:/";

    @Autowired
    UserService userService;
    @Autowired
    BlogService blogService;
    @Autowired
    CommentService commentService;

    //ADD BLOG PAGE - GET
    @GetMapping("/home/{userid}/addblog")
    public String addblog(@PathVariable int userid, Model model) {
        Blog blog = new Blog();
        User user = userService.findUserById(userid);
        addOrEditBlogAttributes(model, blog, user);

        return ADDBLOG;
    }

    //ADD BLOG PAGE - POST
    @PostMapping("/home/{userid}/addblog")
    public String addblog(@PathVariable int userid, @ModelAttribute Blog blog) {
        User user = userService.findUserById(userid);
        blog.setUser(user);
        blogService.addBlog(blog);

        return REDIRECT + HOME + "/" + user.getId();
    }

    //DELETE BLOG PAGE
    @GetMapping("/home/{userid}/deleteblog/{blogid}")
    public String deleteblog(@PathVariable int userid, @PathVariable int blogid) {
        Blog blog = blogService.findBlogById(blogid);
        blogService.deleteblog(blog);

        return REDIRECT + HOME + "/" + userid;
    }

    //EDIT BLOG PAGE - GET
    @GetMapping("/home/{userid}/editblog/{blogid}")
    public String editblog(@PathVariable int userid, @PathVariable int blogid, Model model) {
        Blog blog = blogService.findBlogById(blogid);
        User user = userService.findUserById(userid);
        addOrEditBlogAttributes(model, blog, user);

        return EDITBLOG;
    }

    //EDIT BLOG PAGE - POST
    @PostMapping("/home/{userid}/editblog")
    public String editblog(@PathVariable int userid, @ModelAttribute Blog blog) {
        blogService.addBlog(blog);

        return REDIRECT + HOME + "/" + userid;
    }

    //VIEW BLOG PAGE - GET
    @GetMapping("/home/{userid}/viewblog/{blogid}")
    public String viewBlogUser(@PathVariable int userid, @PathVariable int blogid, Model model) {
        Blog blog = blogService.findBlogById(blogid);
        User user = userService.findUserById(userid);
        List commentList = commentService.fetchAllComments();
        List userList = userService.fetchAllUsers();

        boolean editbool = false;

        Comment comment = new Comment();
        viewBlogAttributes(model, blog, user, comment, commentList, userList, editbool);

        return VIEWBLOG;
    }

    //VIEW BLOG PAGE - POST (ADD COMMENT)
    @PostMapping("/{userid}/{blogid}/addcomment")
    public String addComment(@ModelAttribute Comment comment, @PathVariable int userid, @PathVariable int blogid) {
        commentService.addComment(comment);

        return REDIRECT + HOME + "/" + userid + "/viewblog/" + blogid;
    }

    //DELETE COMMENT
    @GetMapping("/home/{userid}/deletecomment/{blogid}/{commentid}")
    public String deleteComment(@PathVariable int userid, @PathVariable int blogid, @PathVariable int commentid) {
        Comment comment = commentService.findCommentById(commentid);
        commentService.deleteComment(comment);

        return REDIRECT + HOME + "/" + userid + "/viewblog/" + blogid;
    }

    //EDIT COMMENT
    @GetMapping("/home/{userid}/editcomment/{blogid}/{commentid}")
    public String editComment(Model model, @PathVariable int commentid, @PathVariable int userid, @PathVariable int blogid) {
        Blog blog = blogService.findBlogById(blogid);
        User user = userService.findUserById(userid);
        Comment comment = commentService.findCommentById(commentid);
        List commentList = commentService.fetchAllComments();
        List userList = userService.fetchAllUsers();

        boolean editbool = true;
        viewBlogAttributes(model, blog, user, comment, commentList, userList, editbool);

        return VIEWBLOG;
    }

    //ADD OR EDIT BLOG ATTRIBUTES
    public void addOrEditBlogAttributes(Model model, Blog blog, User user) {
        int reqnotification = userService.getRequestCounter();

        model.addAttribute("reqnotification", reqnotification);
        model.addAttribute("blog", blog);
        model.addAttribute("user", user);
    }

    //VIEW BLOG ATTRIBUTES
    public void viewBlogAttributes(Model model, Blog blog, User user, Comment comment, List commentList, List userList, boolean editbool) {
        model.addAttribute("blog", blog);
        model.addAttribute("user", user);
        model.addAttribute("comment", comment);
        model.addAttribute("commentList", commentList);
        model.addAttribute("userList", userList);
        model.addAttribute("editbool", editbool);
    }
}
