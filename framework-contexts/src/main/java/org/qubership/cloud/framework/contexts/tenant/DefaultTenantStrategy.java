package org.qubership.cloud.framework.contexts.tenant;

import org.qubership.cloud.framework.contexts.strategies.AbstractTenantStrategy;
import org.qubership.cloud.context.propagation.core.Strategy;
import org.qubership.cloud.context.propagation.core.supports.strategies.DefaultStrategies;
import java.util.function.Supplier;

public class DefaultTenantStrategy extends AbstractTenantStrategy {

    private final Strategy<TenantContextObject> strategy;

    public DefaultTenantStrategy(Supplier<TenantContextObject> defaultContextObject) {
        strategy = DefaultStrategies.threadLocalWithInheritanceDefaultStrategy(defaultContextObject);
    }

    @Override
    public Strategy<TenantContextObject> getStrategy() {
        return strategy;
    }
}
