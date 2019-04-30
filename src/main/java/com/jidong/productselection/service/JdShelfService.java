package com.jidong.productselection.service;

import com.jidong.productselection.dto.ShelfHeight;
import com.jidong.productselection.entity.JdBracketMountingHeight;
import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.entity.JdShelfConstraint;

import java.util.List;
import java.util.Map;

/**
 * @Author: LiuChong
 * @Date: 2018/11/16 22:25
 * @Description:
 */
public interface JdShelfService {

	Map<Integer, List<ShelfHeight>> getAllShelfHeight(Integer productId);

	List<JdBracketMountingHeight> getAllMountHeight();

	List<JdShelfConstraint> getAllShelfConstraint(Integer productId);

	List<JdComponent> getAllInstallation(Integer productId);

	List<ShelfHeight> getConstraint(Integer shelfId);

	int insertOrUpdate(String heights,Integer bracketId);
}
