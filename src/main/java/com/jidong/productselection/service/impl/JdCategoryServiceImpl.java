package com.jidong.productselection.service.impl;

import com.alibaba.fastjson.JSON;
import com.jidong.productselection.dao.JdCategoryMapper;
import com.jidong.productselection.dao.JdProductMapper;
import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdProduct;
import com.jidong.productselection.request.CategoryAddRequest;
import com.jidong.productselection.service.JdCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: LiuChong
 * @Date: 2018/10/16 13:15
 * @Description:
 */
@Service
public class JdCategoryServiceImpl implements JdCategoryService {

	private static final Integer TOP_LEVEL = Integer.valueOf(0);
	private static final Integer MAX_LEVEL = Integer.valueOf(3);
	private static final Integer IS_SHOW_RANK = Integer.valueOf(5);

	@Autowired
	private JdCategoryMapper categoryMapper;

	@Autowired
	private JdProductMapper productMapper;

	@Override
	public String getMenuTree(Integer prdId) {
		List<Map<Object, Object>> menuTree = categoryMapper.getMenuTree(prdId);
		//menuTree.stream().filter(map -> (boolean) map.get(IS_SHOW_RANK)).collect(Collectors.toList());
		List<Map<Object, Object>> tempMenuTree = new ArrayList<>(menuTree);
		for (Map<Object, Object> singleMenu : menuTree) {
			for (Map.Entry<Object, Object> entry : singleMenu.entrySet()) {
				if (entry.getKey().equals("isShow")) {
					if (!(Boolean) entry.getValue()) {
						tempMenuTree.remove(singleMenu);
					}
				}
			}
		}
		menuTree = tempMenuTree;
		return getTreeList(menuTree);
	}


	@Override
	public String getTreeList(List<Map<Object, Object>> resultMap) {

		Map<String, List<Map<Object, Object>>> temp = new HashMap<String, List<Map<Object, Object>>>();

		for (Map<Object, Object> map : resultMap) {
			//如果temp的键包含这个parentid
			if (temp.containsKey(map.get("parentid").toString())) {
				//那么把所有相同parentid的数据全部添加到该parentid键下
				temp.get(map.get("parentid").toString()).add(map);
			} else {
				//初始化temp  第一次用
				List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
				list.add(map);
				temp.put(map.get("parentid").toString(), list);
			}
		}
		//定义一个完整菜单列表
		ArrayList<Map<Object, Object>> array = new ArrayList<Map<Object, Object>>();

		for (Map<Object, Object> hashMap : resultMap) {
			//如果temp中的键与当前id一致
			if (temp.containsKey(hashMap.get("id").toString())) {
				//说明temp是当前id菜单的子菜单
				hashMap.put("nodes", temp.get(hashMap.get("id").toString()));
			}
			//遇到顶级菜单就添加进完整菜单列表
			if (hashMap.get("parentid").toString().equals("0")) {
				array.add(hashMap);
			}

		}
		return JSON.toJSONString(array);
	}

	private List<Integer> excludePeerCategory(JdCategory category) {
		List<JdCategory> excludeCategories = categoryMapper.findByProductIdAndParentIdAndCategoryId(category.getProductId(), category.getParentId(), category.getCategoryId());
		List<Integer> excludeCategoryIds = new ArrayList<>();
		for (JdCategory excludeCategory : excludeCategories) {
			excludeCategoryIds.add(excludeCategory.getCategoryId());
		}
		return excludeCategoryIds;
	}

	@Override
	public List<Integer> excludeAllCategory(JdCategory category) {
		List<Integer> excludeAllCategoryIds = new ArrayList<>();
		while (!category.getParentId().equals(TOP_LEVEL)) {
			List<Integer> peerCategoryIds = excludePeerCategory(category);
			excludeAllCategoryIds.addAll(peerCategoryIds);
			category = categoryMapper.selectByPrimaryKey(category.getParentId());
		}
		return excludeAllCategoryIds;
	}

	@Override
	public List<JdCategory> findByProductIdAndParentId(Integer productId, Integer parentId) {
		return categoryMapper.findByProductIdAndParentId(productId, parentId);
	}

	@Override
	public JdCategory findOne(Integer categoryId) {
		return categoryMapper.selectByPrimaryKey(categoryId);
	}

