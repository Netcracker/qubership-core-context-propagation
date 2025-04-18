package org.qubership.cloud.context.propagation.sample.snapshot;

import org.qubership.cloud.context.propagation.core.ContextManager;
import org.qubership.cloud.framework.contexts.acceptlanguage.AcceptLanguageContextObject;
import org.qubership.cloud.headerstracking.filters.context.AcceptLanguageContext;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ExecuteWithContextTest {

    @Test
    public void executeWithContextTest() {
        String initialContextValue = "first";
        String newContextValue = "second";
        AcceptLanguageContext.set(initialContextValue);

        Map<String, Object> contextSnapshot = ContextManager.createContextSnapshot();

        AcceptLanguageContext.set(newContextValue);
        assertEquals(newContextValue, ((AcceptLanguageContextObject) ContextManager.get(HttpHeaders.ACCEPT_LANGUAGE)).getAcceptedLanguages());

        ContextManager.executeWithContext(contextSnapshot, () -> {
            assertEquals(initialContextValue, ((AcceptLanguageContextObject) ContextManager.get(HttpHeaders.ACCEPT_LANGUAGE)).getAcceptedLanguages());
            return null;
        });

        assertEquals(newContextValue, ((AcceptLanguageContextObject) ContextManager.get(HttpHeaders.ACCEPT_LANGUAGE)).getAcceptedLanguages());
    }
}
