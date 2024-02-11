package ru.home.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.home.kafka.dto.JsonMessage;
import ru.home.kafka.dto.Message;

/**
 * Kafka consumers.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerListeners {

    @KafkaListener(
            // Определяет группу консюмера
            id = "consumer-group-1",
            // Определяет топик откуда читаем
            topics = "${kafka.topics.test-topic}",
            // ВАЖНО: определяет фабрику, которую мы используем. Иначе используется фабрика по умолчанию и многопоточность не работает
            containerFactory = "kafkaListenerContainerFactory")
    public void handle(@Payload Message message) {
        readMessage(message);
    }


    public void readMessage(Message message) {
        long number = message.getOrderId();
        String currentThreadName = Thread.currentThread().getName();
        log.info("Прочитано заказ  с ид: {} в потоке: {}", number, currentThreadName);
        log.info("заказ: {}", message);
    }

}