package com.jidong.productselection.controller;

import com.jidong.productselection.response.AdPictureURL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/20 17:02
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/photo")
public class WangEditorUpload {

	@Value("${PRODUCT_IMG_UPLOAD}")
	private String PRODUCT_IMG_UPLOAD;

	@Value("${PRODUCT_IMG_REQUEST_PRE}")
	private String PRODUCT_IMG_REQUEST_PRE;

	@ResponseBody
	@PostMapping("/wangEditorUpload")
	public AdPictureURL upload(@RequestParam("photoList") List<MultipartFile> photoList) throws IOException {

		// 存储图片的虚拟本地路径（这里需要配置tomcat的web模块路径，双击猫进行配置）
		String saveFilePath = PRODUCT_IMG_UPLOAD;
		Integer errno = 0;
		List<String> urls = new ArrayList<>();
		AdPictureURL returnAd = new AdPictureURL();

		for (MultipartFile photo : photoList) {
			String oldFileName = photo.getOriginalFilename(); // 获取上传文件的原名
			// 上传图片
			if (oldFileName != null && oldFileName.length() > 0) {
				// 新的图片名称
				String newFileName = UUID.randomUUID() + oldFileName.substring(oldFileName.lastIndexOf("."));
				// 新图片
				File newFile = new File(saveFilePath  + newFileName);
				// 将内存中的数据写入磁盘
				photo.transferTo(newFile);
				// 将新图片名称返回到前端
				urls.add( PRODUCT_IMG_REQUEST_PRE + newFileName);
			} else {
				errno = 1;
			}
		}
		returnAd.setData(urls);
		returnAd.setErrno(errno);
		return returnAd;
	}

}
