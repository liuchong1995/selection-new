package com.jidong.productselection.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jidong.productselection.dao.*;
import com.jidong.productselection.dto.*;
import com.jidong.productselection.entity.*;
import com.jidong.productselection.enums.ComponentTypeEnum;
import com.jidong.productselection.enums.ConstraintOperationEnum;
import com.jidong.productselection.request.ConstraintRequest;
import com.jidong.productselection.request.ConstraintSearchRequest;
import com.jidong.productselection.service.JdCategoryService;
import com.jidong.productselection.service.JdComponentService;
import com.jidong.productselection.service.JdConstraintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: LiuChong
 * @Date: 2018/10/25 15:59
 * @Description:
 */
@Service
public class JdConstraintServiceUniMutexImpl implements JdConstraintService {

	private static final Integer TOP_LEVEL = Integer.valueOf(0);

	private static final String PREMISE = "( 约束前提:";

	private static final String CONSTRAINT_TYPE = ";约束关系:";

	private static final String RESULT = ";约束结果:";

	private static final String SPACE = " ";

	@Autowired
	private JdComponentMapper componentMapper;

	@Autowired
	private JdCategoryMapper categoryMapper;

	@Autowired
	private JdUniMutexMapper uniMutexMapper;

	@Autowired
	private JdCategoryService categoryService;

	@Autowired
	private JdComponentService componentService;

	@Autowired
	private JdMutexDescribeMapper mutexDescribeMapper;

	@Autowired
	private JdAttachmentMapper attachmentMapper;

	@Autowired
	private JdMandatoryMapper mandatoryMapper;

	@Autowired
	private JdShelfConstraintMapper shelfConstraintMapper;

	@Autowired
	private JdProductMapper productMapper;

	@Override
	public String refactorMenuTree(Integer productId, List<JdComponent> selectedList) {
		List<JdCategory> banCateGoryList = new ArrayList<>(getBanCategoryList(productId, selectedList));
		List<JdCategory> categoryList = categoryMapper.findByProductId(productId);
		categoryList.removeAll(banCateGoryList);
		categoryList = categoryList.stream().filter(JdCategory::getIsShow).collect(Collectors.toList());
		List<Map<Object, Object>> forTree = new ArrayList<>();
		for (JdCategory category : categoryList) {
			Map<Object, Object> map = new HashMap<>();
			map.put("id", category.getCategoryId());
			map.put("text", category.getCategoryName());
			map.put("parentid", category.getParentId());
			map.put("level", category.getCategoryLevel());
			map.put("isLeaf", category.getIsLeaf());
			map.put("isShow", category.getIsShow());
			forTree.add(map);
		}
		return categoryService.getTreeList(forTree);
	}

	@Override
	public List<JdComponent> getOptionalListBySelected(Integer categoryId, List<JdComponent> selectedList) {
		JdCategory selectedCategory = categoryMapper.selectByPrimaryKey(categoryId);
		List<JdCategory> banCateGoryList = new ArrayList<>(getBanCategoryList(selectedCategory.getProductId(), selectedList));
		List<JdCategory> allLeafCategory = categoryService.getAllLeafCategory(categoryId);
		List<Integer> allComponentIds = new ArrayList<>();
		for (JdCategory category : allLeafCategory) {
			allComponentIds.addAll(JSON.parseArray(category.getComponentsId(), Integer.class));
		}
		if (allComponentIds.size() == 0) {
			return new ArrayList<>();
		}
		List<JdComponent> componentList = componentMapper.findByComponentIdIn(allComponentIds);
		List<JdComponent> banComponentList = new ArrayList<>(getBanComponentList(selectedCategory.getProductId(), selectedList));
		componentList.removeAll(banComponentList);
		List<JdComponent> finalOptionList = componentList.stream().filter(comp -> !comp.getComponentType().equals(ComponentTypeEnum.ATTACHMENT.getCode()) && !isMutex(banCateGoryList, comp)).collect(Collectors.toList());
		return finalOptionList;
	}

