package com.ecommerce.inventory_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_NAME_ORDER_EVENTS = "order-events";
    public static final String QUEUE_NAME_INVENTORY = "inventory-queue";

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public Queue inventoryQueue() {
        return new Queue(QUEUE_NAME_INVENTORY, Boolean.TRUE);
    }

    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME_ORDER_EVENTS);
    }

    @Bean
    public Binding binding(Queue inventoryQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(inventoryQueue).to(orderEventsExchange).with("order.placed");
    }
}
