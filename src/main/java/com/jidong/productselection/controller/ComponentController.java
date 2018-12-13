package com.jidong.productselection.controller;

import com.github.pagehelper.PageInfo;
import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.request.ComponentSearchRequest;
import com.jidong.productselection.request.GetAttachmentRequest;
import com.jidong.productselection.request.OptionalListBySelectedRequest;
import com.jidong.productselection.response.BaseResponse;
import com.jidong.productselection.service.JdAttachmentService;
import com.jidong.productselection.service.JdComponentService;
import com.jidong.productselection.service.JdConstraintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: LiuChong
 * @Date: 2018/12/6 16:06
 * @Description:
 */
@RestController
@RequestMapping("/component")
@Slf4j
public class ComponentController {
	@Value("${PRODUCT_IMG_UPLOAD}")
	private String PRODUCT_IMG_UPLOAD;

	@Autowired
	private JdComponentService componentService;

	@Autowired
	private JdConstraintService constraintService;

	@Autowired
	private JdAttachmentService attachmentService;

	@GetMapping("/getOptionalListByCateId/{categoryId}")
	public BaseResponse<List<JdComponent>> findByCategoryId(@PathVariable("categoryId") Integer categoryId) {
		try {
			List<JdComponent> componentList = componentService.findByCategory(categoryId);
			return BaseResponse.success(componentList);
		} catch (Exception e) {
			log.error("获取部件列表错误！", e);
			return BaseResponse.error();
		}
	}

	@PostMapping("/getOptionalListBySelected")
	public BaseResponse<List<JdComponent>> getOptionalListBySelected(
			@RequestBody OptionalListBySelectedRequest optionalListBySelectedRequest
	) {
		try {
			List<JdComponent> optionalListBySelected =
					constraintService.getOptionalListBySelected(
							optionalListBySelectedRequest.getCategoryId(), optionalListBySelectedRequest.getSelectedList()
					);
			return BaseResponse.success(optionalListBySelected);
		} catch (Exception e) {
			log.error("获取可选部件列表错误！" + e.getMessage(), e);
			return BaseResponse.error();
		}
	}

	@GetMapping("/{componentId}")
	public BaseResponse<JdComponent> findOne(@PathVariable("componentId") Integer componentId) {
		try {
			JdComponent component = componentService.findById(componentId);
			return BaseResponse.success(component);
		} catch (Exception e) {
			log.error("获取部件错误！" + e.getMessage(), e);
			return BaseResponse.error("获取部件错误！");
		}
	}

	@PostMapping("/hasAttachment")
	public BaseResponse<List<JdComponent>> hasAttachment(@RequestBody GetAttachmentRequest getAttachmentRequest){
		try {
			return BaseResponse.success(attachmentService.hasAttachment(getAttachmentRequest));
		} catch (Exception e){
			log.error("获取附件错误！", e);
			return BaseResponse.error("获取附件错误！");
		}
	}

	@PostMapping("/search")
	public BaseResponse<PageInfo<JdComponent>> search(
			@RequestBody ComponentSearchRequest searchRequest
	) {
		try {
			PageInfo<JdComponent> componentPageInfo = componentService.search(searchRequest);
			return BaseResponse.success(componentPageInfo);
		} catch (Exception e) {
			log.error("获取部件列表错误！" + e.getMessage(), e);
			return BaseResponse.error("获取部件列表错误！");
		}
	}

	@DeleteMapping("/deleteOrRecovery/{componentId}")
	public BaseResponse deleteOrRecovery(@PathVariable("componentId") Integer componentId){
		try {
			return BaseResponse.success(componentService.deleteComponent(componentId));
		} catch (Exception e) {
			log.error("删除或恢复部件失败！" + e.getMessage(), e);
			return BaseResponse.error("删除或恢复部件失败！");
		}
	}
}
