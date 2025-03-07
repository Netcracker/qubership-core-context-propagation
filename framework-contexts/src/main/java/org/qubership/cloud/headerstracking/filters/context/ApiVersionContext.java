package org.qubership.cloud.headerstracking.filters.context;

import org.qubership.cloud.context.propagation.core.ContextManager;
import org.qubership.cloud.framework.contexts.apiversion.ApiVersionContextObject;

import static org.qubership.cloud.framework.contexts.apiversion.ApiVersionProvider.API_VERSION_CONTEXT_NAME;

public class ApiVersionContext {


    public static String get() {
        ApiVersionContextObject apiVersionContextObject = ContextManager.get(API_VERSION_CONTEXT_NAME);
        return apiVersionContextObject.getVersion();
    }

    public static void set(String version) {
        ApiVersionContextObject apiVersionContextObject = new ApiVersionContextObject(version);
        ContextManager.set(API_VERSION_CONTEXT_NAME, apiVersionContextObject);
    }

    public static void clear() {
        ContextManager.clear(API_VERSION_CONTEXT_NAME);
    }
}
