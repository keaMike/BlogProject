package com.blog.project.Repo;

import com.blog.project.Model.Blog;
import com.blog.project.Model.User;
import com.blog.project.Repo.DbHelper.DbHelper;
import com.blog.project.Repo.Interfaces.BlogRepository;
import com.blog.project.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Repository
public class BlogRepositoryImpl implements BlogRepository {

    @Autowired
    private UserService userService;

    @Autowired
    private DbHelper dbHelper;

    private PreparedStatement pstm;


    @Override
    public void save(Blog blog) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("INSERT INTO blogs(userid, blogtitle, blogbody)" +
                                            "VALUES(?, ?, ?)");
            pstm.setInt(1, blog.getUser().getId());
            pstm.setString(2, blog.getTitle());
            pstm.setString(3, blog.getBody());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public void delete(Blog blog) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("DELETE FROM blogs WHERE blogid = ?");
            pstm.setInt(1, blog.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public void update(Blog blog) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("UPDATE blogs SET userid = ?, blogid = ?, blogtitle = ?, blogbody = ?, blogdate = ? WHERE blogid = ?");
            pstm.setInt(1, blog.getUser().getId());
            pstm.setInt(2, blog.getId());
            pstm.setString(3, blog.getTitle());
            pstm.setString(4, blog.getBody());
            pstm.setDate(5, getCurrentTimeStamp());
            pstm.setInt(6, blog.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public Blog findById(int Id) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("SELECT * FROM blogs WHERE blogid = ?");
            pstm.setInt(1, Id);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()) {
                int userid = rs.getInt(1);
                int blogid = rs.getInt(2);
                String blogtitle = rs.getString(3);
                String blogbody = rs.getString(4);
                Date blogdate = rs.getDate(5);

                User user = userService.findUserById(userid);

                Blog blog = new Blog(blogid, blogtitle, blogbody, blogdate, user);
                return blog;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return null;
    }

    @Override
    public boolean isExisting(Blog blog) {
        try {
            Connection con = dbHelper.createConnection();
            boolean isExisting;
            pstm = con.prepareStatement("SELECT * FROM blogs WHERE blogid = ?");
            pstm.setInt(1, blog.getId());
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
            List<Blog> list = new LinkedList<>();
            pstm = con.prepareStatement("SELECT * FROM blogs");
            ResultSet rs = pstm.executeQuery();

            while(rs.next()) {
                int blogid = rs.getInt("blogid");
                Blog blog = findById(blogid);
                list.add(blog);
            }

            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return null;
    }

    private static Date getCurrentTimeStamp() {
        long millis = System.currentTimeMillis();
        java.sql.Date today = new java.sql.Date(millis);
        return today;

    }
}
