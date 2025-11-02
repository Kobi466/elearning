package com.kobi.elearning.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

@Configuration
public class KafkaConfig {

	@Value("${spring.kafka.properties.schema.registry.url}")
	private String schemaRegistryUrl;

	@Bean
	public Serializer<Object> kafkaAvroSerializer() {
		Serializer<Object> serializer = new KafkaAvroSerializer();
		Map<String, String> config = new HashMap<>();
		config.put("schema.registry.url", schemaRegistryUrl);
		serializer.configure(config, false);
		return serializer;
	}
}
