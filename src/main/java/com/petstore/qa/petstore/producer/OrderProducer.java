package com.petstore.qa.petstore.producer;

import com.github.javafaker.Faker;
import com.petstore.qa.petstore.dto.Order;
import com.petstore.qa.petstore.dto.OrderStatus;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@UtilityClass
public class OrderProducer {
    public Order produce() {
        var faker = new Faker();
        return Order.builder().id(faker.number().numberBetween(1000, 10000))
                .petId(faker.number().numberBetween(1000, 10000))
                .quantity(faker.number().numberBetween(1000, 10000))
                .shipDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
                .status(OrderStatus.completed)
                .complete(true)
                .build();
    }
}
