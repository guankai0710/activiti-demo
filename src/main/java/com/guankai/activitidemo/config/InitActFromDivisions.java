package com.guankai.activitidemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;

/**
 * 类描述：自动建表
 *
 * @author guankai
 * @date 2020/8/25
 **/
@Repository
public class InitActFromDivisions {

    private static final Logger LOG = LoggerFactory.getLogger(InitActFromDivisions.class);

    @Value(value = "${spring.datasource.driver-class-name}")
    private String driver;

    @Value(value = "${spring.datasource.url}")
    private String url;

    @Value(value = "${spring.datasource.username}")
    private String userName;

    @Value(value = "${spring.datasource.password}")
    private String password;

    @PostConstruct
    public void init() throws SQLException, ClassNotFoundException{
        //连接数据库
        Class.forName(driver);
        //测试url中是否包含useSSL字段，没有则添加设该字段且禁用
        if( url.indexOf("?") == -1 ){
            url = url + "?useSSL=false" ;
        } else if( url.indexOf("useSSL=false") == -1 || url.indexOf("useSSL=true") == -1 ) {
            url = url + "&useSSL=false";
        }
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();
        //获取数据库表名
        ResultSet actFromTemplate = conn.getMetaData().getTables(null, null, "ACT_FROM_TEMPLATE", null);
        // 判断表是否存在，如果存在则什么都不做，否则创建表
        if( actFromTemplate.next() ){
            return;
        } else {
            //创建表
            stat.executeUpdate(
                 "  CREATE TABLE `ACT_FROM_TEMPLATE` (                                                 "+
                      "    `ID` 			int AUTO_INCREMENT COMMENT 'id',                               "+
                      "    `FROM_KEY` 		varchar(255) COLLATE utf8_bin NOT NULL COMMENT '表单key',       "+
                      "    `TEMP_TYPE` 		int NOT NULL COMMENT '模板类型，1：URL形式；2：HTML形式',        "+
                      "    `FROM_TEMP` 		varchar(4000) COLLATE utf8_bin NOT NULL COMMENT '表单模板',     "+
                      "    `CREATE_TIME` 	timestamp(3) NULL DEFAULT NULL COMMENT '创建时间',              "+
                      "    `UPDATE_TIME` 	timestamp(3) NULL DEFAULT NULL COMMENT '修改时间',              "+
                      "    `DELETED` 		int DEFAULT 0 COMMENT '是否已删除，0：否；1：是',                "+
                      "    PRIMARY KEY (`ID`)                                                               "+
                      "  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='工作流引擎表单模板'; "
            );
            LOG.info("建表 [ACT_FROM_TEMPLATE] -> 工作流引擎表单模板");
        }

        //获取数据库表名
        ResultSet actFromContent = conn.getMetaData().getTables(null, null, "ACT_FROM_CONTENT", null);
        // 判断表是否存在，如果存在则什么都不做，否则创建表
        if( actFromContent.next() ){
            return;
        } else {
            //创建表
            stat.executeUpdate(
                " CREATE TABLE `ACT_FROM_CONTENT` (                                                         "+
                     "   `ID`               int AUTO_INCREMENT COMMENT 'id',                                     "+
                     "   `PROC_INST_ID`     varchar(64) COLLATE utf8_bin NOT NULL COMMENT '流程实例id',          "+
                     "   `TASK_ID`          varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '任务节点id',      "+
                     "   `FROM_KEY`         varchar(255) COLLATE utf8_bin NOT NULL COMMENT '表单key',            "+
                     "   `CONTENT`          varchar(4000) COLLATE utf8_bin DEFAULT NULL COMMENT '表单内容',      "+
                     "   `CREATE_PERSON`    varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',          "+
                     "   `CREATE_TIME`      timestamp(3) NULL DEFAULT NULL COMMENT '创建时间',                    "+
                     "   PRIMARY KEY (`ID`)                                                                      "+
                     " ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT '工作流引擎表单内容';        "
            );
            LOG.info("建表 [ACT_FROM_CONTENT] -> 工作流引擎表单内容");
        }

        // 释放资源
        stat.close();
        conn.close();
    }
}