	@Override
	@Transactional
	public int add(CategoryAddRequest categoryAddRequest) {
		JdCategory category = new JdCategory();
		List<JdCategory> preCategories = categoryAddRequest.getPreCategories();
		Integer productId = categoryAddRequest.getProduct().getProductId();
		category.setCategoryId(categoryMapper.findNextCategoryId())
				.setCategoryName(categoryAddRequest.getNewCategoryName())
				.setProductId(productId)
				.setIsLeaf(false)
				.setIsShow(categoryAddRequest.getIsShow());
		if (!preCategories.isEmpty()) {
			JdCategory parentCategory = preCategories.get(preCategories.size() - 1);
			category.setParentId(parentCategory.getCategoryId())
					.setCategoryLevel(preCategories.size());
			if (preCategories.size() == MAX_LEVEL) {
				category.setIsLeaf(true);
			}
		} else {
			category.setParentId(TOP_LEVEL)
					.setCategoryLevel(TOP_LEVEL);
			if (categoryAddRequest.getCategoryOrder() != null){
				List<Integer> orders = categoryMapper.findByProductIdAndParentId(productId, TOP_LEVEL).stream().map(JdCategory::getCategoryOrder).collect(Collectors.toList());
				if (!orders.contains(categoryAddRequest.getCategoryOrder())){
					category.setCategoryOrder(categoryAddRequest.getCategoryOrder());
				} else {
					throw new RuntimeException("已存在序号");
				}
			}
			Integer categoryId = category.getCategoryId();
			JdProduct product = productMapper.selectByPrimaryKey(productId);
			if (categoryAddRequest.getIsMainCate()){
				product.setMainCateid(categoryId);
			} else if(categoryAddRequest.getIsInstallation()){
				product.setInstallationId(categoryId);
			} else if (categoryAddRequest.getIsShelf()){
				product.setShelfId(categoryId);
			} else if (categoryAddRequest.getIsVoltage()){
				product.setVoltageId(categoryId);
			}
			productMapper.updateByPrimaryKeySelective(product);
		}
		return categoryMapper.insert(category);
	}

	@Override
	@Transactional
	public int update(JdCategory category) {
		return categoryMapper.updateByPrimaryKeySelective(category);
	}

	@Override
	@Transactional
	public int delete(JdCategory category) {
		return categoryMapper.deleteByPrimaryKey(category.getCategoryId());
	}

	//妥协了，人肉递归好了
	@Override
	public List<JdCategory> getAllLeafCategory(Integer categoryId) {
		List<JdCategory> allLeafCategory = new ArrayList<>();
		JdCategory category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category.getIsLeaf()) {
			allLeafCategory.add(category);
		} else {
			for (JdCategory subCategory : categoryMapper.findByParentId(categoryId)) {
				if (subCategory.getIsLeaf()) {
					allLeafCategory.add(subCategory);
				} else {
					for (JdCategory subSubCategory : categoryMapper.findByParentId(subCategory.getCategoryId())) {
						if (subSubCategory.getIsLeaf()) {
							allLeafCategory.add(subSubCategory);
						} else {
							for (JdCategory subSubSubCategory : categoryMapper.findByParentId(subSubCategory.getCategoryId())) {
								if (subSubSubCategory.getIsLeaf()) {
									allLeafCategory.add(subSubCategory);
								}
							}
						}
					}
				}
			}
		}
		return allLeafCategory;
	}

	@Override
	public List<JdCategory> getNewMenuTree(Integer prdId, Integer parentId) {
		return categoryMapper.getNewMenuTree(prdId,parentId);
	}

	@Override
	public List<JdCategory> getAllNewMenuTree(Integer prdId, Integer parentId) {
		return categoryMapper.getAllNewMenuTree(prdId,parentId);
	}

	/**
	 * 返回该分类下所有子分类，不包括该分类
	 * @param categoryId
	 * @return
	 */
	@Override
	public List<JdCategory> getAllSubCates(Integer categoryId) {
		JdCategory currCategory = categoryMapper.selectByPrimaryKey(categoryId);
		if (currCategory.getIsLeaf()) {
			return Collections.emptyList();
		}
		List<JdCategory> res = new ArrayList<>();
		List<JdCategory> secondList = categoryMapper.findByParentId(currCategory.getCategoryId());
		if (secondList != null && secondList.size() > 0) {
			res.addAll(secondList);
			for (JdCategory secondCategory : secondList) {
				if (!secondCategory.getIsLeaf()){
					List<JdCategory> thirdList = categoryMapper.findByParentId(secondCategory.getCategoryId());
					if (thirdList != null && thirdList.size() > 0) {
						res.addAll(thirdList);
						for (JdCategory thirdCategory : thirdList) {
							if (!thirdCategory.getIsLeaf()){
								List<JdCategory> forthList = categoryMapper.findByParentId(thirdCategory.getCategoryId());
								if (forthList != null && forthList.size() > 0) {
									res.addAll(forthList);
								}
							}
						}
					}
				}
			}
		}
		return res;
	}
}
