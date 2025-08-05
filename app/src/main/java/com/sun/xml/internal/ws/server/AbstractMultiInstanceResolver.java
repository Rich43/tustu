package com.sun.xml.internal.ws.server;

import com.sun.xml.internal.ws.api.server.AbstractInstanceResolver;
import com.sun.xml.internal.ws.api.server.ResourceInjector;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.server.WSWebServiceContext;
import java.lang.reflect.Method;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/AbstractMultiInstanceResolver.class */
public abstract class AbstractMultiInstanceResolver<T> extends AbstractInstanceResolver<T> {
    protected final Class<T> clazz;
    private WSWebServiceContext webServiceContext;
    protected WSEndpoint owner;
    private final Method postConstructMethod;
    private final Method preDestroyMethod;
    private ResourceInjector resourceInjector;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AbstractMultiInstanceResolver.class.desiredAssertionStatus();
    }

    public AbstractMultiInstanceResolver(Class<T> clazz) {
        this.clazz = clazz;
        this.postConstructMethod = findAnnotatedMethod(clazz, PostConstruct.class);
        this.preDestroyMethod = findAnnotatedMethod(clazz, PreDestroy.class);
    }

    protected final void prepare(T t2) {
        if (!$assertionsDisabled && this.webServiceContext == null) {
            throw new AssertionError();
        }
        this.resourceInjector.inject(this.webServiceContext, t2);
        invokeMethod(this.postConstructMethod, t2, new Object[0]);
    }

    protected final T create() {
        T t2 = (T) createNewInstance(this.clazz);
        prepare(t2);
        return t2;
    }

    @Override // com.sun.xml.internal.ws.api.server.InstanceResolver
    public void start(WSWebServiceContext wsc, WSEndpoint endpoint) {
        this.resourceInjector = getResourceInjector(endpoint);
        this.webServiceContext = wsc;
        this.owner = endpoint;
    }

    protected final void dispose(T instance) {
        invokeMethod(this.preDestroyMethod, instance, new Object[0]);
    }
}
