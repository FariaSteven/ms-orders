package br.com.food.orders.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderAMQPConfiguration {
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue queueOrdersDetails() {
        return QueueBuilder
                .nonDurable("pagamentos.detalhes-pedido")
                .deadLetterExchange("pagamentos.dlx")
                .build();
    }

    @Bean
    public Queue queueDlqOrdersDetails() {
        return QueueBuilder.nonDurable("pagamentos.detalhes-pedido-dlq").build();
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder.fanoutExchange("pagamentos.ex")
                .build();
    }

    @Bean
    public FanoutExchange deadLetterExchange() {
        return ExchangeBuilder.fanoutExchange("pagamentos.dlx")
                .build();
    }

    @Bean
    public Binding bindOrderPayment() {
        return BindingBuilder.bind(queueOrdersDetails())
                .to(fanoutExchange());
    }

    @Bean
    public Binding bindDlxOrderPayment() {
        return BindingBuilder.bind(queueDlqOrdersDetails())
                .to(deadLetterExchange());
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }
}
