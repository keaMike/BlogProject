package com.blog.project.Repo;

import com.blog.project.Model.Comment;
import com.blog.project.Repo.DbHelper.DbHelper;
import com.blog.project.Repo.Interfaces.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    @Autowired
    private DbHelper dbHelper;

    private PreparedStatement pstm;

    @Override
    public void save(Comment comment) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("INSERT INTO comments(userid, blogid, comment)" +
                                            "VALUES(?, ?, ?)");
            pstm.setInt(1, comment.getUserid());
            pstm.setInt(2, comment.getBlogid());
            pstm.setString(3, comment.getComment());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }

    }

    @Override
    public void delete(Comment comment) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("DELETE FROM comments WHERE commentid = ?");
            pstm.setInt(1, comment.getCommentid());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public void deleteAll(int blogid) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("DELETE FROM comments WHERE blogid = ?");
            pstm.setInt(1, blogid);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public void update(Comment comment) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("UPDATE comments SET comment = ?, date = ? WHERE commentid = ?");
            pstm.setString(1, comment.getComment());
            pstm.setDate(2, getCurrentTimeStamp());
            pstm.setInt(3, comment.getCommentid());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
    }

    public boolean hasComment(int blogid) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("SELECT * FROM comments WHERE blogid = ?");
            pstm.setInt(1, blogid);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return false;
    }

    @Override
    public boolean isExisting(Comment comment) {
        try {
            Connection con = dbHelper.createConnection();
            boolean isExisting;
            pstm = con.prepareStatement("SELECT * FROM comments WHERE commentid = ?");
            pstm.setInt(1, comment.getCommentid());
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                isExisting = true;
            }else {
                isExisting = false;
            }

            return isExisting;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }

        return false;
    }

    @Override
    public List findAll() {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("SELECT * FROM comments");
            ResultSet rs = pstm.executeQuery();
            List<Comment> list = new LinkedList<>();
            while(rs.next()) {
                Comment comment = new Comment();
                fillComment(comment, rs);
                list.add(comment);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return null;
    }

    @Override
    public Comment findById(int id) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("SELECT * FROM comments WHERE commentid = ?");
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            Comment comment = new Comment();
            if(rs.next()) {
                fillComment(comment, rs);
            }
            return comment;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return null;
    }

    @Override
    public void fillComment(Comment comment, ResultSet rs) {
        try {
            comment.setUserid(rs.getInt(1));
            comment.setBlogid(rs.getInt(2));
            comment.setCommentid(rs.getInt(3));
            comment.setComment(rs.getString(4));
            comment.setDate(rs.getDate(5));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Date getCurrentTimeStamp() {
        long millis = System.currentTimeMillis();
        java.sql.Date today = new java.sql.Date(millis);
        return today;

    }
}
