package ru.home.kafka;

import org.springframework.stereotype.Component;
import ru.home.kafka.dto.Message;
import ru.home.kafka.dto.MotherMessage;
import ru.home.kafka.dto.OrderStatus;
import ru.home.kafka.dto.OruMessage;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;


@Component
public class OrderFactory {


    public List<Message> getMapiOkOrder() {
        var order = MotherMessage.builder()
                .orderId(RandomGenerator.getDefault().nextLong())
                .amount(BigDecimal.valueOf(RandomGenerator.getDefault().nextDouble(10000.0)))
                .status(OrderStatus.OK)
                .build();
        return Collections.singletonList(order);
    }


    public List<Message> getMapiErrorOrder() {
        var order = MotherMessage.builder()
                .orderId(RandomGenerator.getDefault().nextLong())
                .amount(BigDecimal.valueOf(RandomGenerator.getDefault().nextDouble(10000.0)))
                .status(OrderStatus.ERROR)
                .build();
        return Collections.singletonList(order);
    }


    public List<Message> getOruOkOrder() {
        var mapi = (MotherMessage) getMapiOkOrder().get(0);
        var oru1 = OruMessage.builder()
                .orderId(mapi.getOrderId())
                .amount(mapi.getAmount())
                .status(OrderStatus.PROCESSING)
                .serviceName(getServiceName() )
                .build();

        var oru2 = OruMessage.builder()
                .orderId(mapi.getOrderId())
                .status(OrderStatus.SOMETHING)
                .partnerName(getPartnerName())
                .build();

        var oru3 = OruMessage.builder()
                .orderId(mapi.getOrderId())
                .status(OrderStatus.DONE)
                .build();

        return List.of(mapi, oru1, oru2, oru3);
    }

    public List<Message> getOruErrorOrder() {
        var mapi = (MotherMessage) getMapiOkOrder().get(0);
        var oru1 = OruMessage.builder()
                .orderId(mapi.getOrderId())
                .amount(mapi.getAmount())
                .status(OrderStatus.PROCESSING)
                .serviceName(getServiceName())
                .build();


        var oru2 = OruMessage.builder()
                .orderId(mapi.getOrderId())
                .status(OrderStatus.SOMETHING)
                .build();


        return List.of(mapi, oru1, oru2);
    }


    private String getServiceName() {
      return "Service-" + RandomGenerator.getDefault().nextInt(4);
    }

    private String getPartnerName() {
        return "Partner-" + RandomGenerator.getDefault().nextInt(10);
    }

}