	private Boolean isMutex(List<JdCategory> banCateGoryList, JdComponent component) {
		List<JdCategory> categoryList = componentService.getCategoryList(component);
		categoryList.retainAll(banCateGoryList);
		return categoryList.size() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public List<JdComponent> findOrderComponents(List<Integer> componentIds) {
		return componentMapper.findByComponentIdIn(componentIds);
	}

	@Override
	@Transactional
	public int insertConstraint(ConstraintRequest constraintRequest) {
		List<Integer> mutexIds = insertMutexes(constraintRequest);
		JdMutexDescribe mutexDescribe = new JdMutexDescribe();
		mutexDescribe.setDescribeId(mutexDescribeMapper.findNextDescribeId())
				.setProductId(constraintRequest.getProductId())
				.setConstraintType(constraintRequest.getConstraintOperation())
				.setMutexIds(JSON.toJSONString(mutexIds))
				.setGroupId(constraintRequest.getGroupId())
				.setGroupName(constraintRequest.getGroupName())
				.setIsDeleted(false)
				.setConstraintDesc(writeConstraintDescribe(constraintRequest));
		return mutexDescribeMapper.insert(mutexDescribe);
	}

	@Override
	@Transactional
	public int deleteConstraint(Integer constraintId) {
		JdMutexDescribe mutexDescribe = mutexDescribeMapper.selectByPrimaryKey(constraintId);
		String mutexIds = mutexDescribe.getMutexIds();
		List<Integer> reallyStorageIds = JSON.parseArray(mutexIds, Integer.class);
		if (mutexDescribe.getConstraintType().equals(ConstraintOperationEnum.ONLY_BE_USED.getCode()) || mutexDescribe.getConstraintType().equals(ConstraintOperationEnum.MUTEX.getCode())) {
			uniMutexMapper.deleteByMutexIdIn(reallyStorageIds);
		} else if (mutexDescribe.getConstraintType().equals(ConstraintOperationEnum.ATTACHMENT.getCode())) {
			JdAttachment attachment = attachmentMapper.selectByPrimaryKey(reallyStorageIds.get(0));
			componentMapper.updateComponentTypeByComponentId(ComponentTypeEnum.REALLY_COMPONENT.getCode(), attachment.getAttachmentId());
			attachmentMapper.updateIsDeletedByAttachId(reallyStorageIds.get(0));
		} else if (mutexDescribe.getConstraintType().equals(ConstraintOperationEnum.MANDATORY.getCode())) {
			mandatoryMapper.updateIsDeletedBYMandatoryId(reallyStorageIds.get(0));
		} else if (mutexDescribe.getConstraintType().equals(ConstraintOperationEnum.SHELF_CONSTRAINT.getCode())) {
			shelfConstraintMapper.deleteByPrimaryKey(reallyStorageIds.get(0));
		}
		return mutexDescribeMapper.deleteByDescribeId(constraintId);
	}

	@Override
	public Integer getMaxGroupId() {
		return mutexDescribeMapper.findMaxGroupId();
	}

	@Override
	public PageInfo<JdMutexDescribe> findAll(int page, int rows) {
		PageHelper.startPage(page, rows);
		List<JdMutexDescribe> mutexDescribeList = mutexDescribeMapper.findByIsDeletedFalse();
		PageInfo<JdMutexDescribe> mutexDescribePage = new PageInfo<>(mutexDescribeList);
		return mutexDescribePage;
	}

	@Override
	@Transactional
	public void deleteConstraints(List<Integer> constraintIds) {
//		List<JdMutexDescribe> mutexDescribeList = mutexDescribeMapper.findByDescribeIdIn(constraintIds);
//		List<Integer> mutexIds = new ArrayList<>();
//		for (JdMutexDescribe mutexDescribe : mutexDescribeList) {
//			mutexIds.addAll(JSON.parseArray(mutexDescribe.getMutexIds(),Integer.class));
//		}
//		uniMutexMapper.deleteByMutexIdIn(mutexIds);
//		mutexDescribeMapper.deleteByDescribeIdIn(constraintIds);
		for (Integer constraintId : constraintIds) {
			deleteConstraint(constraintId);
		}
	}

	private static final String SHELF_CONSTRAINT = "架子约束";
	private static final String GREATER = "架子高度大于安装高度";
	private static final String EQUAL = "架子高度等于安装高度";
	private static final String LESS = "架子高度小于安装高度";

	private static final String MILLIMETER = "mm";

	private static final Integer GREATER_CODE = 1;
	private static final Integer EQUAL_CODE = 2;


	@Override
	@Transactional
	public int addShelfConstraint(JdShelfConstraint shelfConstraint) {
		shelfConstraint.setConstraintId(shelfConstraintMapper.findNestConstraintId());
		if (!shelfConstraint.getRelation().equals(EQUAL_CODE) && shelfConstraint.getRelationValue() == null) {
			shelfConstraint.setRelationValue(0);
		}
		String productName = productMapper.selectByPrimaryKey(shelfConstraint.getProductId()).getProductName();
		String installationName = componentMapper.selectByPrimaryKey(shelfConstraint.getInstallation()).getComponentModelNumber();
		String relation = shelfConstraint.getRelation().equals(GREATER_CODE) ? GREATER : shelfConstraint.getRelation().equals(EQUAL_CODE) ? EQUAL : LESS;
		StringBuilder constraintDescribe = new StringBuilder();
		constraintDescribe.append(productName).append(SPACE).append(installationName).append(SPACE).append(SPACE).append(SPACE).append(relation).append(SPACE);
		if (shelfConstraint.getRelationValue() != null) {
			constraintDescribe.append(shelfConstraint.getRelationValue()).append(MILLIMETER);
		}
		JdMutexDescribe mutexDescribe = new JdMutexDescribe();
		mutexDescribe.setDescribeId(mutexDescribeMapper.findNextDescribeId())
				.setIsDeleted(false)
				.setGroupName(SHELF_CONSTRAINT)
				.setConstraintType(ConstraintOperationEnum.SHELF_CONSTRAINT.getCode())
				.setProductId(shelfConstraint.getProductId())
				.setGroupId(mutexDescribeMapper.findMaxGroupId() + 1)
				.setMutexIds(JSON.toJSONString(Collections.singletonList(shelfConstraint.getConstraintId())))
				.setConstraintDesc(constraintDescribe.toString());
		mutexDescribeMapper.insert(mutexDescribe);
		return shelfConstraintMapper.insert(shelfConstraint);
	}

	@Override
	public PageInfo<JdMutexDescribe> search(ConstraintSearchRequest searchRequest) {
		if (searchRequest.getProductId() == null) {
			return findAll(searchRequest.getPage(), searchRequest.getRows());
		} else {
			PageHelper.startPage(searchRequest.getPage(), searchRequest.getRows());
			ConstraintSearchCondition searchCondition = new ConstraintSearchCondition();
			if (searchRequest.getComponentId() != null) {
				searchCondition
						.setComponentModelNumber(componentMapper.selectByPrimaryKey(searchRequest.getComponentId()).getComponentModelNumber());
			} else if (searchRequest.getCategoryIds() != null && searchRequest.getCategoryIds().size() > 0) {
				searchCondition
						.setCategoryName(categoryMapper.selectByPrimaryKey(searchRequest.getCategoryIds().get(searchRequest.getCategoryIds().size() - 1)).getCategoryName());
			} else {
				return new PageInfo<>(mutexDescribeMapper.findByProductId(searchRequest.getProductId()));
			}
			String productName = productMapper.selectByPrimaryKey(searchRequest.getProductId()).getProductName();
			searchCondition.setProductName(productName);
			List<JdMutexDescribe> result = mutexDescribeMapper.findByConstraintSearchCondition(searchCondition);
			return new PageInfo<>(result);

		}
	}


	private String writeConstraintDescribe(ConstraintRequest constraintRequest) {
		StringBuilder constraintDescribe = new StringBuilder();
		constraintDescribe.append(constraintRequest.getConstraintDesc()).append(PREMISE);
		for (JdCategory category : constraintRequest.getConstraintPremise().getCategories()) {
			constraintDescribe.append(category.getCategoryName()).append(SPACE);
		}
		for (JdComponent component : constraintRequest.getConstraintPremise().getComponents()) {
			constraintDescribe.append(component.getComponentModelNumber()).append(SPACE);
		}
		String constrainOperation = constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.ONLY_BE_USED.getCode()) ? ConstraintOperationEnum.ONLY_BE_USED.getMessage()
				: constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.MUTEX.getCode()) ? ConstraintOperationEnum.MUTEX.getMessage()
				: constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.ATTACHMENT.getCode()) ? ConstraintOperationEnum.ATTACHMENT.getMessage() : ConstraintOperationEnum.MANDATORY.getMessage();
		constraintDescribe.append(CONSTRAINT_TYPE).append(constrainOperation).append(SPACE).append(RESULT);
		for (JdCategory category : constraintRequest.getConstraintResult().getCategories()) {
			constraintDescribe.append(category.getCategoryName()).append(SPACE);
		}
		for (JdComponent component : constraintRequest.getConstraintResult().getComponents()) {
			constraintDescribe.append(component.getComponentModelNumber()).append(SPACE);
		}
		constraintDescribe.append(")");
		return constraintDescribe.toString();
	}

	/**
	 * 获得插入互斥表中的id列表
	 *
	 * @param constraintRequest
	 * @return
	 */
	private List<Integer> insertMutexes(ConstraintRequest constraintRequest) {
		List<Integer> mutexIds = new ArrayList<>();
		ConstraintPremise constraintPremise = constraintRequest.getConstraintPremise();
		ConstraintResult constraintResult = constraintRequest.getConstraintResult();
		List<JdCategory> premiseCategories = constraintPremise.getCategories();
		List<JdComponent> premiseComponents = constraintPremise.getComponents();
		List<JdCategory> resultCategories = constraintResult.getCategories();
		List<JdComponent> resultComponents = constraintResult.getComponents();
		List<Integer> allPremiseCategoryIds = new ArrayList<>();
		List<Integer> allPremiseComponentIds = new ArrayList<>();
		List<Integer> allResultCategoryIds = new ArrayList<>();
		List<Integer> allResultComponentIds = new ArrayList<>();
		if (constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.ONLY_BE_USED.getCode())) {
			//仅可用时获取反向互斥类型及部件
			Set<Integer> excludeCategoryIds = new HashSet<>();
			Set<Integer> excludeComponentIds = new HashSet<>();
			for (JdComponent resultComponent : resultComponents) {
				JdCategory lastCategory = categoryMapper.selectByPrimaryKey(resultComponent.getLastCategoryId());
				List<Integer> tempExcludeCategoryIds = categoryService.excludeAllCategory(lastCategory);
				excludeCategoryIds.addAll(tempExcludeCategoryIds);
				List<Integer> tempExcludeComponentIds = componentService.excludePeerComponent(resultComponent, tempExcludeCategoryIds);
				excludeComponentIds.addAll(tempExcludeComponentIds);
			}
			for (JdCategory resultCategory : resultCategories) {
				List<Integer> tempExcludeCategoryIds = categoryService.excludeAllCategory(resultCategory);
				excludeCategoryIds.addAll(tempExcludeCategoryIds);
			}
			allResultCategoryIds.addAll(excludeCategoryIds);
			allResultComponentIds.addAll(excludeComponentIds);
			for (JdCategory category : premiseCategories) {
				allPremiseCategoryIds.add(category.getCategoryId());
			}
			for (JdComponent premiseComponent : premiseComponents) {
				allPremiseComponentIds.add(premiseComponent.getComponentId());
			}
			JdUniMutex uniMutex = new JdUniMutex();
			Integer nextMutexId = uniMutexMapper.findNextMutexId();
			MixPremiseOrResultIds mixPremiseIds = new MixPremiseOrResultIds();
			MixPremiseOrResultIds mixResultIds = new MixPremiseOrResultIds();
			mixPremiseIds.setCategoryIds(allPremiseCategoryIds);
			mixPremiseIds.setComponentIds(allPremiseComponentIds);
			mixResultIds.setCategoryIds(allResultCategoryIds);
			mixResultIds.setComponentIds(allResultComponentIds);
			uniMutex.setMutexId(nextMutexId)
					.setProductId(constraintRequest.getProductId())
					.setMutexPremise(JSON.toJSONString(mixPremiseIds))
					.setMutextResult(JSON.toJSONString(mixResultIds))
					.setIsDeleted(false);
			uniMutexMapper.insert(uniMutex);
			mutexIds.add(uniMutex.getMutexId());
			//反方向存储
			//1.类型反向
			for (Integer resultCategoryId : allResultCategoryIds) {
				//1.1前提类型反向循环
				for (Integer premiseCategoryId : allPremiseCategoryIds) {
					List<Integer> subPremiseCategoryIds = new ArrayList<>(allPremiseCategoryIds);
					subPremiseCategoryIds.remove(premiseCategoryId);
					subPremiseCategoryIds.add(resultCategoryId);
					List<Integer> subPremiseComponentIds = new ArrayList<>(allPremiseComponentIds);
					MixPremiseOrResultIds subMixPremiseIds = new MixPremiseOrResultIds();
					MixPremiseOrResultIds subMixResultIds = new MixPremiseOrResultIds();
					subMixPremiseIds.setCategoryIds(subPremiseCategoryIds);
					subMixPremiseIds.setComponentIds(subPremiseComponentIds);
					subMixResultIds.setCategoryIds(Collections.singletonList(premiseCategoryId));
					subMixResultIds.setComponentIds(Collections.emptyList());
					JdUniMutex subUniMutex = new JdUniMutex();
					Integer subNextMutexId = uniMutexMapper.findNextMutexId();
					subUniMutex.setMutexId(subNextMutexId)
							.setProductId(constraintRequest.getProductId())
							.setMutexPremise(JSON.toJSONString(subMixPremiseIds))
							.setMutextResult(JSON.toJSONString(subMixResultIds))
							.setIsDeleted(false);
					uniMutexMapper.insert(subUniMutex);
					mutexIds.add(subUniMutex.getMutexId());
				}
				//1.2前提部件反向循环
				for (Integer premiseComponentId : allPremiseComponentIds) {
					List<Integer> subPremiseCategoryIds = new ArrayList<>(allPremiseCategoryIds);
					subPremiseCategoryIds.add(resultCategoryId);
					List<Integer> subPremiseComponentIds = new ArrayList<>(allPremiseComponentIds);
					subPremiseComponentIds.remove(premiseComponentId);
					MixPremiseOrResultIds subMixPremiseIds = new MixPremiseOrResultIds();
					MixPremiseOrResultIds subMixResultIds = new MixPremiseOrResultIds();
					subMixPremiseIds.setCategoryIds(subPremiseCategoryIds);
					subMixPremiseIds.setComponentIds(subPremiseComponentIds);
					subMixResultIds.setCategoryIds(Collections.emptyList());
					subMixResultIds.setComponentIds(Collections.singletonList(premiseComponentId));
					JdUniMutex subUniMutex = new JdUniMutex();
					Integer subNextMutexId = uniMutexMapper.findNextMutexId();
					subUniMutex.setMutexId(subNextMutexId)
							.setProductId(constraintRequest.getProductId())
							.setMutexPremise(JSON.toJSONString(subMixPremiseIds))
							.setMutextResult(JSON.toJSONString(subMixResultIds))
							.setIsDeleted(false);
					uniMutexMapper.insert(subUniMutex);
					mutexIds.add(subUniMutex.getMutexId());
				}
			}
			//反方向存储
			//2.部件反向
			for (Integer resultComponentId : allResultComponentIds) {
				//1.1前提类型反向循环
				for (Integer premiseCategoryId : allPremiseCategoryIds) {
					List<Integer> subPremiseCategoryIds = new ArrayList<>(allPremiseCategoryIds);
					subPremiseCategoryIds.remove(premiseCategoryId);
					List<Integer> subPremiseComponentIds = new ArrayList<>(allPremiseComponentIds);
					subPremiseComponentIds.add(resultComponentId);
					MixPremiseOrResultIds subMixPremiseIds = new MixPremiseOrResultIds();
					MixPremiseOrResultIds subMixResultIds = new MixPremiseOrResultIds();
					subMixPremiseIds.setCategoryIds(subPremiseCategoryIds);
					subMixPremiseIds.setComponentIds(subPremiseComponentIds);
					subMixResultIds.setCategoryIds(Collections.singletonList(premiseCategoryId));
					subMixResultIds.setComponentIds(Collections.emptyList());
					JdUniMutex subUniMutex = new JdUniMutex();
					Integer subNextMutexId = uniMutexMapper.findNextMutexId();
					subUniMutex.setMutexId(subNextMutexId)
							.setProductId(constraintRequest.getProductId())
							.setMutexPremise(JSON.toJSONString(subMixPremiseIds))
							.setMutextResult(JSON.toJSONString(subMixResultIds))
							.setIsDeleted(false);
					uniMutexMapper.insert(subUniMutex);
					mutexIds.add(subUniMutex.getMutexId());
				}
				//1.2前提部件反向循环
				for (Integer premiseComponentId : allPremiseComponentIds) {
					List<Integer> subPremiseCategoryIds = new ArrayList<>(allPremiseCategoryIds);
					List<Integer> subPremiseComponentIds = new ArrayList<>(allPremiseComponentIds);
					subPremiseComponentIds.add(resultComponentId);
					subPremiseComponentIds.remove(premiseComponentId);
					MixPremiseOrResultIds subMixPremiseIds = new MixPremiseOrResultIds();
					MixPremiseOrResultIds subMixResultIds = new MixPremiseOrResultIds();
					subMixPremiseIds.setCategoryIds(subPremiseCategoryIds);
					subMixPremiseIds.setComponentIds(subPremiseComponentIds);
					subMixResultIds.setCategoryIds(Collections.emptyList());
					subMixResultIds.setComponentIds(Collections.singletonList(premiseComponentId));
					JdUniMutex subUniMutex = new JdUniMutex();
					Integer subNextMutexId = uniMutexMapper.findNextMutexId();
					subUniMutex.setMutexId(subNextMutexId)
							.setProductId(constraintRequest.getProductId())
							.setMutexPremise(JSON.toJSONString(subMixPremiseIds))
							.setMutextResult(JSON.toJSONString(subMixResultIds))
							.setIsDeleted(false);
					uniMutexMapper.insert(subUniMutex);
					mutexIds.add(subUniMutex.getMutexId());
				}
			}
		} else if (constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.MUTEX.getCode())) {
			for (JdCategory category : premiseCategories) {
				allPremiseCategoryIds.add(category.getCategoryId());
			}
			for (JdCategory category : resultCategories) {
				allResultCategoryIds.add(category.getCategoryId());
			}
			for (JdComponent premiseComponent : premiseComponents) {
				allPremiseComponentIds.add(premiseComponent.getComponentId());
			}
			for (JdComponent premiseComponent : resultComponents) {
				allResultComponentIds.add(premiseComponent.getComponentId());
			}
			//逻辑貌似错了...统一为两个部件和分类 两个遍历试试
			List<Integer> allMutexCategoryIds = new ArrayList<>(allPremiseCategoryIds);
			allMutexCategoryIds.addAll(allResultCategoryIds);
			List<Integer> allMutexComponentIds = new ArrayList<>(allPremiseComponentIds);
			allMutexComponentIds.addAll(allResultComponentIds);

			for (Integer mutexCategoryId : allMutexCategoryIds) {
				JdUniMutex uniMutex = new JdUniMutex();
				Integer nextMutexId = uniMutexMapper.findNextMutexId();
				MixPremiseOrResultIds mixPremiseIds = new MixPremiseOrResultIds();
				MixPremiseOrResultIds mixResultIds = new MixPremiseOrResultIds();
				List<Integer> tempAllMutexCategoryIds = new ArrayList<>(allMutexCategoryIds);
				tempAllMutexCategoryIds.remove(mutexCategoryId);
				mixPremiseIds.setCategoryIds(tempAllMutexCategoryIds);
				mixPremiseIds.setComponentIds(allMutexComponentIds);
				mixResultIds.setCategoryIds(Collections.singletonList(mutexCategoryId));
				mixResultIds.setComponentIds(Collections.emptyList());
				uniMutex.setMutexId(nextMutexId)
						.setProductId(constraintRequest.getProductId())
						.setMutexPremise(JSON.toJSONString(mixPremiseIds))
						.setMutextResult(JSON.toJSONString(mixResultIds))
						.setIsDeleted(false);
				uniMutexMapper.insert(uniMutex);
				mutexIds.add(uniMutex.getMutexId());
			}

			for (Integer mutexComponentId : allMutexComponentIds) {
				JdUniMutex uniMutex = new JdUniMutex();
				Integer nextMutexId = uniMutexMapper.findNextMutexId();
				MixPremiseOrResultIds mixPremiseIds = new MixPremiseOrResultIds();
				MixPremiseOrResultIds mixResultIds = new MixPremiseOrResultIds();
				List<Integer> tempAllMutexComponentIds = new ArrayList<>(allMutexComponentIds);
				tempAllMutexComponentIds.remove(mutexComponentId);
				mixPremiseIds.setCategoryIds(allMutexCategoryIds);
				mixPremiseIds.setComponentIds(tempAllMutexComponentIds);
				mixResultIds.setCategoryIds(Collections.emptyList());
				mixResultIds.setComponentIds(Collections.singletonList(mutexComponentId));
				uniMutex.setMutexId(nextMutexId)
						.setProductId(constraintRequest.getProductId())
						.setMutexPremise(JSON.toJSONString(mixPremiseIds))
						.setMutextResult(JSON.toJSONString(mixResultIds))
						.setIsDeleted(false);
				uniMutexMapper.insert(uniMutex);
				mutexIds.add(uniMutex.getMutexId());
			}
		}
		//附件关系时单独处理
		else if (constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.ATTACHMENT.getCode())) {
			Integer attachmentId = constraintRequest.getConstraintResult().getComponents().get(0).getComponentId();
			componentMapper.updateComponentTypeByComponentId(ComponentTypeEnum.ATTACHMENT.getCode(), attachmentId);
			JdAttachment attachment = new JdAttachment();
			attachment.setAttachId(attachmentMapper.findNextAttachId());
			attachment.setProductId(constraintRequest.getProductId());
			attachment.setMasterId(constraintRequest.getConstraintPremise().getComponents().get(0).getComponentId());
			attachment.setAttachmentId(attachmentId);
			attachment.setIsDeleted(false);
			attachmentMapper.insert(attachment);
			return Collections.singletonList(attachment.getAttachId());
		} else if (constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.MANDATORY.getCode())) {
			JdMandatory mandatory = new JdMandatory();
			if (premiseCategories.size() != 0) {
				mandatory.setPremiseCategoryId(premiseCategories.get(0).getCategoryId());
			} else if (premiseComponents.size() != 0) {
				mandatory.setPremiseComponentId(premiseComponents.get(0).getComponentId());
			} else {
				mandatory.setPremiseProductId(constraintRequest.getProductId());
			}
			mandatory.setMandatoryId(mandatoryMapper.findNextMandatoryId());
			if (resultCategories.size() != 0) {
				mandatory.setResultCategoryId(resultCategories.get(0).getCategoryId());
			} else if (resultComponents.size() != 0) {
				mandatory.setResultComponentId(resultComponents.get(0).getComponentId());
			}
			mandatory.setIsDeleted(false);
			mandatoryMapper.insertSelective(mandatory);
			return Collections.singletonList(mandatory.getMandatoryId());
		}
		return mutexIds;
	}


	/**
	 * 获取禁用类型
	 *
	 * @param
	 * @param
	 * @return
	 */
	@Override
	public List<JdCategory> getBanCategoryList(Integer productId, List<JdComponent> selectedComponents) {
		List<JdCategory> banCategoryList = new ArrayList<>();
		List<JdCategory> selectedCategories = getCategories(selectedComponents);
		List<Integer> categoryIds = new ArrayList<>();
		for (JdCategory category : selectedCategories) {
			categoryIds.add(category.getCategoryId());
		}
		List<Integer> componentIds = new ArrayList<>();
		for (JdComponent component : selectedComponents) {
			componentIds.add(component.getComponentId());
		}
		List<JdUniMutex> uniMutexList = uniMutexMapper.findByProductId(productId);
		if (uniMutexList != null && uniMutexList.size() > 0) {
			for (JdUniMutex uniMutex : uniMutexList) {
				if (containBan(categoryIds, componentIds, uniMutex.getMutexPremise())) {
					MixPremiseOrResult banResultFromMix = getBanResultFromMix(uniMutex.getMutextResult());
					banCategoryList.addAll(banResultFromMix.getCategories());
				}
			}
		}
		return banCategoryList;
	}

	/**
	 * 获取禁用部件
	 *
	 * @param
	 * @param selectedComponents
	 * @return
	 */
	@Override
	public List<JdComponent> getBanComponentList(Integer productId, List<JdComponent> selectedComponents) {
		List<JdComponent> banComponentList = new ArrayList<>();
		List<JdCategory> selectedCategories = getCategories(selectedComponents);
		List<Integer> categoryIds = new ArrayList<>();
		for (JdCategory category : selectedCategories) {
			categoryIds.add(category.getCategoryId());
		}
		List<Integer> componentIds = new ArrayList<>();
		for (JdComponent component : selectedComponents) {
			componentIds.add(component.getComponentId());
		}
		List<JdUniMutex> uniMutexList = uniMutexMapper.findByProductId(productId);
		if (uniMutexList != null && uniMutexList.size() > 0) {
			for (JdUniMutex uniMutex : uniMutexList) {
				if (containBan(categoryIds, componentIds, uniMutex.getMutexPremise())) {
					MixPremiseOrResult banResultFromMix = getBanResultFromMix(uniMutex.getMutextResult());
					banComponentList.addAll(banResultFromMix.getComponents());
				}
			}
		}
		return banComponentList;
	}

	/**
	 * 判断是否包含禁用
	 *
	 * @param selectedCategoryIds
	 * @param selectedComponentIds
	 * @param mixPremiseString
	 * @return
	 */
	private boolean containBan(List<Integer> selectedCategoryIds, List<Integer> selectedComponentIds, String
			mixPremiseString) {
		MixPremiseOrResultIds mixPremiseOrResultIds = getMixPremiseOrResultIdsFromMix(mixPremiseString);
		List<Integer> premiseCategoryIds = mixPremiseOrResultIds.getCategoryIds();
		List<Integer> premiseComponentIds = mixPremiseOrResultIds.getComponentIds();
		if (premiseCategoryIds.size() > 0) {
			if (selectedCategoryIds.containsAll(premiseCategoryIds)) {
				if (premiseComponentIds.size() > 0) {
					return selectedComponentIds.containsAll(premiseComponentIds);
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return selectedComponentIds.containsAll(premiseComponentIds);
		}
	}

	private MixPremiseOrResult getBanResultFromMix(String mixPremiseString) {
		MixPremiseOrResultIds mixPremiseOrResultIds = getMixPremiseOrResultIdsFromMix(mixPremiseString);
		List<Integer> categoryIds = mixPremiseOrResultIds.getCategoryIds();
		List<Integer> componentIds = mixPremiseOrResultIds.getComponentIds();
		MixPremiseOrResult mixResult = new MixPremiseOrResult();
		List<JdCategory> categories = new ArrayList<>();
		if (categoryIds != null && categoryIds.size() > 0) {
			categories = categoryMapper.findByCategoryIdIn(categoryIds);
			mixResult.setCategories(categories);
		}
		List<JdComponent> components = new ArrayList<>();
		if (componentIds != null && componentIds.size() > 0) {
			components = componentMapper.findByComponentIdIn(componentIds);
			mixResult.setComponents(components);
		}
		mixResult.setCategories(categories);
		mixResult.setComponents(components);
		return mixResult;
	}

	private MixPremiseOrResultIds getMixPremiseOrResultIdsFromMix(String mixPremiseString) {
		return JSON.parseObject(mixPremiseString, MixPremiseOrResultIds.class);
	}

	/**
	 * 根据已选组件列表获取所有类别
	 *
	 * @param selectedList
	 * @return
	 */
	@Override
	public List<JdCategory> getCategories(List<JdComponent> selectedList) {
		List<JdCategory> categories = new ArrayList<>();
		for (JdComponent component : selectedList) {
			JdCategory lastCategory = categoryMapper.selectByPrimaryKey(component.getLastCategoryId());
			categories.add(lastCategory);
			Integer parentId = lastCategory.getParentId();
			while (!parentId.equals(TOP_LEVEL)) {
				JdCategory category = categoryMapper.selectByPrimaryKey(parentId);
				categories.add(category);
				parentId = category.getParentId();
			}
		}
		return categories;
	}

	@Override
	public List<JdCategory> refactorNewMenuTree(Integer productId, List<JdComponent> selectedList) {
		List<JdCategory> banCateGoryList = new ArrayList<>(getBanCategoryList(productId, selectedList));
		List<JdCategory> categoryList = categoryMapper.getNewMenuTree(productId, TOP_LEVEL);
		List<Integer> banCategoryIds = banCateGoryList.stream().map(JdCategory::getCategoryId).collect(Collectors.toList());
		List<JdCategory> tempTopCategoryList = new ArrayList<>(categoryList);
		//顶层过滤
		for (JdCategory topCategory : categoryList) {
			if (banCategoryIds.contains(topCategory.getCategoryId())) {
				tempTopCategoryList.remove(topCategory);
			}
		}
		categoryList = new ArrayList<>(tempTopCategoryList);
		//第二层过滤
		for (JdCategory topCategory : categoryList) {
			List<JdCategory> tempSecondCategoryList = new ArrayList<>(topCategory.getChildren());
			for (JdCategory secondCategory : topCategory.getChildren()) {
				if (banCategoryIds.contains(secondCategory.getCategoryId())) {
					tempSecondCategoryList.remove(secondCategory);
				}
			}
			topCategory.setChildren(tempSecondCategoryList);
		}
		//第三层过滤
		for (JdCategory topCategory : categoryList) {
			for (JdCategory secondCategory : topCategory.getChildren()) {
				List<JdCategory> tempThirdCategoryList = new ArrayList<>(secondCategory.getChildren());
				for (JdCategory thirdCategory : secondCategory.getChildren()) {
					if (banCategoryIds.contains(thirdCategory.getCategoryId())) {
						tempThirdCategoryList.remove(thirdCategory);
					}
				}
				secondCategory.setChildren(tempThirdCategoryList);
			}
		}
		//第四层过滤
		for (JdCategory topCategory : categoryList) {
			for (JdCategory secondCategory : topCategory.getChildren()) {
				for (JdCategory thirdCategory : secondCategory.getChildren()) {
					List<JdCategory> tempForthCategoryList = new ArrayList<>(thirdCategory.getChildren());
					for (JdCategory forthCategory : thirdCategory.getChildren()) {
						if (banCategoryIds.contains(forthCategory.getCategoryId())) {
							tempForthCategoryList.remove(forthCategory);
						}
					}
					thirdCategory.setChildren(tempForthCategoryList);
				}
			}
		}
		return categoryList;
	}
}
