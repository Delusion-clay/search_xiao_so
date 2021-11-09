package com.it.search.dao;

import com.it.search.db.DBCommon;
import com.it.search.domain.WarningMessage;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description:
 * @author: Delusion
 * @date: 2021-05-31 17:04
 */
public class WarningMessageDao {
    private static final Logger LOG = Logger.getLogger(WarningMessageDao.class);


    /**
     *  预警消息写入mysql库
     * @param warningMessage
     * @return
     */
    public static Integer insertWarningMessage(WarningMessage warningMessage){

        //获取数据库连接
        Connection conn = DBCommon.getConn("xiao_so");
        //构建插入SQL
        String sql = "insert into warn_message(id,type,title,url,infomessage,SensitiveWords) values(?,?,?,?,?,?)";

        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        int id=-1;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,warningMessage.getId());
            stmt.setString(2,warningMessage.getType());
            stmt.setString(3,warningMessage.getTitle());
            stmt.setString(4,warningMessage.getUrl());
            stmt.setString(5,warningMessage.getInfomessage());
            stmt.setString(6,warningMessage.getSensitiveWords());
            id = stmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error(null,e);
        }finally {
            try {
                DBCommon.close(conn,stmt,resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

}

