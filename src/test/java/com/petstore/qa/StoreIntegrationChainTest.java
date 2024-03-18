package com.petstore.qa;

import com.petstore.qa.petstore.client.StoreClient;
import com.petstore.qa.petstore.dto.Order;
import com.petstore.qa.petstore.producer.OrderProducer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StoreIntegrationChainTest extends BaseTest {
    private StoreClient storeClient;
    private Order createOrderRequest;

    @BeforeClass
    public void beforeClass() {
        storeClient = new StoreClient();
        createOrderRequest = OrderProducer.produce();
    }

    @Test
    public void createOrderTest() {
        var createOrderResponse = storeClient.createOrder(createOrderRequest)
                .expectingStatusCode(200)
                .readEntity();
        assertThat(createOrderRequest).isEqualTo(createOrderResponse);
    }

    @Test(dependsOnMethods = "createOrderTest")
    public void getOrderTest() {
        var getOrderResponse = storeClient.getOrderById(createOrderRequest.getId())
                .expectingStatusCode(200)
                .readEntity();
        assertThat(getOrderResponse).isEqualTo(createOrderRequest);
    }

    @Test(dependsOnMethods = "getOrderTest")
    public void deleteOrderTest() {
        storeClient.deleteOrder(createOrderRequest.getId())
                .expectingStatusCode(200);

        storeClient.getOrderById(createOrderRequest.getId())
                .expectingStatusCode(404);
    }

}
