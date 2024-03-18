package com.petstore.qa.common.config;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
public class RequestSpecificConfig {
    private String servicePath;
    private String contentType;
    private String authHeader;
    private String accept;

    public RequestSpecificConfig(RequestSpecificConfig other) {
        this(other.servicePath, other.contentType, other.authHeader, other.accept);
    }

    public RequestSpecificConfig(String servicePath, String contentType, String authHeader) {
        this.servicePath = servicePath;
        this.contentType = contentType;
        this.authHeader = authHeader;
    }

    public RequestSpecificConfig(String servicePath, String contentType, String accept, String authHeader) {
        this.servicePath = servicePath;
        this.contentType = contentType;
        this.accept = accept;
        this.authHeader = authHeader;
    }

    public RequestSpecificConfig withContentType(String contentType) {
        RequestSpecificConfig configuration = new RequestSpecificConfig(this);
        configuration.contentType = contentType;
        return configuration;
    }

    public RequestSpecificConfig withAuthHeader(String authHeader) {
        RequestSpecificConfig configuration = new RequestSpecificConfig(this);
        configuration.authHeader = authHeader;
        return configuration;
    }

    public RequestSpecificConfig withAccept(String accept) {
        RequestSpecificConfig configuration = new RequestSpecificConfig(this);
        configuration.accept = accept;
        return configuration;
    }

    public void overrideValues(RequestSpecificConfig values) {
        this.servicePath = values.getServicePath() != null ? values.servicePath : this.servicePath;
        this.contentType = values.contentType != null ? values.contentType : this.contentType;
        this.authHeader = values.authHeader != null ? values.authHeader : this.authHeader;
        this.accept = values.accept != null ? values.accept : this.accept;
    }
}
