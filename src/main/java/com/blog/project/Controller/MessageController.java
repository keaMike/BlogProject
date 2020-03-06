package com.blog.project.Controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Controller
public class MessageController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //USER PAGES
    private final String HOME = "home";
    private final String CHATROOM = "chat";
    private final String REDIRECT = "redirect:/";

    @Autowired
    UserService userService;
    @Autowired
    BlogService blogService;
    @Autowired
    MessageService messageService;

    //CHATROOM PAGE
    @GetMapping("/home/{userid}/chatroom")
    public String chatRoom(@PathVariable int userid, Model model) {
        chatRoomAttributes(userid, model);
        model.addAttribute("chatActive", false);
        return CHATROOM;
    }


    //CHAT ROOM WITH OTHER USER
    @GetMapping("/home/{userid}/chatroom/{otherid}")
    public String chatRoom(@PathVariable int userid, Model model, @PathVariable int otherid) {
        chatRoomAttributes(userid, model);
        List msgList = messageService.fetchAllChatMsg(userid, otherid);
        User otheruser = userService.findUserById(otherid);


        Message msg = new Message();
        model.addAttribute("msg", msg);
        model.addAttribute("msgList", msgList);
        model.addAttribute("otheruser", otheruser);
        model.addAttribute("chatActive", true);
        return CHATROOM;
    }

    //CHAT ROOM SAVE MESSAGE
    @PostMapping("/home/{userid}/chatroom/{otherid}")
    public String saveMsg(@ModelAttribute Message message, @PathVariable int userid, @PathVariable int otherid) {
        message.setSenderId(userid);
        message.setReceiverId(otherid);
        messageService.addMessage(message);
        return REDIRECT + HOME + "/" + userid + "/chatroom/" + otherid;
    }

    //DELETE CONVERSATION
    @GetMapping("/home/{userid}/deleteconversation/{otherid}")
    public String deleteConversation(@PathVariable int userid, @PathVariable int otherid, Model model) {
        log.info(userid + " " + otherid);
        messageService.deleteConversation(userid, otherid);
        model.addAttribute("chatActive", false);
        return REDIRECT + HOME + "/" + userid + "/chatroom/";
    }


    public void chatRoomAttributes(int userid, Model model) {
        User user = userService.findUserById(userid);
        List users = userService.fetchAllUsers();
        Set<Integer> conList = messageService.fetchAllConversations(userid);
        int reqnotification = userService.getRequestCounter();
        List lastMsgList = new ArrayList();
        Iterator<Integer> it = conList.iterator();
        while(it.hasNext()) {
            Message msg = messageService.fetchLastMessage(userid, it.next());
            if(msg.getMessage() != null) {
                lastMsgList.add(msg);
            }
        }
        removeActiveUser(users, userid);

        model.addAttribute("reqnotification", reqnotification);
        model.addAttribute("users", users);
        model.addAttribute("conList", conList);
        model.addAttribute("user", user);
        model.addAttribute("lastMsgList", lastMsgList);
    }

    public void removeActiveUser(List<User> users, int userid) {
        //REMOVE ACTIVE USER FROM USER LIST
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getId() == userid) {
                users.remove(i);
            }
        }
    }

}
