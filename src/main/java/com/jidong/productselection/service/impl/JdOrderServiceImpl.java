package com.jidong.productselection.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jidong.productselection.dao.*;
import com.jidong.productselection.dto.MandatoryResult;
import com.jidong.productselection.dto.OrderDetail;
import com.jidong.productselection.dto.OrderItem;
import com.jidong.productselection.entity.*;
import com.jidong.productselection.enums.ComponentTypeEnum;
import com.jidong.productselection.request.GenerateOrderModelNumberRequest;
import com.jidong.productselection.request.OrderSearchRequest;
import com.jidong.productselection.service.JdComponentService;
import com.jidong.productselection.service.JdConstraintService;
import com.jidong.productselection.service.JdOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: LiuChong
 * @Date: 2018/10/22 09:46
 * @Description:
 */
@Slf4j
@Service
public class JdOrderServiceImpl implements JdOrderService {

	private static final Integer QTY = Integer.valueOf(1);

	private static final String CROSS_BAR = "-";

	@Autowired
	private JdOrderMapper orderMapper;

	@Autowired
	private JdCategoryMapper categoryMapper;

	@Autowired
	private JdComponentMapper componentMapper;

	@Autowired
	private JdAttachmentMapper attachmentMapper;

	@Autowired
	private JdComponentService componentService;

	@Autowired
	private JdMandatoryMapper mandatoryMapper;

	@Autowired
	private JdConstraintService constraintService;

	@Autowired
	private JdShelfDistinctionMapper shelfDistinctionMapper;

	@Autowired
	private JdControlBoxMapper controlBoxMapper;

	@Autowired
	private JdBracketMountingHeightMapper mountingHeightMapper;

	@Autowired

	private JdProductMapper productMapper;

	@Override
	public PageInfo<JdOrder> findByPage(int page, int rows) {
		PageHelper.startPage(page, rows);
		List<JdOrder> orders = orderMapper.findByIsDeleted(false);
		PageInfo<JdOrder> orderPageInfo = new PageInfo<>(orders);
		return orderPageInfo;
	}

	@Override
	public PageInfo<JdOrder> searchByPage(OrderSearchRequest orderSearchRequest) {
		PageHelper.startPage(orderSearchRequest.getPage(), orderSearchRequest.getRows());
		List<JdOrder> orders = orderMapper.findByOrderSearchRequest(orderSearchRequest);
		PageInfo<JdOrder> orderPageInfo = new PageInfo<>(orders);
		return orderPageInfo;
	}

	@Override
	@Transactional
	public int deleteByOrderId(Integer orderId) {
		return orderMapper.deleteByOrderId(orderId);
	}

