package com.petstore.qa.petstore.client;

import com.petstore.qa.common.clients.ResponseWrapper;
import com.petstore.qa.common.clients.RestClient;
import com.petstore.qa.common.config.RequestSpecificConfig;
import com.petstore.qa.petstore.dto.Order;
import io.qameta.allure.Step;

import javax.ws.rs.core.GenericType;
import java.util.Map;

public class StoreClient extends RestClient<StoreClient> {
    private final static String CREATE_ORDER = "/v2/store/order";
    private final static String ORDER = "/v2/store/order/%s";
    private final static String INVENTORY = "/v2/store/inventory";

    @Override
    protected RequestSpecificConfig getDefaultConfiguration() {
        return new RequestSpecificConfig(clientConfiguration.apiUrl(), "application/json", "application/json");
    }

    @Override
    protected StoreClient getThis() {
        return this;
    }

    @Step("Create order")
    public ResponseWrapper<Order> createOrder(Order order) {
        return post(CREATE_ORDER, new GenericType<>(Order.class), order);
    }

    @Step("Get order by id {0}")
    public ResponseWrapper<Order> getOrderById(Integer orderId) {
        return get(ORDER.formatted(orderId), new GenericType<>(Order.class));
    }

    @Step("Delete order {0}")
    public ResponseWrapper<Void> deleteOrder(Integer orderId) {
        return delete(ORDER.formatted(orderId), new GenericType<>(Void.class));
    }

    @Step("Get inventory")
    public ResponseWrapper<Map<String, Object>> getInventory() {
        return get(INVENTORY, new GenericType<>() {});
    }

}
