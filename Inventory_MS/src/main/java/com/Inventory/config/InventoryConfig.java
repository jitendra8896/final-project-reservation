package com.Inventory.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.Inventory.constant.*;

@Configuration
public class InventoryConfig {

	@Bean
	public NewTopic reversePaymentTopic() {
		NewTopic newTopic = TopicBuilder.name(Constant.REVERSE_PAYMENT_CREATED_TOPIC)
					.build();
		return newTopic;
		
	}
	
	@Bean
	public NewTopic updateBookingStatuTopic() {
		NewTopic newTopic = TopicBuilder.name(Constant.UPDATE_BOOKING_STATUS_TOPICE)
					.build();
		return newTopic;
		
	}
}
