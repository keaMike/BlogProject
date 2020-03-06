package com.blog.project.Repo;

import com.blog.project.Model.Message;
import com.blog.project.Repo.DbHelper.DbHelper;
import com.blog.project.Repo.Interfaces.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    @Autowired
    DbHelper dbHelper;
    PreparedStatement pstm;

    @Override
    public void save(Message message) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("INSERT INTO messages(messageid, receiverid, senderid, message)" +
                    "VALUES(?, ?, ?, ?)");
            pstm.setInt(1, message.getMessageid());
            pstm.setInt(2, message.getReceiverId());
            pstm.setInt(3, message.getSenderId());
            pstm.setString(4, message.getMessage());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public void deleteAll(int userid, int otherid) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("DELETE FROM messages WHERE receiverid = ? AND senderid = ? OR receiverid = ? AND senderid = ?");
            pstm.setInt(1, userid);
            pstm.setInt(2, otherid);
            pstm.setInt(3, otherid);
            pstm.setInt(4, userid);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public List findAll(int userid) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("SELECT * FROM messages WHERE receiverid = ? OR senderid = ?");
            pstm.setInt(1, userid);
            pstm.setInt(2, userid);
            ResultSet rs = pstm.executeQuery();
            return fillList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return null;
    }

    public List findAllChatMsg(int userid, int otherid) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("SELECT * FROM messages " +
                                            "WHERE receiverid = ? AND senderid = ? OR receiverid = ? AND senderid = ? " +
                                            "ORDER BY msgdate DESC");
            msgStatements(userid, otherid);
            ResultSet rs = pstm.executeQuery();
            return fillList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return null;
    }

    public Message findLastMsg(int userid, int otherid) {
        try {
            Connection con = dbHelper.createConnection();
            pstm = con.prepareStatement("SELECT * FROM messages " +
                                            "WHERE receiverid = ? AND senderid = ? OR receiverid = ? AND senderid = ? " +
                                            "ORDER BY msgdate DESC LIMIT 1");
            msgStatements(userid, otherid);
            ResultSet rs = pstm.executeQuery();
            Message msg = new Message();
            while (rs.next()) {
                msg.setMessageid(rs.getInt(1));
                msg.setReceiverId(rs.getInt(2));
                msg.setSenderId(rs.getInt(3));
                msg.setMessage(rs.getString(4));
                msg.setDate(rs.getDate(5));
            }
            return msg;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return null;
    }

    public List fillList(ResultSet rs) {
        try {
            List messages = new LinkedList();
            while (rs.next()) {
                Message message = new Message();
                message.setMessageid(rs.getInt(1));
                message.setReceiverId(rs.getInt(2));
                message.setSenderId(rs.getInt(3));
                message.setMessage(rs.getString(4));
                message.setDate(rs.getDate(5));

                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void msgStatements(int userid, int otherid) {
        try {
            pstm.setInt(1, userid);
            pstm.setInt(2, otherid);
            pstm.setInt(3, otherid);
            pstm.setInt(4, userid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
