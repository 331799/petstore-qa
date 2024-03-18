package com.petstore.qa.common.clients;


import lombok.Getter;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

@Getter
public class ResponseWrapper<T> {
    protected final Response response;
    protected final GenericType<T> genericType;
    protected final String requestLocation;

    ResponseWrapper(Response response, GenericType<T> genericType, String requestLocation) {
        this.response = response;
        this.genericType = genericType;
        this.requestLocation = requestLocation;
    }

    public T readEntity() {
        return response.readEntity(genericType);
    }

    public ResponseWrapper<T> expectingStatusCode(int statusCode) {
        assertThat(response.getStatus()).as("Validate status code").isEqualTo(statusCode);
        return this;
    }

    public ResponseWrapper<T> expectingContentType(String contentType) {
        var mediaType = response.getMediaType();
        assertThat(mediaType.getType() + "/" + mediaType.getSubtype()).as("Validate content type").isEqualTo(contentType);
        return this;
    }
}