	@Override
	public JdOrder findOne(Integer orderId) {
		return orderMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public MandatoryResult getMandatoryResult(JdOrder order) {
		List<Integer> componentIds = (JSON.parseArray(order.getComponentIds(), JdComponent.class)).stream().map(JdComponent::getComponentId).collect(Collectors.toList());
		List<JdComponent> components = componentMapper.findByComponentIdIn(componentIds);
		Set<JdCategory> categories = new HashSet<>();
		for (JdComponent component : components) {
			categories.addAll(componentService.getCategoryList(component));
		}

		List<Integer> categoryIds = categories.stream().map(JdCategory::getCategoryId).collect(Collectors.toList());
		MandatoryResult mandatoryResult = getMandatoryItem(order.getProductId(), categoryIds, componentIds);
		MandatoryResult selectedList = new MandatoryResult();
		selectedList.setComponents(components);
		selectedList.setCategories(new ArrayList<>(categories));
		return compareMandatoryAndSelected(order.getProductId(), mandatoryResult, selectedList);
	}

	@Override
	public OrderDetail getOrderDetail(Integer orderId) {
		OrderDetail orderDetail = new OrderDetail();
		JdOrder order = findOne(orderId);
		orderDetail.setOrder(order);
		Integer productId = order.getProductId();
		JdProduct product = productMapper.selectByPrimaryKey(productId);
		List<OrderItem> orderItems = JSON.parseArray(order.getComponentIds(), OrderItem.class);
		List<Integer> componentIds = orderItems.stream().map(OrderItem::getCompId).collect(Collectors.toList());
		List<JdComponent> components = componentMapper.findByComponentIdIn(componentIds);
		components.forEach(comp -> {
			if (Objects.equals(comp.getFirstCategoryId(),product.getShelfId())){
				comp.setComponentModelNumber(order.getShelfCode());
			}
		});
		orderDetail.setComponents(components);
		return orderDetail;
	}

	//比较已选列表和必选列表 还需要去除互斥类型
	private MandatoryResult compareMandatoryAndSelected(Integer productId, MandatoryResult mandatory, MandatoryResult selectedList) {
		mandatory.getCategories().removeAll(selectedList.getCategories());
		mandatory.getComponents().retainAll(selectedList.getComponents());
		mandatory.getCategories().removeAll(constraintService.getBanCategoryList(productId, selectedList.getComponents()));
		mandatory.getComponents().removeAll(constraintService.getBanComponentList(productId, selectedList.getComponents()));
		return mandatory;
	}

	//获取必选列表
	private MandatoryResult getMandatoryItem(Integer productId, List<Integer> categoryIds, List<Integer> componentIds) {
		List<JdMandatory> mandatoryList = mandatoryMapper.findByPremiseProductId(productId);
		mandatoryList.addAll(mandatoryMapper.findByPremiseCategoryIdIn(categoryIds));
		mandatoryList.addAll(mandatoryMapper.findByPremiseComponentIdIn(componentIds));
		List<Integer> resultCategoryIds = mandatoryList.stream().map(JdMandatory::getResultCategoryId).collect(Collectors.toList());
		List<Integer> resultComponentIds = mandatoryList.stream().map(JdMandatory::getResultComponentId).collect(Collectors.toList());
		List<JdCategory> resultCategories = resultCategoryIds.size() == 0 ? Collections.emptyList() : categoryMapper.findByCategoryIdIn(resultCategoryIds);
		List<JdComponent> resultComponents = resultComponentIds.size() == 0 ? Collections.emptyList() : componentMapper.findByComponentIdIn(resultComponentIds);
		MandatoryResult mandatoryResult = new MandatoryResult();
		mandatoryResult.setCategories(resultCategories);
		mandatoryResult.setComponents(resultComponents);
		return mandatoryResult;
	}

	@Override
	@Transactional
	public int insert(JdOrder order) {
		parseComponentIds(order);
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		order.setCreator(userName);
		order.setModifier(userName);
		Integer nextOrderId = orderMapper.findNextOrderId();
		order.setOrderId(nextOrderId);
		order.setOrderNumber(UUID.randomUUID().toString().replace("-", ""));
		Date now = new Date();
		order.setCreateTime(now);
		order.setUpdateTime(now);
		order.setIsDeleted(false);
		return orderMapper.insert(order);
	}

	//生成componentIds以及架子安装号
	private void parseComponentIds(JdOrder order) {
		List<JdComponent> componentList = JSON.parseArray(order.getComponentIds(), JdComponent.class);
		List<Integer> componentIds = componentList.stream().map(JdComponent::getComponentId).collect(Collectors.toList());
		//生成架子安装号
		JdProduct product = productMapper.selectByPrimaryKey(order.getProductId());
		if (product.getShelfId() != null){
			List<JdShelfDistinction> shelfDistinctionList = shelfDistinctionMapper.findByProductId(order.getProductId());
			//获取最佳匹配的以获取架子后缀
			String shelfTail = "";
			int mostMatch = 0;
			JdShelfDistinction mostMatchShelfDistinction = new JdShelfDistinction();
			for (JdShelfDistinction shelfDistinction : shelfDistinctionList) {
				List<JdComponent> selectedList = componentMapper.findByComponentIdIn(componentIds);
				List<Integer> tempIdList = constraintService.getCategories(selectedList).stream().map(JdCategory::getCategoryId).collect(Collectors.toList());
				tempIdList.retainAll(JSON.parseArray(shelfDistinction.getCategoryIds(), Integer.class));
				if (tempIdList.size() > mostMatch) {
					mostMatch = tempIdList.size();
					mostMatchShelfDistinction = shelfDistinction;
				}
			}
			if (StringUtils.hasText(mostMatchShelfDistinction.getDistinctionCode())) {
				shelfTail = mostMatchShelfDistinction.getDistinctionCode();
			} else {
				log.warn("未找到架子后缀！");
			}
			//获取控制箱安装号
			String controllerBoxInstallationCode = "";
			List<Integer> controlBoxIds = controlBoxMapper.find().stream().map(JdControlBox::getComponentId).collect(Collectors.toList());
			controlBoxIds.retainAll(componentIds);
			if (!CollectionUtils.isEmpty(controlBoxIds)) {
				Integer controlBoxId = controlBoxIds.get(0);
				controllerBoxInstallationCode = controlBoxMapper.findByComponentId(controlBoxId).getInstallationCode();
			} else {
				log.warn("未找到控制箱后缀！");
			}
			//拼装架子组件编码

			StringBuilder shelfCode = new StringBuilder();
			Integer shelfCategoryId = product.getShelfId();
			Integer installationId = product.getInstallationId();
			List<JdComponent> shelf = componentList.stream().filter(comp -> comp.getFirstCategoryId().equals(shelfCategoryId)).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(shelf)) {
				shelfCode.append(shelf.get(0).getComponentModelNumber()).append(CROSS_BAR).append(order.getShelfHeight()).append(CROSS_BAR);
				JdComponent installation = componentList.stream().filter(comp -> comp.getFirstCategoryId().equals(installationId)).collect(Collectors.toList()).get(0);
				shelfCode.append(installation.getComponentShortNumber());
				Integer mountHeight = order.getMountHeight();
				String heightCode = mountingHeightMapper.findByHeight(mountHeight).getHeightCode();
				shelfCode.append(heightCode);
				if (StringUtils.hasText(controllerBoxInstallationCode) || StringUtils.hasText(shelfTail)) {
					shelfCode.append(CROSS_BAR).append(controllerBoxInstallationCode).append(shelfTail);
				}
			} else {
				log.warn("未找到架子组件！");
			}
			order.setShelfCode(shelfCode.toString());
		}

		//生成componentIds
		List<OrderItem> orderItems = new ArrayList<>();
		for (JdComponent component : componentList) {
			List<Integer> tempComponentIds = new ArrayList<>(componentIds);
			Integer componentType = component.getComponentType();
			Integer componentParent;
			if (componentType.equals(ComponentTypeEnum.ATTACHMENT.getCode())) {
				List<Integer> masterIds = attachmentMapper.findByAttachmentId(component.getComponentId()).stream().map(JdAttachment::getMasterId).collect(Collectors.toList());
				tempComponentIds.retainAll(masterIds);
				componentParent = tempComponentIds.get(0);
			} else {
				componentParent = Integer.valueOf(0);
			}
			if (componentType.equals(ComponentTypeEnum.REALLY_COMPONENT.getCode())){
				if (Objects.equals(component.getFirstCategoryId(),product.getShelfId())){
					componentType = ComponentTypeEnum.SHELF.getCode();
				}
			}
			if (componentType.equals(ComponentTypeEnum.VIRTUAL_COMPONENT.getCode())){
				if (Objects.equals(component.getFirstCategoryId(),product.getInstallationId())){
					componentType = ComponentTypeEnum.INSTALLATION.getCode();
				}
			}
			orderItems.add(new OrderItem(component.getComponentId(), QTY, componentType, componentParent,component.getComponentModelNumber()));
		}
		order.setComponentIds(JSON.toJSONString(orderItems));
	}

