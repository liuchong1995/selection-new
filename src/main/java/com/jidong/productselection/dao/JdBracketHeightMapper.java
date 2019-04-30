package com.jidong.productselection.dao;
import com.jidong.productselection.entity.JdBracketHeight;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator 2018/11/16
*/
@Mapper
public interface JdBracketHeightMapper {
    int insert(JdBracketHeight record);

    int insertSelective(JdBracketHeight record);

    List<JdBracketHeight> findByBracketIdIn(@Param("bracketIdList") List<Integer> bracketIdList);

    JdBracketHeight findByBracketId(@Param("bracketId")Integer bracketId);

    Integer findNextbracketHeightId();

    int updateHeightsByBracketId(@Param("updatedHeights")String updatedHeights,@Param("bracketId")Integer bracketId);


}