package com.petstore.qa.common.clients;


import com.petstore.qa.common.clients.factory.ClientFactory;
import com.petstore.qa.common.config.ClientConfiguration;
import com.petstore.qa.common.config.ClientConfigurationImpl;
import com.petstore.qa.common.config.RequestSpecificConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public abstract class RestClient<Impl extends RestClient<Impl>> {
    private final Client client = createCoreClient();
    private final Map<String, Object> headers = new HashMap<>();
    protected final ClientConfiguration clientConfiguration;
    private RequestSpecificConfig requestSpecificConfig = new RequestSpecificConfig();

    public RestClient() {
        this.clientConfiguration = ClientConfigurationImpl.getInstance();
    }

    protected abstract RequestSpecificConfig getDefaultConfiguration();

    protected abstract Impl getThis();

    protected Client createCoreClient() {
        return ClientFactory.createClient();
    }

    protected <F> ResponseWrapper<F> get(String path, GenericType<F> genericType) {
        return get(path, genericType, new HashMap<>());
    }

    protected <F> ResponseWrapper<F> get(String path, GenericType<F> genericType, Map<String, String> queryParams) {
        return executeRequest(path, genericType, queryParams, "GET", null);
    }

    protected <T, F> ResponseWrapper<F> delete(String path, GenericType<F> genericType) {
        return delete(path, genericType, new HashMap<>(), null);
    }

    protected <F> ResponseWrapper<F> delete(String path, GenericType<F> genericType, Map<String, String> queryParams) {
        return executeRequest(path, genericType, queryParams, "DELETE", null);
    }

    protected <T, F> ResponseWrapper<F> delete(String path, GenericType<F> genericType, T dto) {
        return delete(path, genericType, new HashMap<>(), dto);
    }

    protected <T, F> ResponseWrapper<F> delete(String path, GenericType<F> genericType, Map<String, String> queryParams, T dto) {
        return executeRequest(path, genericType, queryParams, "DELETE", dto);
    }

    protected <T, F> ResponseWrapper<F> post(String path, GenericType<F> genericType, T dto) {
        return post(path, genericType, new HashMap<>(), dto);
    }

    protected <T, F> ResponseWrapper<F> post(String path, GenericType<F> genericType) {
        return post(path, genericType, new HashMap<>(), null);
    }

    protected <T, F> ResponseWrapper<F> post(String path, GenericType<F> genericType, Map<String, String> queryParams, T dto) {
        return executeRequest(path, genericType, queryParams, "POST", dto);
    }

    protected <F> ResponseWrapper<F> put(String path, GenericType<F> genericType, Map<String, String> queryParams) {
        return executeRequest(path, genericType, queryParams, "PUT", null);
    }

    protected <T, F> ResponseWrapper<F> put(String path, GenericType<F> genericType, T dto) {
        return put(path, genericType, new HashMap<>(), dto);
    }

    protected <T, F> ResponseWrapper<F> put(String path, GenericType<F> genericType) {
        return put(path, genericType, new HashMap<>(), null);
    }


    protected <T, F> ResponseWrapper<F> put(String path, GenericType<F> genericType, Map<String, String> queryParams, T dto) {
        return executeRequest(path, genericType, queryParams, "PUT", dto);
    }

    protected <T, F> ResponseWrapper<F> patch(String path, GenericType<F> genericType, T dto) {
        return patch(path, genericType, new HashMap<>(), dto);
    }

    protected <T, F> ResponseWrapper<F> patch(String path, GenericType<F> genericType, Map<String, String> queryParams, T dto) {
        return executeRequest(path, genericType, queryParams, "PATCH", dto);
    }

    public void addHeader(String key, Object value) {
        headers.put(key, value);
    }

    protected Object getHeader(String key) {
        return headers.get(key);
    }

    public Impl usingAccept(String accept) {
        requestSpecificConfig = requestSpecificConfig.withAccept(accept);
        return getThis();
    }

    public Impl usingContentType(String contentType) {
        requestSpecificConfig = requestSpecificConfig.withContentType(contentType);
        return getThis();
    }

    public Impl usingContentType(MediaType contentType) {
        return usingContentType(contentType.toString());
    }

    public Impl usingAuthHeader(String authHeader) {
        requestSpecificConfig = requestSpecificConfig.withAuthHeader("Bearer " + authHeader);
        return getThis();
    }

    protected <T, F> ResponseWrapper<F> executeRequest(
            String path,
            GenericType<F> genericType,
            Map<String, String> queryParams,
            String method,
            T dto) {
        var specificConfig = getDefaultConfiguration();
        specificConfig.overrideValues(requestSpecificConfig);
        var configuration = new RequestSpecificConfig(specificConfig);

        var target = client.target(configuration.getServicePath()).path(path);
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            if (entry.getValue().contains(";")) {
                String[] values = entry.getValue().split(";");
                for (int i = 0; i < values.length; i++) {
                    target = target.queryParam(entry.getKey(), values[i]);
                }
            } else {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }

        var builder = target.request();
        if (configuration.getAuthHeader() != null && !configuration.getAuthHeader().isEmpty()) {
            builder.header(HttpHeaders.AUTHORIZATION, configuration.getAuthHeader());
        }
        if (configuration.getAccept() != null && !configuration.getAccept().isEmpty()) {
            builder.header(HttpHeaders.ACCEPT, configuration.getAccept());
        }
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }
        headers.clear();

        var invocation = builder.build(method, Entity.entity(dto, configuration.getContentType()));
        var response = invocation.invoke();

        return new ResponseWrapper<>(response, genericType, path);
    }
}
