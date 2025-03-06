package org.qubership.cloud.framework.contexts.acceptlanuages;

import org.qubership.cloud.context.propagation.core.ContextManager;
import org.qubership.cloud.context.propagation.core.RequestContextPropagation;
import org.qubership.cloud.framework.contexts.acceptlanguage.AcceptLanguageContextObject;
import org.qubership.cloud.framework.contexts.acceptlanguage.AcceptLanguageProvider;
import org.qubership.cloud.framework.contexts.data.ContextDataRequest;
import org.qubership.cloud.framework.contexts.data.ContextDataResponse;
import org.qubership.cloud.framework.contexts.data.SimpleIncomingContextData;
import org.qubership.cloud.framework.contexts.helper.AbstractContextTestWithProperties;
import org.qubership.cloud.headerstracking.filters.context.AcceptLanguageContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.qubership.cloud.framework.contexts.data.ContextDataRequest.CUSTOM_HEADER;
import static jakarta.ws.rs.core.HttpHeaders.ACCEPT_LANGUAGE;

class AcceptLanguageContextObjectPropagationTest extends AbstractContextTestWithProperties {

    private final String TEST_LANGUAGES = "en; ru;";

    static {
        propertiesToSet.put("headers.allowed", CUSTOM_HEADER);
    }

    @Test
    public void testAcceptLanguageContextPropagation() {
        RequestContextPropagation.initRequestContext(new ContextDataRequest());
        Assertions.assertNotNull(ContextManager.get(ACCEPT_LANGUAGE));
        AcceptLanguageContextObject acceptLanguageContextObject = ContextManager.get(ACCEPT_LANGUAGE);
        Assertions.assertEquals(TEST_LANGUAGES, acceptLanguageContextObject.getAcceptedLanguages());
        ContextDataResponse responseContextData = new ContextDataResponse();
        RequestContextPropagation.populateResponse(responseContextData);
        Assertions.assertEquals(TEST_LANGUAGES, responseContextData.getResponseHeaders().get(ACCEPT_LANGUAGE));
        ContextManager.clear(ACCEPT_LANGUAGE);
        Assertions.assertNotNull(ContextManager.get(ACCEPT_LANGUAGE));
    }

    @Test
    public void testAcceptLanguageContextWrapper() {
        RequestContextPropagation.initRequestContext(new ContextDataRequest());
        Assertions.assertEquals(TEST_LANGUAGES, AcceptLanguageContext.get());

        AcceptLanguageContext.set("fr-Bel;");
        Assertions.assertEquals("fr-Bel;", AcceptLanguageContext.get());

        AcceptLanguageContext.clear();
        Assertions.assertNull(AcceptLanguageContext.get());
    }

    @Test
    public void testAcceptLanguageSerializableDataFromCxtManager() {
        RequestContextPropagation.initRequestContext(new ContextDataRequest());

        Map<String, Map<String, Object>> serializableContextData = ContextManager.getSerializableContextData();

        Assertions.assertTrue(serializableContextData.containsKey(AcceptLanguageProvider.ACCEPT_LANGUAGE));
    }

    @Test
    public void testAcceptLanguageSerializableData() {
        SimpleIncomingContextData contextData = new SimpleIncomingContextData(Map.of(ACCEPT_LANGUAGE, "en; ru;"));
        AcceptLanguageContextObject acceptLanguageContextObject = new AcceptLanguageContextObject(contextData);

        Map<String, Object> serializableContextData = acceptLanguageContextObject.getSerializableContextData();

        Assertions.assertEquals(1, serializableContextData.size());
        Assertions.assertEquals("en; ru;", serializableContextData.get(ACCEPT_LANGUAGE));

        AcceptLanguageContextObject acceptLanguageContextObject2 = new AcceptLanguageContextObject(new SimpleIncomingContextData());
        Assertions.assertEquals(0, acceptLanguageContextObject2.getSerializableContextData().size());
    }
}