package ru.home.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OruMessage implements Message {
    private long orderId;

    private OrderStatus status;

    private BigDecimal amount;

    private String partnerName;

    private String serviceName;


}
