package com.jidong.productselection.dao;
import com.jidong.productselection.entity.JdAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator 2018/11/02
*/
@Mapper
public interface JdAttachmentMapper {
    int deleteByPrimaryKey(Integer attachId);

    int insert(JdAttachment record);

    int insertSelective(JdAttachment record);

    JdAttachment selectByPrimaryKey(Integer attachId);

    int updateByPrimaryKeySelective(JdAttachment record);

    int updateByPrimaryKey(JdAttachment record);

    List<JdAttachment> findByMasterId(@Param("masterId") Integer masterId);

    Integer findNextAttachId();

    int updateIsDeletedByAttachId(@Param("attachId") Integer attachId);

	List<JdAttachment> findByAttachmentId(@Param("attachmentId") Integer attachmentId);

}