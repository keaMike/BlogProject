package com.blog.project.Repo;

import com.blog.project.Model.User;
import com.blog.project.Repo.DbHelper.DbHelper;
import com.blog.project.Repo.Interfaces.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DbHelper dbHelper;

    private PreparedStatement pstm;

    public UserRepositoryImpl() {
    }

    @Override
    public void save(User user) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("INSERT INTO users(username, userpassword, userrole, userstatus)" +
                                            "VALUES(?, ?, ?, ?)");
            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getPassword());
            pstm.setBoolean(3, user.isAdmin());
            pstm.setBoolean(4, user.isStatus());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public void delete(User user) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("DELETE FROM users WHERE userid = ?");
            pstm.setInt(1, user.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public void update(User user) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("UPDATE users SET userstatus = ?, userrole = ? WHERE userid = ?");
            pstm.setBoolean(1, user.isStatus());
            pstm.setBoolean(2, user.isAdmin());
            pstm.setInt(3, user.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public User validateLogin(User user) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("SELECT * FROM users WHERE username = ?  AND userpassword = ?");
            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getPassword());

            ResultSet rs = pstm.executeQuery();
            User tempuser = fillUser(rs);
            if(tempuser.getId() > 0) {
                return tempuser;
            }

            return null;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }

        return null;
    }

    @Override
    public User findById(int Id) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("SELECT * FROM users WHERE userid = ?");
            pstm.setInt(1, Id);
            ResultSet rs = pstm.executeQuery();
            return fillUser(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return null;
    }

    @Override
    public List findAll() {
        try {
            Connection con = dbHelper.createConnection();
            List<User> list = new LinkedList<>();
            pstm = con.prepareStatement("SELECT * FROM users");
            ResultSet rs = pstm.executeQuery();

            if(rs != null) {
                fillUserList(list, rs);
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return null;
    }

    public User fillUser(ResultSet rs) {
        try {
            while(rs.next()) {
                User user = fillBody(rs);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List fillUserList(List list, ResultSet rs) {
        try {
            while(rs.next()) {
               User user = fillBody(rs);
               list.add(user);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private User fillBody(ResultSet rs) {
        User user = new User();
        try {
            user.setId(rs.getInt(1));
            user.setUsername(rs.getString(2));
            user.setPassword(rs.getString(3));
            user.setAdmin(rs.getBoolean(4));
            user.setStatus(rs.getBoolean(5));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isExisting(User user) {
        try {
            Connection con = dbHelper.createConnection();
            boolean isExisting;
            pstm = con.prepareStatement("SELECT * FROM users WHERE userid = ?");
            pstm.setInt(1, user.getId());
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
}
