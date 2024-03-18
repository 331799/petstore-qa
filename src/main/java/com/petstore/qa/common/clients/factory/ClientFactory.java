package com.petstore.qa.common.clients.factory;


import io.qameta.allure.jaxrs.AllureJaxRs;
import lombok.experimental.UtilityClass;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.cfg.Annotations;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

@UtilityClass
public final class ClientFactory {
    public Client createClient() {
        var provider = new JacksonJaxbJsonProvider();
        provider.setAnnotationsToUse(new Annotations[]{Annotations.JACKSON});

        var config = new ClientConfig(provider);
        config.connectorProvider(new ApacheConnectorProvider());
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        config.property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE);
        config.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);

        var logger = Logger.getLogger("Client");
        var feature = new LoggingFeature(logger, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY,
                null);

        SSLContext sslcontext;
        try {
            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new java.security.SecureRandom());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return ClientBuilder.newBuilder()
                .withConfig(config)
                .sslContext(sslcontext)
                .build()
                .register(feature)
                .register(MultiPartFeature.class)
                .register(AllureJaxRs.class);
    }
}
