package com.Payment.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.Payment.constant.Constant;

@Configuration
public class PaymentConfig {

	@Bean
	public NewTopic createPaymentTopic() {
		NewTopic newTopic = TopicBuilder.name(Constant.PAYMENT_CREATED_TOPIC)
					.build();
		return newTopic;
		
	}
	
	@Bean
	public NewTopic bookingReverseTopic() {
		NewTopic newTopic = TopicBuilder.name(Constant.REVERSE_BOOKING_ORDER_TOPIC)
					.build();
		return newTopic;
		
	}
	
	
}
