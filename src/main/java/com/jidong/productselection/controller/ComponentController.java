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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

	@PostMapping("/uploadFile")
	public Map<String, Object> uploadFile(@RequestParam("singleImage") MultipartFile singleImage)
			throws IllegalStateException, IOException {
		// 原始名称
		String oldFileName = singleImage.getOriginalFilename(); // 获取上传文件的原名
		// 存储图片的虚拟本地路径（这里需要配置tomcat的web模块路径，双击猫进行配置）
		String saveFilePath = PRODUCT_IMG_UPLOAD;
		// 上传图片
		if (oldFileName != null && oldFileName.length() > 0) {
			// 新的图片名称
			String newFileName = UUID.randomUUID() + oldFileName.substring(oldFileName.lastIndexOf("."));
			// 新图片
			File newFile = new File(saveFilePath  + newFileName);
			// 将内存中的数据写入磁盘
			singleImage.transferTo(newFile);
			// 将新图片名称返回到前端
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", "成功啦");
			map.put("url", newFileName);
			return map;
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", "图片不合法");
			return map;
		}
	}
}
