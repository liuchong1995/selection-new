package com.jidong.productselection.service.impl;

import com.alibaba.fastjson.JSON;
import com.jidong.productselection.dao.JdCategoryMapper;
import com.jidong.productselection.dao.JdMutexDescribeMapper;
import com.jidong.productselection.dao.JdProductMapper;
import com.jidong.productselection.entity.JdCategory;
import com.jidong.productselection.entity.JdComponent;
import com.jidong.productselection.entity.JdProduct;
import com.jidong.productselection.request.CategoryAddRequest;
import com.jidong.productselection.service.JdCategoryService;
import com.jidong.productselection.service.JdConstraintService;
import com.jidong.productselection.util.ConstraintUtil;
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

	@Autowired
	private JdMutexDescribeMapper mutexDescribeMapper;

	@Autowired
	private ConstraintUtil constraintUtil;

	@Autowired
	private JdConstraintService constraintService;

	@Override
	@Deprecated
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
	@Deprecated
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
				.setIsShow(categoryAddRequest.getIsShow())
				.setIsDeleted(false);
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
			if (categoryAddRequest.getCategoryOrder() != null) {
				List<Integer> orders = categoryMapper.findByProductIdAndParentId(productId, TOP_LEVEL).stream().map(JdCategory::getCategoryOrder).collect(Collectors.toList());
				if (!orders.contains(categoryAddRequest.getCategoryOrder())) {
					category.setCategoryOrder(categoryAddRequest.getCategoryOrder());
				} else {
					throw new RuntimeException("已存在序号");
				}
			}
			Integer categoryId = category.getCategoryId();
			JdProduct product = productMapper.selectByPrimaryKey(productId);
			if (categoryAddRequest.getIsMainCate()) {
				product.setMainCateid(categoryId);
			} else if (categoryAddRequest.getIsInstallation()) {
				product.setInstallationId(categoryId);
			} else if (categoryAddRequest.getIsShelf()) {
				product.setShelfId(categoryId);
			} else if (categoryAddRequest.getIsVoltage()) {
				product.setVoltageId(categoryId);
			}
			productMapper.updateByPrimaryKeySelective(product);
		}
		int num = categoryMapper.insert(category);
		//List<JdMutexDescribe> onlyUsedConstraints = mutexDescribeMapper.findByProductIdAndConstraintType(productId, ConstraintOperationEnum.ONLY_BE_USED.getCode());
		//List<JdMutexDescribe> needRegenerateList = onlyUsedConstraints.stream().filter(ele -> constraintUtil.needReGenerate(category, ele)).collect(Collectors.toList());
		//constraintService.regenerate(needRegenerateList);
		return num;
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

	//递归
	@Override
	public List<JdCategory> getAllLeafCategory(Integer categoryId) {
		List<JdCategory> allLeafCategory = new ArrayList<>();
		JdCategory category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category.getIsLeaf()) {
			allLeafCategory.add(category);
			return allLeafCategory;
		}
		List<JdCategory> childrenCate = categoryMapper.findByParentId(categoryId);
		for (JdCategory cate : childrenCate) {
			allLeafCategory.addAll(getAllLeafCategory(cate.getCategoryId()));
		}
		return allLeafCategory;
