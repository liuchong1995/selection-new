package com.jidong.productselection.service.impl;

import com.jidong.productselection.dao.JdAttachmentMapper;
import com.jidong.productselection.dao.JdComponentMapper;
import com.jidong.productselection.entity.JdAttachment;
import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.request.GetAttachmentRequest;
import com.jidong.productselection.service.JdAttachmentService;
import com.jidong.productselection.service.JdConstraintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LiuChong
 * @Date: 2018/11/2 13:45
 * @Description:
 */
@Service
public class JdAttachmentServiceImpl implements JdAttachmentService {

	@Autowired
	private JdAttachmentMapper attachmentMapper;

	@Autowired
	private JdComponentMapper componentMapper;

	@Autowired
	private JdConstraintService constraintService;

	@Override
	public List<JdComponent> hasAttachment(GetAttachmentRequest getAttachmentRequest) {
		List<JdAttachment> attachments = attachmentMapper.findByMasterId(getAttachmentRequest.getComponentId());
		List<JdComponent> componentList = new ArrayList<>();
		if (attachments.size() > 0){
			for (JdAttachment attachment : attachments) {
				componentList.add(componentMapper.selectByPrimaryKey(attachment.getAttachmentId()));
			}
		}
		JdComponent component = componentMapper.selectByPrimaryKey(getAttachmentRequest.getComponentId());
		componentList.removeAll(constraintService.getBanComponentList(component.getProductId(),getAttachmentRequest.getSelectedList()));
		return componentList;
	}
}
