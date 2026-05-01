package com.ecommerce.notification_service.config;

import com.ecommerce.notification_service.event.OrderCancelledEvent;
import com.ecommerce.notification_service.event.OrderConfirmedEvent;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_NAME_ORDER_EVENTS = "order-events";
    public static final String QUEUE_NAME_NOTIFICATION = "notification-queue";

    @Bean
    public MessageConverter messageConverter() {
        // Usamos la versión moderna de Jackson para Spring Boot 4
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        DefaultClassMapper classMapper = new DefaultClassMapper();

        // Permitimos que confíe en nuestros paquetes de eventos
        classMapper.setTrustedPackages("*");

        // MAPEAREMOS LAS IDENTIDADES:
        // "Nombre de clase que viene del emisor" -> "Clase local que la recibe"
        Map<String, Class<?>> idClassMapping = new HashMap<>();

        // Si viene un 'OrderPlacedEvent' desde Inventario, lo tratamos como 'OrderConfirmedEvent' local
        idClassMapping.put("com.ecommerce.inventory_service.event.OrderConfirmedEvent", OrderConfirmedEvent.class);

        // Si viene una cancelación, la mapeamos a nuestra clase local de cancelación
        idClassMapping.put("com.ecommerce.inventory_service.event.OrderCancelledEvent", OrderCancelledEvent.class);

        classMapper.setIdClassMapping(idClassMapping);
        converter.setClassMapper(classMapper);

        return converter;
    }

    @Bean
    public Queue notificationQueue(){
        return QueueBuilder.durable(QUEUE_NAME_NOTIFICATION)
                .withArgument("x-dead-letter-exchange", "notification-dlx")
                .withArgument("x-dead-letter-routing-key","notification.dead")
                .build();
    }

    @Bean
    public TopicExchange orderEventsExchange(){
        return new TopicExchange(EXCHANGE_NAME_ORDER_EVENTS);
    }

    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange orderEventsExchange){
        return BindingBuilder.bind(notificationQueue)
                .to(orderEventsExchange)
                .with("order.confirmed");
    }

    @Bean
    public Binding cancelledBinding(Queue notificationQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(orderEventsExchange)
                .with("order.cancelled");
    }

    @Bean
    public DirectExchange deadLetterExchange(){
        return new DirectExchange("notification-dlx");
    }
    @Bean
    public Queue deadLetterQueue(){

        return new Queue("notification-dlq", true);
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with("notification.dead");
    }
}
