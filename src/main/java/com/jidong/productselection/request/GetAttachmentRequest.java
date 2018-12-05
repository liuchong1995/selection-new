package com.jidong.productselection.request;

import com.jidong.productselection.entity.JdComponent;
import lombok.Data;

import java.util.List;

/**
 * @Auther: LiuChong
 * @Date: 2018/11/22 15:18
 * @Description:
 */
@Data
public class GetAttachmentRequest {

	private Integer componentId;

	private List<JdComponent> selectedList;
}
