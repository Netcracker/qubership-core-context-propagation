package org.qubership.cloud.context.propagation.sample.threads;

import org.qubership.cloud.context.propagation.core.ContextManager;
import org.qubership.cloud.framework.contexts.allowedheaders.AllowedHeadersContextObject;
import org.qubership.cloud.headerstracking.filters.context.AllowedHeadersContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;


public class AllowedHeadersThreadsPropagationTest extends AbstractThreadTest {
    private Map<String, String> HEADERS = new HashMap<>();
    public static final String ALLOWED_HEADER = "allowed_header";
    final Runnable runnableWithAllowedHeaders = () -> assertEquals(HEADERS, AllowedHeadersContext.getHeaders());
    final Runnable runnableWithoutAllowedHeaders = () -> assertEquals(Collections.emptyMap(), AllowedHeadersContext.getHeaders());

    @Before
    public void setUp() {
        HEADERS.put("header", "value");
    }

    @Test
    public void testPropagationForAllowedHeaders() throws Exception {
        AllowedHeadersContext.set(HEADERS);
        simpleExecutor.submit(runnableWithAllowedHeaders).get();
    }

    @Test
    public void testNoPropagationForAllowedHeaders() throws Exception {
        simpleExecutor.submit(runnableWithoutAllowedHeaders).get();
    }

    @Test
    public void childThreadDoesntAffectParentOne() {
        ContextManager.set(ALLOWED_HEADER, new AllowedHeadersContextObject(HEADERS));

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            HashMap<String, String> tmpHeaders = new HashMap<>();
            tmpHeaders.put("test", "test");
            ContextManager.set(ALLOWED_HEADER, new AllowedHeadersContextObject(tmpHeaders));
        });
        executor.shutdown();
        assertEquals(HEADERS, ((AllowedHeadersContextObject) ContextManager.get(ALLOWED_HEADER)).getHeaders());
    }
}
