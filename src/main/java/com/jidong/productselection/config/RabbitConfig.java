package com.jidong.productselection.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: LiuChong
 * @Date: 2018/12/20 14:30
 * @Description:
 */
@Configuration
public class RabbitConfig {

	@Value("${ps-connect-cad.exchange}")
	private String exchangeName;

	@Value("${ps-connect-cad.pstocad-queue}")
	private String queueName;

	@Value("${ps-connect-cad.cadtops-queue}")
	private String cadToPsQueueName;

	@Bean
	public TopicExchange psToCADExchange() {
		return new TopicExchange(exchangeName);
	}

	@Bean
	public Queue psToCADQueue() {
		return new Queue(queueName, true);
	}

	@Bean
	public Queue CADToPsQueue() {
		return new Queue(cadToPsQueueName, true);
	}

	@Bean
	public Binding binding() {
		return BindingBuilder.bind(psToCADQueue()).to(psToCADExchange()).with(queueName);
	}

	@Bean
	public Binding bindingCADToPsQueue() {
		return BindingBuilder.bind(CADToPsQueue()).to(psToCADExchange()).with(cadToPsQueueName);
	}
}