	@Override
	@Transactional
	public int update(JdOrder order) {
		parseComponentIds(order);
		Date now = new Date();
		order.setUpdateTime(now);
		order.setModifier(SecurityContextHolder.getContext().getAuthentication().getName());
		return orderMapper.updateByPrimaryKeySelective(order);
	}

	@Override
	public String generateOrderModelNumber(GenerateOrderModelNumberRequest modelNumberRequest) {
		String orderModelNumber = "";
		JdProduct product = productMapper.selectByPrimaryKey(modelNumberRequest.getProductId());
		Map<Integer, String> orderShortNumberMap = new TreeMap<>(Comparator.naturalOrder());
		for (JdComponent component : modelNumberRequest.getSelectedList()) {
			Integer order = categoryMapper.selectByPrimaryKey(component.getFirstCategoryId()).getCategoryOrder();
			String shortNumber = "";
			boolean isShelf = component.getFirstCategoryId().equals(product.getShelfId());
			boolean isInstallation = component.getFirstCategoryId().equals(product.getInstallationId());
			if (isShelf) {
				shortNumber = component.getComponentShortNumber().trim()
						+ handleShelfHeight(modelNumberRequest.getShelfHeight());
			} else if (isInstallation) {
				if (modelNumberRequest.getMountHeight() != null) {
					shortNumber = component.getComponentShortNumber().trim()
							+ modelNumberRequest.getMountHeight().getHeightCode();
				} else {
					shortNumber = component.getComponentShortNumber().trim();
				}
			} else if (StringUtils.hasText(component.getComponentKey())) {
				shortNumber = component.getComponentKey().trim();
			} else {
				shortNumber = component.getComponentShortNumber().trim();
			}
			orderShortNumberMap.put(order, shortNumber);
		}
		orderModelNumber = addCrossBar(orderShortNumberMap, JSON.parseArray(product.getSegmentation(), Integer.class));
		return orderModelNumber;
	}

