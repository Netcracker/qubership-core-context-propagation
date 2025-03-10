package org.qubership.cloud.context.propagation.core.providers.xversion;

import org.qubership.cloud.context.propagation.core.ContextProvider;
import org.qubership.cloud.context.propagation.core.RegisterProvider;
import org.qubership.cloud.context.propagation.core.Strategy;
import org.qubership.cloud.context.propagation.core.contextdata.IncomingContextData;
import org.jetbrains.annotations.Nullable;

@RegisterProvider
public class XVersionProvider implements ContextProvider<XVersionContextObject> {

    public static final String CONTEXT_NAME = "x-version";
    XVersionStrategy xVersionStrategy = new XVersionStrategy();

    @Override
    public Strategy<XVersionContextObject> strategy() {
        return xVersionStrategy;
    }

    @Override
    public int initLevel() {
        return 0;
    }

    @Override
    public int providerOrder() {
        return 0;
    }

    @Override
    public final String contextName() {
        return CONTEXT_NAME;
    }

    @Override
    public XVersionContextObject provide(@Nullable IncomingContextData contextData) {
        return new XVersionContextObject(contextData);
    }

}
