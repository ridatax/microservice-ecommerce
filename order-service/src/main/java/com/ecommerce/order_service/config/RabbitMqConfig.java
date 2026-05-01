package com.ecommerce.order_service.config;

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

    @Bean
    public MessageConverter messageConverter(){
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public TopicExchange orderEventsExchange(){
        return new TopicExchange(EXCHANGE_NAME_ORDER_EVENTS);
    }

    @Bean
    public Queue orderConfirmedQueue() {
        return new Queue("order-confirmed-queue", true);
    }

    @Bean
    public Binding confirmedBinding(Queue orderConfirmedQueue, TopicExchange orderEventsExchange){
        return BindingBuilder.bind(orderConfirmedQueue)
                .to(orderEventsExchange)
                .with("order.confirmed");
    }

    @Bean
    public Queue orderCancelledQueue() {
        return new Queue("order-cancelled-queue", true);
    }

    @Bean
    public Binding cancelledBinding(Queue orderCancelledQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(orderCancelledQueue)
                .to(orderEventsExchange)
                .with("order.cancelled");
    }
}
