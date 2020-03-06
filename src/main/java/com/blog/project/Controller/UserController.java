package com.blog.project.Controller;

import com.blog.project.Model.Blog;
import com.blog.project.Model.Message;
import com.blog.project.Model.User;
import com.blog.project.Services.BlogService;
import com.blog.project.Services.MessageService;
import com.blog.project.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //USER PAGES
    private final String HOME = "home";

    //ADMIN PAGES
    private final String ALLADMINS = "alladmins";
    private final String ALLUSERS = "allusers";
    private final String EDITUSER = "edituser";

    //MISC
    private final String LOGIN = "login";
    private final String REQUEST = "requestacess";
    private final String REDIRECT = "redirect:/";

    @Autowired
    UserService userService;
    @Autowired
    BlogService blogService;

    //ROOT PAGE - GET
    @GetMapping("/")
    public String root() {
        return REDIRECT + LOGIN;
    }

    //LOGIN PAGE - GET
    @GetMapping("/login")
    public String login() {
        return LOGIN;
    }

    //LOGIN PAGE - POST
    @PostMapping("/login")
    public String login(@ModelAttribute User userform) {
        log.info("Loggin in...");

        User user = userService.validateLogin(userform);

        if (user != null && user.isStatus()) {
            log.info("Logged in...");
            return REDIRECT + "home/" + user.getId();
        }
        return LOGIN;
    }

    //REQUEST ACESS PAGE - GET
    @GetMapping("/requestacess")
    public String requestacess() {
        return REQUEST;
    }

    //REQUEST ACESS PAGE - POST
    @PostMapping("/requestacess")
    public String addrequestacess(@ModelAttribute User user) {
        //IF THERES NO USERS, GIVE INSTANT PERMIT
        if(userService.fetchAllUsers().size() == 0) {
            user.setStatus(true);
        }
        userService.addUser(user);

        return REDIRECT + LOGIN;
    }

    //USER LANDING PAGE
    @GetMapping("/home/{userid}")
    public String home(@PathVariable int userid, Model model) {
        List<Blog> blogList = blogService.fetchAllBlogs();
        List<User> userList = userService.fetchAllUsers();
        User user = userService.findUserById(userid);
        if(user.isAdmin()) {
            int reqnotification = userService.getRequestCounter();

            model.addAttribute("reqnotification", reqnotification);
            model.addAttribute("userList", userList);
            model.addAttribute("blogList", blogList);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("blogList", blogList);
            model.addAttribute("user", user);
        }
        return HOME;
    }

    //ALL ADMINS PAGE
    @GetMapping("/home/{userid}/alladmins")
    public String alladmins(@PathVariable int userid, Model model) {
        allUsersAndAdminsAttr(userid, model);
        return ALLADMINS;
    }

    //ALL USERS PAGE
    @GetMapping("/home/{userid}/allusers")
    public String allusers(@PathVariable int userid, Model model) {
        allUsersAndAdminsAttr(userid, model);
        return ALLUSERS;
    }

    public void allUsersAndAdminsAttr(int userid, Model model) {
        List<User> userList = userService.fetchAllUsers();
        User user = userService.findUserById(userid);

        int reqnotification = userService.getRequestCounter();

        model.addAttribute("reqnotification", reqnotification);
        model.addAttribute("user", user);
        model.addAttribute("userList", userList);
    }

    //ALL USERS PAGE - CHANGE STATUS
    @GetMapping("/home/{userid}/changestatus/{id}")
    public String changestatus(@PathVariable int userid, @PathVariable int id) {
        User user = userService.findUserById(id);
        userService.changeStatus(user);
        userService.addUser(user);
        return REDIRECT + HOME + "/" + userid + "/" + ALLUSERS;
    }

    //DELETE USER & REQUEST
    @GetMapping("/home/{userid}/deleteuser/{id}")
    public String deleteuser(@PathVariable int userid, @PathVariable int id) {
        User user = userService.findUserById(id);
        if(userid != id) {
            userService.deleteUser(user);
        }

        return REDIRECT + HOME + "/" + userid + "/" + ALLUSERS;
    }

    //EDIT USER PAGE - GET
    @GetMapping("/home/{userid}/edituser/{id}")
    public String edituser(@PathVariable int id, @PathVariable int userid, Model model) {
        User user = userService.findUserById(id);
        User mainuser = userService.findUserById(userid);

        model.addAttribute("user", mainuser);
        model.addAttribute("users", user);
        return EDITUSER;
    }

    //EDIT USER PAGE - POST
    @PostMapping("/home/{userid}/edituser/{id}")
    public String edituser(@PathVariable int userid, @PathVariable int id, @ModelAttribute User formuser) {
        User user = userService.findUserById(id);
        user.setAdmin(formuser.isAdmin());
        userService.addUser(user);

        if(user.isAdmin()) {
            return REDIRECT + HOME + "/" + userid + "/" + ALLADMINS;
        } else {
            return REDIRECT + HOME + "/" + userid + "/" + ALLUSERS;
        }
    }
}
