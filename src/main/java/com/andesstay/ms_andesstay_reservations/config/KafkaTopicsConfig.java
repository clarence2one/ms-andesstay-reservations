package com.andesstay.ms_andesstay_reservations.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * El productor declara reservations.events con las mismas particiones, réplicas y
 * políticas de retención que declaran audit y report. Así el tópico existe aunque el
 * primer mensaje se publique antes de que arranquen los consumidores.
 */
@Configuration
public class KafkaTopicsConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaTopicsConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public NewTopic reservationsEventsTopic() {
        return new NewTopic(kafkaProperties.topicReservationsEvents(),
                kafkaProperties.partitions(), (short) kafkaProperties.replicationFactor())
                .configs(Map.of(
                        "retention.ms", Long.toString(kafkaProperties.reservationsRetentionMs()),
                        "retention.bytes", Long.toString(kafkaProperties.reservationsRetentionBytes()),
                        "cleanup.policy", kafkaProperties.cleanupPolicy(),
                        "min.insync.replicas", Integer.toString(kafkaProperties.minInsyncReplicas())
                ));
    }
}
