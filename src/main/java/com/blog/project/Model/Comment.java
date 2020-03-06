package com.blog.project.Model;

import java.util.Date;

public class Comment {

    private int userid;
    private int blogid;
    private int commentid;
    private String comment;
    private Date date;

    public Comment() {
    }

    public Comment(int userid, int blogid, int commentid, String comment, Date date) {
        this.userid = userid;
        this.blogid = blogid;
        this.commentid = commentid;
        this.comment = comment;
        this.date = date;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getBlogid() {
        return blogid;
    }

    public void setBlogid(int blogid) {
        this.blogid = blogid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userid=" + userid +
                ", blogid=" + blogid +
                ", commentid=" + commentid +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                '}';
    }
}
