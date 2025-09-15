package com.netcracker.cloud.headerstracking.filters.context;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import com.netcracker.cloud.context.propagation.core.RequestContextPropagation;
import com.netcracker.cloud.framework.contexts.data.ContextDataRequest;

@Slf4j
public abstract class AbstractContextTest {
    public static final String CUSTOM_HEADER = "Custom-header-1";

    @BeforeEach
    void setup() {
        System.out.println("TEST BeforeEach started");
        System.setProperty("headers.allowed", CUSTOM_HEADER);
        RequestContextPropagation.initRequestContext(new ContextDataRequest());
        System.out.println("TEST BeforeEach finished");
    }
}
