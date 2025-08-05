package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/ContainerResolver.class */
public abstract class ContainerResolver {
    private static final ThreadLocalContainerResolver DEFAULT = new ThreadLocalContainerResolver();
    private static volatile ContainerResolver theResolver = DEFAULT;

    @NotNull
    public abstract Container getContainer();

    public static void setInstance(ContainerResolver resolver) {
        if (resolver == null) {
            resolver = DEFAULT;
        }
        theResolver = resolver;
    }

    @NotNull
    public static ContainerResolver getInstance() {
        return theResolver;
    }

    public static ThreadLocalContainerResolver getDefault() {
        return DEFAULT;
    }
}
