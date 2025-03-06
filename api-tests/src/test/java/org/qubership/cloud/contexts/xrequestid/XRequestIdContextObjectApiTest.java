package org.qubership.cloud.contexts.xrequestid;

import org.qubership.cloud.context.propagation.core.ContextManager;
import org.qubership.cloud.context.propagation.core.RequestContextPropagation;
import org.qubership.cloud.context.propagation.core.contextdata.IncomingContextData;
import org.qubership.cloud.context.propagation.core.contexts.common.RequestProvider;
import org.qubership.cloud.contexts.IncomingContextDataFactory;
import org.qubership.cloud.framework.contexts.xrequestid.XRequestIdContextObject;
import org.qubership.cloud.framework.contexts.xrequestid.XRequestIdContextProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.*;

public class XRequestIdContextObjectApiTest {

    @Before
    public void setup() {
        ContextManager.register(Collections.singletonList(new RequestProvider()));
    }

    @Test
    public void testDefaultXRequestId() {
        XRequestIdContextObject xRequestIdContextObject = new XRequestIdContextObject((IncomingContextData) null);
        assertNotNull(xRequestIdContextObject.getRequestId());
    }

    @Test
    public void testXRequestIdFromIncomingContextData() {
        IncomingContextData xRequestIdIncomingContextData = IncomingContextDataFactory.getXRequestIdIncomingContextData();
        XRequestIdContextObject xRequestIdContextObject = new XRequestIdContextObject(xRequestIdIncomingContextData);
        String expectedValue = (String) xRequestIdIncomingContextData.get("X-Request-Id");
        assertEquals(expectedValue, xRequestIdContextObject.getRequestId());
    }

    @Test
    public void testConstructorWithXRequestIdParameter() {
        String customRequestId = UUID.randomUUID().toString();
        XRequestIdContextObject xRequestIdContextObject = new XRequestIdContextObject(customRequestId);
        assertEquals(customRequestId, xRequestIdContextObject.getRequestId());
    }

    @Test
    public void testGetXRequestIdFromContextManager() {
        ContextManager.register(Collections.singletonList(new XRequestIdContextProvider()));
        IncomingContextData xRequestIdIncomingContextData = IncomingContextDataFactory.getXRequestIdIncomingContextData();
        RequestContextPropagation.initRequestContext(xRequestIdIncomingContextData);
        XRequestIdContextObject xRequestIdContextObject = ContextManager.get(XRequestIdContextProvider.X_REQUEST_ID_CONTEXT_NAME); // API

        assertEquals(xRequestIdIncomingContextData.get("X-Request-Id"), xRequestIdContextObject.getRequestId());

        RequestContextPropagation.initRequestContext(null);
        xRequestIdContextObject = ContextManager.get(XRequestIdContextProvider.X_REQUEST_ID_CONTEXT_NAME); // API

        assertNotNull(xRequestIdContextObject.getRequestId());
        assertNotEquals(xRequestIdIncomingContextData.get("X-Request-Id"), xRequestIdContextObject.getRequestId());

    }
}
