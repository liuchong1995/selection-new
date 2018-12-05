package com.jidong.productselection.service;

import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.request.GetAttachmentRequest;

import java.util.List;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/2 13:44
 * @Description:
 */
public interface JdAttachmentService {

	List<JdComponent> hasAttachment(GetAttachmentRequest getAttachmentRequest);
}
