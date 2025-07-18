package org.qubership.cloud.contexts.apiversion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.qubership.cloud.context.propagation.core.ContextManager;
import org.qubership.cloud.context.propagation.core.RequestContextPropagation;
import org.qubership.cloud.context.propagation.core.contextdata.IncomingContextData;
import org.qubership.cloud.context.propagation.core.contexts.common.RequestProvider;
import org.qubership.cloud.contexts.IncomingContextDataFactory;
import org.qubership.cloud.framework.contexts.apiversion.ApiVersionContextObject;
import org.qubership.cloud.framework.contexts.apiversion.ApiVersionProvider;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ApiVersionContextObjectApiTest {

    @BeforeEach
    void setup() {
        ContextManager.register(Collections.singletonList(new RequestProvider()));
    }

    @Test
    void testDefaultApiVersion() {
        ApiVersionContextObject apiVersionContextObject = new ApiVersionContextObject((IncomingContextData) null);
        assertEquals("v1", apiVersionContextObject.getVersion());
    }

    @Test
    void testApiVersionFromIncomingContextData() {
        ApiVersionContextObject apiVersionContextObject = new ApiVersionContextObject(
                IncomingContextDataFactory.getApiVersionIncomingContextData());
        String expected = "v2";
        assertEquals(expected, apiVersionContextObject.getVersion());
    }

    @Test
    void testConstructorWithApiVersionParameter() {
        ApiVersionContextObject apiVersionContextObject = new ApiVersionContextObject("v2");
        assertEquals("v2", apiVersionContextObject.getVersion());
    }

    @Test
    void testGetApiVersionFromContextManager() {
        ContextManager.register(Collections.singletonList(new ApiVersionProvider()));
        RequestContextPropagation.initRequestContext(IncomingContextDataFactory.getApiVersionIncomingContextData());
        ApiVersionContextObject apiVersionContextObject = ContextManager.get(ApiVersionProvider.API_VERSION_CONTEXT_NAME); // API

        assertEquals("v2", apiVersionContextObject.getVersion());

        RequestContextPropagation.initRequestContext(null);
        apiVersionContextObject = ContextManager.get(ApiVersionProvider.API_VERSION_CONTEXT_NAME); // API

        String expectedDefaultValue = "v1";
        assertEquals(expectedDefaultValue, apiVersionContextObject.getVersion());

    }
}
