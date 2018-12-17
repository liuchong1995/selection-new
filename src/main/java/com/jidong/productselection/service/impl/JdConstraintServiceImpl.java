package com.jidong.productselection.service.impl;

/**
 * @Author: LiuChong
 * @Date: 2018/10/19 11:12
 * @Description: 重构为统一的互斥模型*/


@Deprecated
public class JdConstraintServiceImpl {

	/*private static final Integer TOP_LEVEL = Integer.valueOf(0);

	@Autowired
	private JdComponentMapper componentMapper;

	@Autowired
	private JdCategoryMapper categoryMapper;

	@Autowired
	private JdMutexMapper mutexMapper;

	@Autowired
	private JdCategoryService categoryService;

	@Autowired
	private JdComponentService componentService;

	@Autowired
	private JdMutexDescribeMapper mutexDescribeMapper;

	public String refactorMenuTree(Integer productId, List<JdComponent> selectedList) {
		List<JdCategory> banCateGoryList = new ArrayList<>();
		banCateGoryList.addAll(getBanCategoryList(productId,selectedList,ConstraintTypeEnum.COMPONENT_TO_CATEGORY));
		banCateGoryList.addAll(getBanCategoryList(productId,selectedList,ConstraintTypeEnum.CATEGORY_TO_CATEGORY));
		banCateGoryList.addAll(getBanCategoryList(productId,selectedList,ConstraintTypeEnum.COMPONENT_TO_MIX));
		banCateGoryList.addAll(getBanCategoryList(productId,selectedList,ConstraintTypeEnum.MIX_TO_CATEGORY));
		banCateGoryList.addAll(getBanCategoryList(productId,selectedList,ConstraintTypeEnum.CATEGORY_TO_MIX));
		banCateGoryList.addAll(getBanCategoryList(productId,selectedList,ConstraintTypeEnum.MIX_TO_MIX));
		List<JdCategory> categoryList = categoryMapper.findByProductId(productId);
		categoryList.removeAll(banCateGoryList);
		List<Map<Object, Object>> forTree = new ArrayList<>();
		for (JdCategory category : categoryList) {
			Map<Object, Object> map = new HashMap<>();
			map.put("id",category.getCategoryId());
			map.put("text",category.getCategoryName());
			map.put("parentid",category.getParentId());
			map.put("level",category.getCategoryLevel());
			map.put("isLeaf",category.getIsLeaf());
			forTree.add(map);
		}
		return categoryService.getTreeList(forTree);
	}

	public List<JdComponent> getOptionalListBySelected(Integer categoryId, List<JdComponent> selectedList) {
		List<JdComponent> banComponentList = new ArrayList<>();
		JdCategory category = categoryMapper.selectByPrimaryKey(categoryId);
		banComponentList.addAll(getBanComponentList(category.getProductId(),selectedList,ConstraintTypeEnum.COMPONENT_TO_COMPONENT));
		banComponentList.addAll(getBanComponentList(category.getProductId(),selectedList,ConstraintTypeEnum.CATEGORY_TO_COMPONENT));
		banComponentList.addAll(getBanComponentList(category.getProductId(),selectedList,ConstraintTypeEnum.MIX_TO_COMPONENT));
		banComponentList.addAll(getBanComponentList(category.getProductId(),selectedList,ConstraintTypeEnum.COMPONENT_TO_MIX));
		banComponentList.addAll(getBanComponentList(category.getProductId(),selectedList,ConstraintTypeEnum.CATEGORY_TO_MIX));
		banComponentList.addAll(getBanComponentList(category.getProductId(),selectedList,ConstraintTypeEnum.MIX_TO_MIX));
		List<Integer> componentIds = JSON.parseArray(category.getComponentsId(), Integer.class);
		if (componentIds == null || componentIds.size() == 0){
			return null;
		}
		List<JdComponent> components = componentMapper.findByComponentIdIn(componentIds);
		components.removeAll(banComponentList);
		return components;
	}

	public List<JdComponent> findOrderComponents(List<Integer> componentIds) {
		return componentMapper.findByComponentIdIn(componentIds);
	}

	public int insertConstraint(ConstraintRequest constraintRequest) {
		List<Integer> mutexIds = insertMutexes(constraintRequest);
		JdMutexDescribe mutexDescribe = new JdMutexDescribe();
		mutexDescribe.setDescribeId(mutexDescribeMapper.findNextDescribeId())
				.setProductId(constraintRequest.getProductId())
				.setConstraintType(constraintRequest.getConstraintType())
				.setMutexIds(JSON.toJSONString(mutexIds))
				.setMutexIds(constraintRequest.getConstraintDesc());
		return mutexDescribeMapper.insert(mutexDescribe);
	}

	public int deleteConstraint(Integer constraintId) {
		return 0;
	}

*
	 * 获得插入互斥表中的id列表
	 * @param constraintRequest
	 * @return
	 * @// TODO: 2018/10/25 还有很多情况遇上再说！！！！


	private List<Integer> insertMutexes(ConstraintRequest constraintRequest){
		List<Integer> mutexIds = new ArrayList<>();
		//组件对组件关系
		if (constraintRequest.getConstraintType().equals(ConstraintTypeEnum.COMPONENT_TO_COMPONENT.getCode())){
			List<JdComponent> resultComponents = constraintRequest.getConstraintResult().getComponents();
			List<Integer> resultComponentIds = new ArrayList<>();
			for (JdComponent resultComponent : resultComponents) {
				resultComponentIds.add(resultComponent.getComponentId());
			}
			List<JdComponent> premiseComponents = constraintRequest.getConstraintPremise().getComponents();
			List<Integer> premiseComponentIds = new ArrayList<>();
			for (JdComponent premiseComponent : premiseComponents) {
				premiseComponentIds.add(premiseComponent.getComponentId());
			}
			if (constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.ONLY_BE_USED.getCode())){
				Set<Integer> excludeComponentIds = new HashSet<>();
				Set<Integer> excludeCategoryIds = new HashSet<>();
				for (JdComponent resultComponent : resultComponents) {
					List<Integer> tempExcludeComponentIds = componentService.excludePeerComponent(resultComponent);
					excludeComponentIds.addAll(tempExcludeComponentIds);
					JdCategory lastCategory = categoryMapper.selectByPrimaryKey(resultComponent.getLastCategoryId());
					List<Integer> tempExcludeCategoryIds = categoryService.excludeAllCategory(lastCategory);
					excludeCategoryIds.addAll(tempExcludeCategoryIds);
				}
				//插入组件对组件互斥
				for (Integer excludeComponentId : excludeComponentIds) {
					JdMutex mutex = new JdMutex();
					mutex.setMutexId(mutexMapper.findNextMutexId())
							.setProductId(constraintRequest.getProductId())
							.setMutexType(ConstraintTypeEnum.COMPONENT_TO_COMPONENT.getCode())
							.setMutexPremise(JSON.toJSONString(Arrays.asList(excludeComponentId)))
							.setMutextResult(JSON.toJSONString(Arrays.asList(premiseComponentIds)));
					mutexMapper.insert(mutex);
					mutexIds.add(mutex.getMutexId());
				}
				JdMutex lastMutexComponentToComponent = new JdMutex();
				lastMutexComponentToComponent.setMutexId(mutexMapper.findNextMutexId())
						.setProductId(constraintRequest.getProductId())
						.setMutexType(ConstraintTypeEnum.COMPONENT_TO_COMPONENT.getCode())
						.setMutexPremise(JSON.toJSONString(Arrays.asList(premiseComponentIds)))
						.setMutextResult(JSON.toJSONString(Arrays.asList(excludeComponentIds)));
				mutexMapper.insert(lastMutexComponentToComponent);
				mutexIds.add(lastMutexComponentToComponent.getMutexId());
				//插入对类型对组件,组件对类型互斥
				for (Integer excludeCategoryId : excludeCategoryIds) {
					JdMutex mutex = new JdMutex();
					mutex.setMutexId(mutexMapper.findNextMutexId())
							.setProductId(constraintRequest.getProductId())
							.setMutexType(ConstraintTypeEnum.CATEGORY_TO_COMPONENT.getCode())
							.setMutexPremise(JSON.toJSONString(Arrays.asList(excludeCategoryId)))
							.setMutextResult(JSON.toJSONString(Arrays.asList(premiseComponents)));
					mutexMapper.insert(mutex);
					mutexIds.add(mutex.getMutexId());
				}
				JdMutex lastMutexComponentToCategory = new JdMutex();
				lastMutexComponentToCategory.setMutexId(mutexMapper.findNextMutexId())
						.setProductId(constraintRequest.getProductId())
						.setMutexType(ConstraintTypeEnum.COMPONENT_TO_CATEGORY.getCode())
						.setMutexPremise(JSON.toJSONString(Arrays.asList(premiseComponents)))
						.setMutextResult(JSON.toJSONString(Arrays.asList(excludeCategoryIds)));
				mutexMapper.insert(lastMutexComponentToCategory);
				mutexIds.add(lastMutexComponentToCategory.getMutexId());
			} else if (constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.MUTEX.getCode())){
				JdMutex mutex = new JdMutex();
				mutex.setMutexId(mutexMapper.findNextMutexId())
						.setProductId(constraintRequest.getProductId())
						.setMutexType(ConstraintTypeEnum.COMPONENT_TO_COMPONENT.getCode())
						.setMutexPremise(JSON.toJSONString(Arrays.asList(premiseComponentIds)))
						.setMutextResult(JSON.toJSONString(Arrays.asList(resultComponentIds)));
				mutexMapper.insert(mutex);
				mutexIds.add(mutex.getMutexId());
				JdMutex mutexReverse = new JdMutex();
				mutex.setMutexId(mutexMapper.findNextMutexId())
						.setProductId(constraintRequest.getProductId())
						.setMutexType(ConstraintTypeEnum.COMPONENT_TO_COMPONENT.getCode())
						.setMutexPremise(JSON.toJSONString(Arrays.asList(resultComponentIds)))
						.setMutextResult(JSON.toJSONString(Arrays.asList(premiseComponentIds)));
				mutexMapper.insert(mutex);
				mutexIds.add(mutex.getMutexId());
			}
		}
		//组件对类型关系
		else if (constraintRequest.getConstraintType().equals(ConstraintTypeEnum.COMPONENT_TO_CATEGORY.getCode())){
			List<JdComponent> premiseComponents = constraintRequest.getConstraintPremise().getComponents();
			List<JdCategory> resultCategories = constraintRequest.getConstraintResult().getCategories();
			List<Integer> premiseComponentIds = new ArrayList<>();
			for (JdComponent premiseComponent : premiseComponents) {
				premiseComponentIds.add(premiseComponent.getComponentId());
			}
			//仅可用于
			if (constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.ONLY_BE_USED.getCode())){
				Set<Integer> excludeCategoryIds = new HashSet<>();
				for (JdCategory resultCategory : resultCategories) {
					List<Integer> tempExcludeCategoryIds = categoryService.excludeAllCategory(resultCategory);
					excludeCategoryIds.addAll(tempExcludeCategoryIds);
				}
				for (Integer excludeCategoryId : excludeCategoryIds) {
					JdMutex mutex = new JdMutex();
					mutex.setMutexId(mutexMapper.findNextMutexId())
							.setProductId(constraintRequest.getProductId())
							.setMutexType(ConstraintTypeEnum.COMPONENT_TO_CATEGORY.getCode())
							.setMutexPremise(JSON.toJSONString(Arrays.asList(excludeCategoryId)))
							.setMutextResult(JSON.toJSONString(Arrays.asList(premiseComponentIds)));
					mutexMapper.insert(mutex);
					mutexIds.add(mutex.getMutexId());
				}
				JdMutex lastMutexCategoryToComponent = new JdMutex();
				lastMutexCategoryToComponent.setMutexId(mutexMapper.findNextMutexId())
						.setProductId(constraintRequest.getProductId())
						.setMutexType(ConstraintTypeEnum.CATEGORY_TO_COMPONENT.getCode())
						.setMutexPremise(JSON.toJSONString(Arrays.asList(premiseComponentIds)))
						.setMutextResult(JSON.toJSONString(Arrays.asList(excludeCategoryIds)));
				mutexMapper.insert(lastMutexCategoryToComponent);
				mutexIds.add(lastMutexCategoryToComponent.getMutexId());
			} else if (constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.MUTEX.getCode())){
				//todo 暂时未遇上部件对类型互斥这种情况，以后处理
			}
		}
		//组件对混合关系
		else if (constraintRequest.getConstraintType().equals(ConstraintTypeEnum.COMPONENT_TO_MIX.getCode())){
			List<JdComponent> premiseComponents = constraintRequest.getConstraintPremise().getComponents();
			ConstraintResult constraintResult = constraintRequest.getConstraintResult();
			List<JdComponent> resultComponents = constraintResult.getComponents();
			List<JdCategory> resultCategories = constraintResult.getCategories();
			List<Integer> resultComponentIds = new ArrayList<>();
			for (JdComponent resultComponent : resultComponents) {
				resultComponentIds.add(resultComponent.getComponentId());
			}
			List<Integer> resultCategoryIds = new ArrayList<>();
			for (JdCategory resultCategory : resultCategories) {
				resultCategoryIds.add(resultCategory.getCategoryId());
			}
			List<Integer> premiseComponentIds = new ArrayList<>();
			for (JdComponent premiseComponent : premiseComponents) {
				premiseComponentIds.add(premiseComponent.getComponentId());
			}
			//仅可用于
			if (constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.ONLY_BE_USED.getCode())){
				//todo 暂时未遇上部件对混合仅可用于这种情况，以后处理
			} else if (constraintRequest.getConstraintOperation().equals(ConstraintOperationEnum.MUTEX.getCode())){
				JdMutex componentToMixMutex = new JdMutex();
				componentToMixMutex.setMutexId(mutexMapper.findNextMutexId())
						.setProductId(constraintRequest.getProductId())
						.setMutexType(ConstraintTypeEnum.MIX_TO_COMPONENT.getCode())
						.setMutexPremise(JSON.toJSONString(constraintResult))
						.setMutextResult(JSON.toJSONString(Arrays.asList(premiseComponentIds)));
				mutexMapper.insert(componentToMixMutex);
				mutexIds.add(componentToMixMutex.getMutexId());
				//反向组件对混合互斥
				for (Integer resultComponentId : resultComponentIds) {
					MixPremiseOrResultIds mixPremise = new MixPremiseOrResultIds();
					List<Integer> categoryIds = new ArrayList<>();
					categoryIds.addAll(resultCategoryIds);
					List<Integer> componentIds = new ArrayList<>();
					componentIds.addAll(resultComponentIds);
					componentIds.remove(resultComponentId);
					componentIds.addAll(premiseComponentIds);
					mixPremise.setCategoryIds(categoryIds);
					mixPremise.setComponentIds(componentIds);
					JdMutex mixToComponentMutex = new JdMutex();
					mixToComponentMutex.setMutexId(mutexMapper.findNextMutexId())
							.setProductId(constraintRequest.getProductId())
							.setMutexType(ConstraintTypeEnum.MIX_TO_COMPONENT.getCode())
							.setMutexPremise(JSON.toJSONString(mixPremise))
							.setMutextResult(JSON.toJSONString(Arrays.asList(resultComponentId)));
					mutexMapper.insert(mixToComponentMutex);
					mutexIds.add(mixToComponentMutex.getMutexId());
				}
				for (Integer resultCategoryId : resultCategoryIds) {
					MixPremiseOrResultIds mixPremise = new MixPremiseOrResultIds();
					List<Integer> categoryIds = new ArrayList<>();
					categoryIds.addAll(resultCategoryIds);
					categoryIds.remove(resultCategoryId);
					List<Integer> componentIds = new ArrayList<>();
					componentIds.addAll(resultComponentIds);
					componentIds.addAll(premiseComponentIds);
					mixPremise.setCategoryIds(categoryIds);
					mixPremise.setComponentIds(componentIds);
					JdMutex mixToCategoryMutex = new JdMutex();
					mixToCategoryMutex.setMutexId(mutexMapper.findNextMutexId())
							.setProductId(constraintRequest.getProductId())
							.setMutexType(ConstraintTypeEnum.MIX_TO_CATEGORY.getCode())
							.setMutexPremise(JSON.toJSONString(mixPremise))
							.setMutextResult(JSON.toJSONString(Arrays.asList(resultCategoryId)));
					mutexMapper.insert(mixToCategoryMutex);
					mutexIds.add(mixToCategoryMutex.getMutexId());
				}
			}
		}
		//todo 其他情况暂时未遇上，以后处理
		return mutexIds;
	}


*
	 * 获取禁用类型
	 * @param
	 * @param
	 * @return


	private List<JdCategory> getBanCateGoryList (List<JdCategory> selectedCategories,List<JdComponent> selectedComponents){
		List<Integer> categoryIds = new ArrayList<>();
		for (JdCategory category : selectedCategories) {
			categoryIds.add(category.getCategoryId());
		}
		List<JdMutex> mutexListCategoryToCategory = mutexMapper.findByMutexType(ConstraintTypeEnum.CATEGORY_TO_CATEGORY.getCode());
		List<JdCategory> banCategoryList = new ArrayList<>();
		for (JdMutex mutex : mutexListCategoryToCategory) {
			List<Integer> mutexPremise = JSON.parseArray(mutex.getMutexPremise(), Integer.class);
			if (categoryIds.containsAll(mutexPremise)){
				List<Integer> banCategoryIds = JSON.parseArray(mutex.getMutextResult(), Integer.class);
				List<JdCategory> banComponents= categoryMapper.findByCategoryIdIn(banCategoryIds);
				banCategoryList.addAll(banComponents);
			}
		}
		List<Integer> componentIds = new ArrayList<>();
		for (JdComponent component : selectedComponents) {
			componentIds.add(component.getComponentId());
		}
		List<JdMutex> mutexListComponentToCategory = mutexMapper.findByMutexType(ConstraintTypeEnum.COMPONENT_TO_CATEGORY.getCode());
		for (JdMutex mutex : mutexListComponentToCategory) {
			List<Integer> mutexPremise = JSON.parseArray(mutex.getMutexPremise(), Integer.class);
			if (componentIds.containsAll(mutexPremise)){
				List<Integer> banCategoryIds = JSON.parseArray(mutex.getMutextResult(), Integer.class);
				List<JdCategory> banComponents= categoryMapper.findByCategoryIdIn(banCategoryIds);
				banCategoryList.addAll(banComponents);
			}
		}
		return banCategoryList;
	}


	private List<JdCategory> getBanCategoryList(Integer productId,List<JdComponent> selectedComponents,ConstraintTypeEnum constraintType){
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
		List<JdMutex> mutexList = mutexMapper.findByProductIdAndMutexType(productId,constraintType.getCode());
		if (mutexList != null && mutexList.size() > 0){
			if (constraintType.getCode().equals(ConstraintTypeEnum.COMPONENT_TO_CATEGORY.getCode())){
				for (JdMutex mutex : mutexList) {
					List<Integer> mutexPremise = JSON.parseArray(mutex.getMutexPremise(), Integer.class);
					if (componentIds.containsAll(mutexPremise)){
						List<Integer> banCategoryIds = JSON.parseArray(mutex.getMutextResult(), Integer.class);
						List<JdCategory> banComponents= categoryMapper.findByCategoryIdIn(banCategoryIds);
						banCategoryList.addAll(banComponents);
					}
				}
			} else if (constraintType.getCode().equals(ConstraintTypeEnum.CATEGORY_TO_CATEGORY.getCode())){
				for (JdMutex mutex : mutexList) {
					List<Integer> mutexPremise = JSON.parseArray(mutex.getMutexPremise(), Integer.class);
					if (categoryIds.containsAll(mutexPremise)){
						List<Integer> banCategoryIds = JSON.parseArray(mutex.getMutextResult(), Integer.class);
						List<JdCategory> banComponents= categoryMapper.findByCategoryIdIn(banCategoryIds);
						banCategoryList.addAll(banComponents);
					}
				}
			} else if (constraintType.getCode().equals(ConstraintTypeEnum.COMPONENT_TO_MIX.getCode())){
				for (JdMutex mutex : mutexList) {
					List<Integer> mutexPremise = JSON.parseArray(mutex.getMutexPremise(), Integer.class);
					if (componentIds.containsAll(mutexPremise)){
						MixPremiseOrResult mixResult = getBanResultFromMix(mutex.getMutextResult());
						banCategoryList.addAll(mixResult.getCategories());
					}
				}
			} else if (constraintType.getCode().equals(ConstraintTypeEnum.MIX_TO_CATEGORY.getCode())){
				for (JdMutex mutex : mutexList) {
					if (containMixBanType(categoryIds,componentIds,mutex.getMutexPremise())){
						List<Integer> banCategoryIds = JSON.parseArray(mutex.getMutextResult(), Integer.class);
						List<JdCategory> banComponents= categoryMapper.findByCategoryIdIn(banCategoryIds);
						banCategoryList.addAll(banComponents);
					}
				}
			} else if (constraintType.getCode().equals(ConstraintTypeEnum.CATEGORY_TO_MIX.getCode())){
				for (JdMutex mutex : mutexList) {
					List<Integer> mutexPremise = JSON.parseArray(mutex.getMutexPremise(), Integer.class);
					if (categoryIds.containsAll(mutexPremise)){
						MixPremiseOrResult mixResult = getBanResultFromMix(mutex.getMutextResult());
						banCategoryList.addAll(mixResult.getCategories());
					}
				}
			} else if (constraintType.getCode().equals(ConstraintTypeEnum.MIX_TO_MIX.getCode())){
				for (JdMutex mutex : mutexList) {
					if (containMixBanType(categoryIds,componentIds,mutex.getMutexPremise())){
						MixPremiseOrResult mixResult = getBanResultFromMix(mutex.getMutextResult());
						banCategoryList.addAll(mixResult.getCategories());
					}
				}
			}
		}
		return banCategoryList;
	}

*
	 * 获取禁用部件
	 * @param
	 * @param selectedComponents
	 * @return


private List<JdComponent> getBanComponentList (List<JdCategory> selectedCategories,List<JdComponent> selectedComponents){
		List<Integer> categoryIds = new ArrayList<>();
		for (JdCategory category : selectedCategories) {
			categoryIds.add(category.getCategoryId());
		}
		List<JdMutex> mutexListCategoryToComponent = mutexMapper.findByMutexType(ConstraintTypeEnum.CATEGORY_TO_COMPONENT.getCode());
		List<JdComponent> banComponentList = new ArrayList<>();
		for (JdMutex mutex : mutexListCategoryToComponent) {
			List<Integer> mutexPremise = JSON.parseArray(mutex.getMutexPremise(), Integer.class);
			if (categoryIds.containsAll(mutexPremise)){
				List<Integer> banComponentIds = JSON.parseArray(mutex.getMutextResult(), Integer.class);
				List<JdComponent> banComponents= componentMapper.findByComponentIdIn(banComponentIds);
				banComponentList.addAll(banComponents);
			}
		}
		List<Integer> componentIds = new ArrayList<>();
		for (JdComponent component : selectedComponents) {
			componentIds.add(component.getComponentId());
		}
		List<JdMutex> mutexListComponentToComponent = mutexMapper.findByMutexType(ConstraintTypeEnum.COMPONENT_TO_COMPONENT.getCode());
		for (JdMutex mutex : mutexListComponentToComponent) {
			List<Integer> mutexPremise = JSON.parseArray(mutex.getMutexPremise(), Integer.class);
			if (componentIds.containsAll(mutexPremise)){
				List<Integer> banComponentIds = JSON.parseArray(mutex.getMutextResult(), Integer.class);
				List<JdComponent> banComponents= componentMapper.findByComponentIdIn(banComponentIds);
				banComponentList.addAll(banComponents);
			}
		}
		return banComponentList;
	}


	private List<JdComponent> getBanComponentList (Integer productId,List<JdComponent> selectedComponents,ConstraintTypeEnum constraintType){
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
		List<JdMutex> mutexList = mutexMapper.findByProductIdAndMutexType(productId,constraintType.getCode());
		if (mutexList != null && mutexList.size() > 0){
			if (constraintType.getCode().equals(ConstraintTypeEnum.COMPONENT_TO_COMPONENT.getCode())){
				for (JdMutex mutex : mutexList) {
					List<Integer> mutexPremise = JSON.parseArray(mutex.getMutexPremise(), Integer.class);
					if (componentIds.containsAll(mutexPremise)){
						List<Integer> banComponentIds = JSON.parseArray(mutex.getMutextResult(), Integer.class);
						List<JdComponent> banComponents = componentMapper.findByComponentIdIn(banComponentIds);
						banComponentList.addAll(banComponents);
					}
				}
			} else if (constraintType.getCode().equals(ConstraintTypeEnum.CATEGORY_TO_COMPONENT.getCode())){
				for (JdMutex mutex : mutexList) {
					List<Integer> mutexPremise = JSON.parseArray(mutex.getMutexPremise(), Integer.class);
					if (categoryIds.containsAll(mutexPremise)){
						List<Integer> banComponentIds = JSON.parseArray(mutex.getMutextResult(), Integer.class);
						List<JdComponent> banComponents = componentMapper.findByComponentIdIn(banComponentIds);
						banComponentList.addAll(banComponents);
					}
				}
			} else if (constraintType.getCode().equals(ConstraintTypeEnum.MIX_TO_COMPONENT.getCode())){
				for (JdMutex mutex : mutexList) {
					if (containMixBanType(categoryIds,componentIds,mutex.getMutexPremise())){
						List<Integer> banComponentIds = JSON.parseArray(mutex.getMutextResult(), Integer.class);
						List<JdComponent> banComponents = componentMapper.findByComponentIdIn(banComponentIds);
						banComponentList.addAll(banComponents);
					}
				}
			} else if (constraintType.getCode().equals(ConstraintTypeEnum.COMPONENT_TO_MIX.getCode())){
				for (JdMutex mutex : mutexList) {
					if (containMixBanType(categoryIds,componentIds,mutex.getMutexPremise())){
						MixPremiseOrResult mixResult = getBanResultFromMix(mutex.getMutextResult());
						banComponentList.addAll(mixResult.getComponents());
					}
				}
			} else if (constraintType.getCode().equals(ConstraintTypeEnum.CATEGORY_TO_MIX.getCode())){
				for (JdMutex mutex : mutexList) {
					List<Integer> mutexPremise = JSON.parseArray(mutex.getMutexPremise(), Integer.class);
					if (categoryIds.containsAll(mutexPremise)){
						MixPremiseOrResult mixResult = getBanResultFromMix(mutex.getMutextResult());
						banComponentList.addAll(mixResult.getComponents());
					}
				}
			} else if (constraintType.getCode().equals(ConstraintTypeEnum.MIX_TO_MIX.getCode())){
				for (JdMutex mutex : mutexList) {
					if (containMixBanType(categoryIds,componentIds,mutex.getMutexPremise())){
						MixPremiseOrResult mixResult = getBanResultFromMix(mutex.getMutextResult());
						banComponentList.addAll(mixResult.getComponents());
					}
				}
			}
		}
		return banComponentList;
	}
*
	 * 判断是否包含混合禁用
	 * @param selectedCategories
	 * @param selectedComponents
	 * @param mixPremiseString
	 * @return


	private boolean containMixBanType(List<Integer> selectedCategories,List<Integer> selectedComponents,String mixPremiseString){
		MixPremiseOrResultIds mixPremiseOrResultIds = getMixPremiseOrResultIdsFromMix(mixPremiseString);
		if (selectedCategories.containsAll(mixPremiseOrResultIds.getCategoryIds())){
			return selectedComponents.containsAll(mixPremiseOrResultIds.getCategoryIds());
		}
		return false;
	}

	private MixPremiseOrResult getBanResultFromMix(String mixPremiseString){
		MixPremiseOrResultIds mixPremiseOrResultIds = getMixPremiseOrResultIdsFromMix(mixPremiseString);
		List<JdCategory> categories = categoryMapper.findByCategoryIdIn(mixPremiseOrResultIds.getCategoryIds());
		List<JdComponent> components = componentMapper.findByComponentIdIn(mixPremiseOrResultIds.getCategoryIds());
		MixPremiseOrResult mixResult = new MixPremiseOrResult();
		mixResult.setCategories(categories);
		mixResult.setComponents(components);
		return mixResult;
	}

	private MixPremiseOrResultIds getMixPremiseOrResultIdsFromMix(String mixPremiseString){
		MixPremiseOrResultIds mixPremiseOrResultIds = JSON.parseObject(mixPremiseString, MixPremiseOrResultIds.class);
		return mixPremiseOrResultIds;
	}
//	public List<JdCategory> getBanCategory(List<JdCategory> selectedCategories){
//		List<JdConstraint> categoryToCategoryConstraints = constraintMapper.findByConstraintType(ConstraintTypeEnum.CATEGORY_TO_CATEGORY.getCode());
//		List<Map<Integer,List<Integer>>> constraintPremiseMapList = new ArrayList<>();
//		for (JdConstraint constraint : categoryToCategoryConstraints) {
//			Map<Integer, List<Integer>> constraintPremiseMap = new HashMap<>();
//			constraintPremiseMap.put(constraint.getConstraintId(), JSON.parseArray(constraint.getConstraintPremise(),Integer.class));
//			constraintPremiseMapList.add(constraintPremiseMap);
//		}
//		List<Integer> selectedCategoryIds = new ArrayList<>();
//		for (JdCategory selectedCategory : selectedCategories) {
//			selectedCategoryIds.add(selectedCategory.getCategoryId());
//		}
//		List<JdCategory> banCategoryList = new ArrayList<>();
//		for (Map<Integer, List<Integer>> premiseMap : constraintPremiseMapList) {
//			for (Map.Entry<Integer, List<Integer>> entry : premiseMap.entrySet()) {
//				if (selectedCategoryIds.containsAll(entry.getValue())){
//					JdConstraint constraint = constraintMapper.selectByPrimaryKey(entry.getKey());
//					if (constraint.getConstraintType().equals(ConstraintOperationEnum.MUTEX.getCode())){
//						banCategoryList.addAll(categoryMapper.findByCategoryIdIn(JSON.parseArray(constraint.getConstraintResult(),Integer.class)));
//					} else if (constraint.getConstraintType().equals(ConstraintOperationEnum.AVAILABLE.getCode())){
//						List<JdCategory> availableCategories = getCategoriesFromCategories(categoryMapper.findByCategoryIdIn(JSON.parseArray(constraint.getConstraintResult(), Integer.class)));
//						for (JdCategory category : availableCategories) {
//							// TODO: 2018/10/19
//						}
//					}
//				}
//			}
//		}
//		return banCategoryList;
//	}

*
	 * 根据已选组件列表获取所有类别
	 * @param selectedList
	 * @return


	public List<JdCategory> getCategories(List<JdComponent> selectedList){
		List<JdCategory> categories = new ArrayList<>();
		for (JdComponent component : selectedList) {
			JdCategory lastCategory = categoryMapper.selectByPrimaryKey(component.getLastCategoryId());
			categories.add(lastCategory);
			Integer parentId = lastCategory.getParentId();
			while (!parentId.equals(TOP_LEVEL)){
				JdCategory category = categoryMapper.selectByPrimaryKey(parentId);
				categories.add(category);
				parentId = category.getParentId();
			}
		}
		return categories;
	}

*
	 * 根据已有类别列表获取所有类别
	 * @param categories
	 * @return


	public List<JdCategory> getCategoriesFromCategories(List<JdCategory> categories){
		List<JdCategory> allCategories = new ArrayList<>();
		allCategories.addAll(categories);
		for (JdCategory category : categories) {
			Integer parentId = category.getParentId();
			while (!parentId.equals(TOP_LEVEL)){
				JdCategory tempCategory = categoryMapper.selectByPrimaryKey(parentId);
				allCategories.add(category);
				parentId = category.getParentId();
			}
		}
		return allCategories;
	}*/
}
