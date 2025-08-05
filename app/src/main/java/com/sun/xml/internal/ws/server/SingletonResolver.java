package com.sun.xml.internal.ws.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.server.AbstractInstanceResolver;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.server.WSWebServiceContext;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/SingletonResolver.class */
public final class SingletonResolver<T> extends AbstractInstanceResolver<T> {

    @NotNull
    private final T singleton;

    public SingletonResolver(@NotNull T singleton) {
        this.singleton = singleton;
    }

    @Override // com.sun.xml.internal.ws.api.server.InstanceResolver
    @NotNull
    public T resolve(Packet request) {
        return this.singleton;
    }

    @Override // com.sun.xml.internal.ws.api.server.InstanceResolver
    public void start(WSWebServiceContext wsc, WSEndpoint endpoint) {
        getResourceInjector(endpoint).inject(wsc, this.singleton);
        invokeMethod(findAnnotatedMethod(this.singleton.getClass(), PostConstruct.class), this.singleton, new Object[0]);
    }

    @Override // com.sun.xml.internal.ws.api.server.InstanceResolver
    public void dispose() {
        invokeMethod(findAnnotatedMethod(this.singleton.getClass(), PreDestroy.class), this.singleton, new Object[0]);
    }
}