	private String handleShelfHeight(Integer shelfHeight) {
		int remainder = shelfHeight % 100;
		String result = "";
		if (remainder == 0) {
			int quotient = shelfHeight / 100;
			if (quotient >= 10) {
				result = Integer.valueOf(quotient).toString();
			} else {
				result = "0" + Integer.valueOf(quotient).toString();
			}
		} else {
			result = shelfHeight >= 1000 ? (Integer.valueOf(shelfHeight / 10)).toString() : shelfHeight.toString();
		}
		return result;
	}

	private String addCrossBar(Map<Integer, String> orderShortNumberMap, List<Integer> subsectionNumbs) {
		List<StringBuilder> sections = new ArrayList<>();
		for (int i = 0; i < subsectionNumbs.size(); i++) {
			StringBuilder section = new StringBuilder();
			if (i == 0) {
				for (int j = 1; j < subsectionNumbs.get(i) + 1; j++) {
					if (orderShortNumberMap.containsKey(j)) {
						section.append(orderShortNumberMap.get(j));
					}
				}
			} else {
				for (int j = subsectionNumbs.get(i - 1) + 1; j < subsectionNumbs.get(i) + 1; j++) {
					if (orderShortNumberMap.containsKey(j)) {
						section.append(orderShortNumberMap.get(j));
					}
				}
			}
			if (StringUtils.hasText(section)) {
				sections.add(section);
			}
		}
		StringBuilder finalModelNumber = new StringBuilder();
		for (int i = 0; i < sections.size(); i++) {
			finalModelNumber.append(sections.get(i));
			if (i + 1 < sections.size() && StringUtils.hasText(sections.get(i + 1))) {
				finalModelNumber.append('-');
			}
		}
		return finalModelNumber.toString();
	}

}
