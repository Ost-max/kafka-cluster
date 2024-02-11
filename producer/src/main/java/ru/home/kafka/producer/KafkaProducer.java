package ru.home.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.home.kafka.OrderFactory;
import ru.home.kafka.dto.Message;
import ru.home.kafka.dto.MotherMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

@Component
@Slf4j
public class KafkaProducer {

    private final String topic;
    private final OrderFactory orderFactory;

    public KafkaProducer(OrderFactory orderFactory,
                         KafkaTemplate<Object, Object> kafkaTemplate,
                         @Value("${kafka.topics.test-topic}") String topic) {
        this.orderFactory = orderFactory;
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        log.info("topic: {}", topic);
    }

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public void sendMessages() {
        try {
            List<Message> messages = new ArrayList<>();
            int ordersToGenerate = 10;
            for (int i = 0; i < ordersToGenerate; i++) {
                var randCase = RandomGenerator.getDefault().nextInt(4);
                switch (randCase) {
                    case 0 -> messages.addAll(orderFactory.getMapiOkOrder());
                    case 1 -> messages.addAll(orderFactory.getMapiErrorOrder());
                    case 2 -> messages.addAll(orderFactory.getOruOkOrder());
                    case 3 -> messages.addAll(orderFactory.getOruErrorOrder());
                    default -> {
                    }
                }
            }
            System.out.println(messages.size() + " messages generated");
            for (Message message : messages) {
                var sleepTime = RandomGenerator.getDefault().nextLong(10, 300);
                log.info("sleepTime  {}", sleepTime);

                Thread.sleep(sleepTime);

                kafkaTemplate.send(topic, String.valueOf(message.getOrderId()), message);
                log.info("Отправлено сообщение  {}", message);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
