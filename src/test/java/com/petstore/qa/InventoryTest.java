package com.petstore.qa;

import com.petstore.qa.petstore.client.StoreClient;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryTest extends BaseTest {

    @Test
    public void inventoryTest() {
        var storeClient = new StoreClient();
        var inventory = storeClient.getInventory()
                .expectingStatusCode(200)
                .readEntity();
        assertThat(inventory).isNotEmpty();
    }

}
