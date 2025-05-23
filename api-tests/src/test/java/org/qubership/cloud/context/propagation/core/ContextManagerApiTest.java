package org.qubership.cloud.context.propagation.core;

import org.qubership.cloud.ContextPropagationHelperTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.qubership.cloud.context.propagation.core.ContextManager.CORE_CONTEXTPROPAGATION_PROVIDERS_LOOKUP;
import static org.qubership.cloud.context.propagation.core.ContextManager.LOOKUP_CONTEXT_PROVIDERS_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContextManagerApiTest {

    private final String CONTEXT_NAME = "context-name";

    @Before
    public void start() throws Exception {
        ContextPropagationHelperTest.clearRegistry();
    }

    @Test
    public void checkCoreContextpropagationProvidersLookupProperty() {
        assertEquals("Value of CORE_CONTEXTPROPAGATION_PROVIDERS_LOOKUP must be core.contextpropagation.providers.lookup otherwise it breaks backward compatibility!",
                CORE_CONTEXTPROPAGATION_PROVIDERS_LOOKUP,
                "core.contextpropagation.providers.lookup"
        );
    }

    @Test
    public void checkLookupContextProvidersPath() {
        assertEquals(
                "Value of LOOKUP_CONTEXT_PROVIDERS_PATH must be context_propagation.context_providers.path otherwise it breaks backward compatibility!",
                LOOKUP_CONTEXT_PROVIDERS_PATH,
                "context_propagation.context_providers.path"
        );
    }

    private Method getContextManagerMethod(String methodName, Class<?>... parameterTypes) {
        Class<ContextManager> contextManagerClass = ContextManager.class;
        Method getContextProvidersMethod = null;
        try {
            getContextProvidersMethod = contextManagerClass.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            Assert.fail(String.format("method with name '%s' does not exist in ContextManager class", methodName));
        }
        Assert.assertNotNull(getContextProvidersMethod);
        return getContextProvidersMethod;
    }

    private String getGenericName(Type genericReturnType) {
        ParameterizedType pType = (ParameterizedType) genericReturnType;
        return pType.getActualTypeArguments()[0].getTypeName();
    }

    private Type getReturnedType(Method getContextProvidersMethod) {
        return getContextProvidersMethod.getGenericReturnType();
    }

    @Test
    public void checkGetContextProvidersSignature() {
        Method getContextProvidersMethod = getContextManagerMethod("getContextProviders");
        Type returnedType = getReturnedType(getContextProvidersMethod);

        assertEquals(Collection.class.getName(), getContainerReturnedType(returnedType).getTypeName());

        String genericTypeName = getGenericName(returnedType);

        assertEquals("org.qubership.cloud.context.propagation.core.ContextProvider<?>", genericTypeName);
    }

    @Test
    public void testGetContextProvidersAPI() {
        Collection<ContextProvider<?>> contextProviders = ContextManager.getContextProviders();
        assertNotNull(contextProviders);
    }

    private Type getContainerReturnedType(Type returnedType) {
        return ((ParameterizedType) returnedType).getRawType();
    }

    @Test
    public void checkRegisterSignature() {
        Method registerMethod = getContextManagerMethod("register", List.class);

        Type returnedType = getReturnedType(registerMethod);
        assertEquals(Collection.class.getName(), getContainerReturnedType(returnedType).getTypeName());

        String genericTypeName = getGenericName(returnedType);
        assertEquals("org.qubership.cloud.context.propagation.core.ContextProvider<?>", genericTypeName);

        assertEquals(1, registerMethod.getParameterCount());
        assertEquals(List.class, registerMethod.getParameterTypes()[0]);
    }

    @Test
    public void testRegisterAPI() {
        ContextProvider<?> contextProvider = mock(ContextProvider.class);
        Collection<ContextProvider<?>> register = ContextManager.register(Collections.singletonList(contextProvider)); // API
        assertNotNull(register);
    }

    @Test
    public void checkSetSignature() {
        Method registerMethod = getContextManagerMethod("set", String.class, Object.class);

        Type returnedType = getReturnedType(registerMethod);
        assertEquals("void", returnedType.getTypeName());
    }

    @Test
    public void testSetAPI() {
        createAndRegisterTestContext();
        ContextManager.set(CONTEXT_NAME, "context-value"); // API
    }

    @Test
    public void checkGetSignature() {
        Method registerMethod = getContextManagerMethod("get", String.class);

        Type returnedType = getReturnedType(registerMethod);
        assertEquals("V", returnedType.getTypeName());
    }

    @Test
    public void testGetAPI() {
        Strategy mockStrategy = createAndRegisterTestContext();
        when(mockStrategy.get()).thenReturn("context-value");

        Object contextValue = ContextManager.get(CONTEXT_NAME); // API
        assertEquals("context-value", contextValue);
    }

    @Test
    public void checkGetSafeSignature() {
        Method registerMethod = getContextManagerMethod("getSafe", String.class);

        Type returnedType = getReturnedType(registerMethod);
        assertEquals("java.util.Optional<V>", returnedType.getTypeName());
    }

    @Test
    public void testGetSafeAPI() {
        Strategy mockStrategy = createAndRegisterTestContext();
        when(mockStrategy.getSafe()).thenReturn(Optional.of("context-value"));

        Optional<String> contextValue = ContextManager.getSafe(CONTEXT_NAME); // API
        assertEquals("context-value", contextValue.get());
    }

    @Test
    public void checkClearSignature() {
        Method registerMethod = getContextManagerMethod("clear", String.class);

        Type returnedType = getReturnedType(registerMethod);
        assertEquals("void", returnedType.getTypeName());
    }

    @Test
    public void testClearAPI() {
        createAndRegisterTestContext();
        ContextManager.clear(CONTEXT_NAME); // API
    }

    @Test
    public void checkClearAllSignature() {
        Method registerMethod = getContextManagerMethod("clearAll");

        Type returnedType = getReturnedType(registerMethod);
        assertEquals("void", returnedType.getTypeName());
    }

    @Test
    public void testClearAllAPI() {
        createAndRegisterTestContext();
        ContextManager.clearAll(); // API
    }

    @Test
    public void checkGetAllSignature() {
        Method registerMethod = getContextManagerMethod("getAll");

        Type returnedType = getReturnedType(registerMethod);
        assertEquals(List.class.getName(), getContainerReturnedType(returnedType).getTypeName());

        String genericTypeName = getGenericName(returnedType);
        assertEquals("java.lang.Object", genericTypeName);

    }

    @Test
    public void testGetAllAPI() {
        createAndRegisterTestContext();
        ContextManager.getAll(); // API
    }

    @Test
    public void checkNewScopeSignature() {
        Method method = getContextManagerMethod("newScope", String.class, Object.class);

        Type returnedType = getReturnedType(method);
        assertEquals(Scope.class.getName(), returnedType.getTypeName());
    }

    @Test
    public void testNewScopeAPI() {
        createAndRegisterTestContext();
        Scope new_value = ContextManager.newScope(CONTEXT_NAME, "new value"); // API
    }

    @Test
    public void testCreateContextSnapshotAPI() {
        createAndRegisterTestContext();
        Map<String, Object> contextSnapshot = ContextManager.createContextSnapshot(Collections.singleton(CONTEXT_NAME));// API
    }

    @Test
    public void testCreateContextSnapshotAPIWithExcludedContexts() {
        createAndRegisterTestContext();
        Map<String, Object> contextSnapshot = ContextManager.createContextSnapshotWithoutContexts(Collections.singleton(CONTEXT_NAME));// API
    }

    @Test
    public void testActivateContextSnapshotAPI() {
        createAndRegisterTestContext();
        Map<String, Object> contextSnapshot = new HashMap<>();
        contextSnapshot.put(CONTEXT_NAME, "snapshot context name");
        ContextManager.activateContextSnapshot(contextSnapshot); // API
    }

    @Test
    public void testExecuteWithContextAPI() {
        createAndRegisterTestContext();
        Map<String, Object> contextSnapshot = new HashMap<>();
        contextSnapshot.put(CONTEXT_NAME, "snapshot context name");
        Object supplierObject = ContextManager.executeWithContext(contextSnapshot, () -> null);// API
    }

    @Test
    public void testCreateContextSnapshotWithoutParamsAPI() {
        createAndRegisterTestContext();
        Map<String, Object> contextSnapshot = ContextManager.createContextSnapshot();// API
    }

    private Strategy createAndRegisterTestContext() {
        ContextProvider<?> mockContextProvider = mock(ContextProvider.class);
        when(mockContextProvider.contextName()).thenReturn(CONTEXT_NAME);
        Strategy mockStrategy = mock(Strategy.class);
        when(mockContextProvider.strategy()).thenReturn(mockStrategy);
        ContextManager.register(Collections.singletonList(mockContextProvider));
        return mockStrategy;
    }
}

