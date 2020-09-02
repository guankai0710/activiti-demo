package com.guankai.activitidemo.dao;

import com.guankai.activitidemo.entity.ActFromContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ActFromContentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ActFromContent record);

    int insertSelective(ActFromContent record);

    ActFromContent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActFromContent record);

    int updateByPrimaryKey(ActFromContent record);

    List<ActFromContent> selectByFromKey(@Param("procInstId") String procInstId, @Param("taskId") String taskId, @Param("fromKey") String fromKey);
}