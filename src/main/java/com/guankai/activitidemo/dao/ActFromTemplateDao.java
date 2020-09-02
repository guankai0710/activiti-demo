package com.guankai.activitidemo.dao;

import com.guankai.activitidemo.entity.ActFromTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ActFromTemplateDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ActFromTemplate record);

    int insertSelective(ActFromTemplate record);

    ActFromTemplate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActFromTemplate record);

    int updateByPrimaryKey(ActFromTemplate record);
}