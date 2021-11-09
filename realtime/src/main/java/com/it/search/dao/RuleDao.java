package com.it.search.dao;

import com.it.search.db.DBCommon;
import com.it.search.domain.RuleDomain;
import com.it.search.timer.SyncRule2RedisTimer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * @description:
 * @author: Delusion
 * @date: 2021-05-31 16:06
 */
public class RuleDao {
    private static final Logger LOG = Logger.getLogger(SyncRule2RedisTimer.class);
    public static List<RuleDomain> getRuleList(){
        List<RuleDomain> listRules = null;
        //数据库连接
        Connection conn = DBCommon.getConn("xiao_so");
        QueryRunner query = new QueryRunner();
        String sql = "select * from rule";
        try {
            listRules = query.query(conn,sql,new BeanListHandler<>(RuleDomain.class));

        } catch (SQLException e) {
            LOG.error(null,e);
            e.printStackTrace();
        }
        return listRules;
    }
    public static void main(String[] args) {
        List<RuleDomain> ruleList = RuleDao.getRuleList();
        ruleList.forEach(x->{
            System.out.println(x.getWords());
        });
    }
}
