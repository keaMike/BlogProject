package com.blog.project.Services;

import com.blog.project.Model.Message;
import com.blog.project.Repo.MessageRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageService {

    @Autowired
    MessageRepositoryImpl messageRepo;

    public void addMessage(Message message) {
        messageRepo.save(message);
    }

    public void deleteConversation(int userid, int otherid) {
        messageRepo.deleteAll(userid, otherid);
    }

    public List fetchAllChatMsg(int userid, int otherid) {
        return messageRepo.findAllChatMsg(userid, otherid);
    }

    public Message fetchLastMessage(int userid, int otherid) {
        return messageRepo.findLastMsg(userid, otherid);
    }

    public Set fetchAllConversations(int userid) {
        List<Message> allMsgs = messageRepo.findAll(userid);
        Set<Integer> allConversations = new HashSet();
        for(int i = 0; i < allMsgs.size(); i++) {
            allConversations.add(allMsgs.get(i).getReceiverId());
            allConversations.add(allMsgs.get(i).getSenderId());
        }
        return allConversations;
    }
}