//		if (category.getIsLeaf()) {
//			allLeafCategory.add(category);
//		} else {
//			for (JdCategory subCategory : categoryMapper.findByParentId(categoryId)) {
//				if (subCategory.getIsLeaf()) {
//					allLeafCategory.add(subCategory);
//				} else {
//					for (JdCategory subSubCategory : categoryMapper.findByParentId(subCategory.getCategoryId())) {
//						if (subSubCategory.getIsLeaf()) {
//							allLeafCategory.add(subSubCategory);
//						} else {
//							for (JdCategory subSubSubCategory : categoryMapper.findByParentId(subSubCategory.getCategoryId())) {
//								if (subSubSubCategory.getIsLeaf()) {
//									allLeafCategory.add(subSubCategory);
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		return allLeafCategory;
	}

	@Override
	public List<JdCategory> getNewMenuTree(Integer prdId, Integer parentId) {
		return categoryMapper.getNewMenuTree(prdId, parentId);
	}

	@Override
	public List<JdCategory> getAllNewMenuTree(Integer prdId, Integer parentId) {
		return categoryMapper.getAllNewMenuTree(prdId, parentId);
	}

	/**
	 * 返回该分类下所有子分类，不包括该分类
	 *
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
				if (!secondCategory.getIsLeaf()) {
					List<JdCategory> thirdList = categoryMapper.findByParentId(secondCategory.getCategoryId());
					if (thirdList != null && thirdList.size() > 0) {
						res.addAll(thirdList);
						for (JdCategory thirdCategory : thirdList) {
							if (!thirdCategory.getIsLeaf()) {
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

	/**
	 * 把组件按照最后一级分类分成不同的分组
	 *
	 * @param jdComponents
	 * @return
	 */
	@Override
	public Map<Integer, List<JdComponent>> partitionComp(List<JdComponent> jdComponents) {
		Map<Integer, List<JdComponent>> partitionCompMap = new HashMap<>();
		for (JdComponent component : jdComponents) {
			if (partitionCompMap.containsKey(component.getLastCategoryId())) {
				List<JdComponent> componentList = partitionCompMap.get(component.getLastCategoryId());
				componentList.add(component);
			} else {
				partitionCompMap.put(component.getLastCategoryId(), new ArrayList<>(Collections.singleton(component)));
			}
		}
		return partitionCompMap;
	}

	/**
	 * 把类别按照顶级分类分成不同的分组
	 *
	 * @param jdCategories
	 * @return
	 */
	@Override
	public Map<Integer, List<JdCategory>> partitionCate(List<JdCategory> jdCategories) {
		Map<Integer, List<JdCategory>> partitionCateMap = new HashMap<>();
		for (JdCategory category : jdCategories) {
			if (category.getParentId().equals(TOP_LEVEL)) {
				partitionCateMap.put(category.getCategoryId(), new ArrayList<>(Collections.singleton(category)));
			} else {
				JdCategory topCate = getTopCate(category);
				if (partitionCateMap.containsKey(topCate.getCategoryId())) {
					List<JdCategory> categoryList = partitionCateMap.get(topCate.getCategoryId());
					categoryList.add(category);
				} else {
					partitionCateMap.put(topCate.getCategoryId(), new ArrayList<>(Collections.singleton(category)));
				}
			}
		}
		return partitionCateMap;
	}

	/**
	 * 此方法和groupCate方法为互斥时分组所用
	 * eg:类型 AB C 部件 d 互斥于 类型 E 部件 fg
	 * 则变成 ACE BCE df dg
	 * 改成递归吧!!!
	 *
	 * @param compListMap
	 * @return
	 */
	private List<List<JdComponent>> groupCompRes = new ArrayList<>();
	@Override
	public List<List<JdComponent>> groupComp(Map<Integer, List<JdComponent>> compListMap) {
		List<List<JdComponent>> temp = new ArrayList<>();
		groupCompRes.clear();
		compListMap.forEach((key, compList) -> temp.add(compList));
		if (!temp.isEmpty()){
			groupCompRecursion(temp,0,new ArrayList<>());
		}
//		for (Map.Entry<Integer, List<JdComponent>> entry : compListMap.entrySet()) {
//			int times = entry.getValue().size();
//			if (times == 1) {
//				if (res.size() > 0) {
//					for (List<JdComponent> re : res) {
//						re.add(entry.getValue().get(0));
//					}
//				} else {
//					res.add(new ArrayList<>(Collections.singletonList(entry.getValue().get(0))));
//				}
//			} else {
//				if (res.size() > 0) {
//					ArrayList<List<JdComponent>> tempRes = new ArrayList<>(res);
//					res.clear();
//					for (List<JdComponent> tempRe : tempRes) {
//						for (JdComponent component : entry.getValue()) {
//							List<JdComponent> temp = new ArrayList<>(tempRe);
//							temp.add(component);
//							res.add(temp);
//						}
//					}
//				} else {
//					for (JdComponent component : entry.getValue()) {
//						res.add(new ArrayList<>(Collections.singletonList(component)));
//					}
//				}
//			}
//		}
		return groupCompRes;
	}

	private void groupCompRecursion(List<List<JdComponent>> compListList, int index, List<JdComponent> tempList) {
		if (compListList.size() == index){
			groupCompRes.add(tempList);
			return;
		}
		for (JdComponent comp : compListList.get(index)) {
			tempList.add(comp);
			groupCompRecursion(compListList,index + 1,tempList);
		}
	}

	private List<List<JdCategory>> groupCateRes = new ArrayList<>();
	@Override
	public List<List<JdCategory>> groupCate(Map<Integer, List<JdCategory>> cateListMap) {
		List<List<JdCategory>> temp = new ArrayList<>();
		groupCateRes.clear();
		cateListMap.forEach((key, compList) -> temp.add(compList));
		if (!temp.isEmpty()){
			groupCateRecursion(temp,0,new ArrayList<>());
		}

//		for (Map.Entry<Integer, List<JdCategory>> entry : cateListMap.entrySet()) {
//			int times = entry.getValue().size();
//			if (times == 1) {
//				if (res.size() > 0) {
//					for (List<JdCategory> re : res) {
//						re.add(entry.getValue().get(0));
//					}
//				} else {
//					res.add(new ArrayList<>(Collections.singletonList(entry.getValue().get(0))));
//				}
//			} else {
//				if (res.size() > 0) {
//					ArrayList<List<JdCategory>> tempRes = new ArrayList<>(res);
//					res.clear();
//					for (List<JdCategory> tempRe : tempRes) {
//						for (JdCategory category : entry.getValue()) {
//							List<JdCategory> temp = new ArrayList<>(tempRe);
//							temp.add(category);
//							res.add(temp);
//						}
//					}
//				} else {
//					for (JdCategory category : entry.getValue()) {
//						res.add(new ArrayList<>(Collections.singletonList(category)));
//					}
//				}
//			}
//		}
		return groupCateRes;
	}

	private void groupCateRecursion(List<List<JdCategory>> cateListList, int index, List<JdCategory> tempList) {
		if (cateListList.size() == index){
			groupCateRes.add(tempList);
			return;
		}
		for (JdCategory cate : cateListList.get(index)) {
			tempList.add(cate);
			groupCateRecursion(cateListList,index + 1,tempList);
		}
	}


	private JdCategory getTopCate(JdCategory category) {
		JdCategory cate = categoryMapper.selectByPrimaryKey(category.getParentId());
		while (!cate.getParentId().equals(TOP_LEVEL)) {
			cate = categoryMapper.selectByPrimaryKey(cate.getParentId());
		}
		return cate;
	}
}
