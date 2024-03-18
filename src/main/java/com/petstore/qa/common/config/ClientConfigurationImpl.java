package com.petstore.qa.common.config;

import org.aeonbits.owner.ConfigFactory;

public class ClientConfigurationImpl {
    public static ClientConfiguration getInstance() {
        return ConfigFactory.create(ClientConfiguration.class);
    }
}
