package com.netcracker.cloud.headerstracking.filters.context;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AllowedHeadersContextTest extends AbstractContextTest {

    @Test
    void testRequestWithHeader() {
        System.out.println("TEST testRequestWithHeader started");
        assertEquals("custom_value", AllowedHeadersContext.getHeaders().get(CUSTOM_HEADER));
        System.out.println("TEST testRequestWithHeader finished");
    }

    @Test
    void testSetContext() {
        System.out.println("TEST testSetContext started");
        Map<String, String> resultMap = new HashMap<>() {{
            put("header1", "value1");
            put("header2", "value2");
        }};
        AllowedHeadersContext.set(resultMap);
        assertEquals(resultMap, AllowedHeadersContext.getHeaders());
        System.out.println("TEST testSetContext finished");
    }

    @Test
    void testClearContext() {
        System.out.println("TEST testClearContext started");
        assertFalse(AllowedHeadersContext.getHeaders().isEmpty());
        AllowedHeadersContext.clear();
        assertTrue(AllowedHeadersContext.getHeaders().isEmpty());
        System.out.println("TEST testClearContext finished");
    }
}
