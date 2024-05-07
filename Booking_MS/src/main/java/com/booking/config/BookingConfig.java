package com.booking.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.booking.constant.Constant;

@Configuration
public class BookingConfig {

	@Bean
    public ModelMapper modelMapperBean() {
        return new ModelMapper();
    }
 
    @Bean
    @LoadBalanced
    public WebClient webClient() {
        return WebClient.builder().baseUrl("http://localhost:8083").build();
    }
    
    @Bean(name = "Inventory")
    @LoadBalanced
    public RestClient restClient1() {
    	return RestClient.builder().baseUrl("http://localhost:8083").build();
    }
    
    
    @Bean(name="Payment")
    @LoadBalanced
    public RestClient restClient2() {
    	return RestClient.builder().baseUrl("http://localhost:8084").build();
    }
    
    @Bean
	public NewTopic bookingOrderTopic() {
		NewTopic newTopic = TopicBuilder.name(Constant.BOOKING_ORDER_TOPIC)
					.build();
		return newTopic;
		
	}
    
    
}
