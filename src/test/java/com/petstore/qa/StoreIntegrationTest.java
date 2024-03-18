package com.petstore.qa;

import com.petstore.qa.petstore.client.StoreClient;
import com.petstore.qa.petstore.producer.OrderProducer;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StoreIntegrationTest extends BaseTest {

    @Test
    public void orderTest() {
        var createOrderRequest = OrderProducer.produce();
        var storeClient = new StoreClient();
        var createOrderResponse = storeClient.createOrder(createOrderRequest)
                .expectingStatusCode(200)
                .readEntity();
        assertThat(createOrderRequest).isEqualTo(createOrderResponse);

        var getOrderResponse = storeClient.getOrderById(createOrderResponse.getId())
                .expectingStatusCode(200)
                .readEntity();
        assertThat(getOrderResponse).isEqualTo(createOrderRequest);

        storeClient.deleteOrder(createOrderResponse.getId())
                .expectingStatusCode(200);

        storeClient.getOrderById(createOrderRequest.getId())
                .expectingStatusCode(404);
    }
}
