package com.blog.project.Services;


import com.blog.project.Model.User;
import com.blog.project.Repo.UserRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepositoryImpl userRepo;

    public List fetchAllUsers() {
        List<User> user = new LinkedList<>();
        user.addAll(userRepo.findAll());
        return user;
    }

    public void addUser(User user) {
        if(userRepo.isExisting(user)) {
            userRepo.update(user);
        } else {
            if (!isEncrypted(user.getPassword())) {
                user.setPassword(encryptPassword(user.getPassword()));
            }
            userRepo.save(user);
        }
    }

    public void deleteUser(User user) {
        userRepo.delete(user);
    }

    public void changeStatus(User user) {
        user.setStatus(!user.isStatus());
    }

    public User validateLogin(User user) {
        user.setPassword(encryptPassword(user.getPassword()));
        return userRepo.validateLogin(user);
    }

    public int getRequestCounter() {
        List<User> userList = fetchAllUsers();
        int reqnotification = 0;
        for(User users: userList) {
            if(!users.isStatus()) {
                reqnotification = reqnotification + 1;
            }
        }
        return reqnotification;
    }

    public User findUserById(int id) {
        return userRepo.findById(id);
    }

    public String encryptPassword(String pass) {
        String generatedPass = baseEncryption(addSalt(pass));
        return generatedPass;
    }

    public String addSalt(String pass) {
        String salt = "F1nD1nGd0rY";
        return salt + pass;
    }

    public String baseEncryption(String pass) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedhash = digest.digest(pass.getBytes());

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < encodedhash.length; i++) {
            String hex = Integer.toHexString(0xff & encodedhash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public boolean isEncrypted(String pass) {
        if(pass.length() >= 64) {
            return true;
        }
        return false;
    }
}
