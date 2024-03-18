package com.petstore.qa.common.config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config.properties"})
public interface ClientConfiguration extends Config {
    @Key("api.url")
    String apiUrl();
}