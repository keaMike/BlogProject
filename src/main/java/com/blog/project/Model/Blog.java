package com.blog.project.Model;

import java.util.Date;

public class Blog {

    private int id;
    private String title;
    private String body;
    private Date date;
    private User user;

    public Blog() {
    }

    public Blog(int id, String title, String body, Date date, User user) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.date = date;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", date=" + date +
                ", user=" + user +
                '}';
    }
}
