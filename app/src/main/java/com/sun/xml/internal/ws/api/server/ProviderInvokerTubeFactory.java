package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.server.provider.AsyncProviderInvokerTube;
import com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder;
import com.sun.xml.internal.ws.server.provider.ProviderInvokerTube;
import com.sun.xml.internal.ws.server.provider.SyncProviderInvokerTube;
import com.sun.xml.internal.ws.util.ServiceFinder;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/ProviderInvokerTubeFactory.class */
public abstract class ProviderInvokerTubeFactory<T> {
    private static final ProviderInvokerTubeFactory DEFAULT = new DefaultProviderInvokerTubeFactory();
    private static final Logger logger = Logger.getLogger(ProviderInvokerTubeFactory.class.getName());

    protected abstract ProviderInvokerTube<T> doCreate(@NotNull Class<T> cls, @NotNull Invoker invoker, @NotNull ProviderArgumentsBuilder<?> providerArgumentsBuilder, boolean z2);

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/ProviderInvokerTubeFactory$DefaultProviderInvokerTubeFactory.class */
    private static class DefaultProviderInvokerTubeFactory<T> extends ProviderInvokerTubeFactory<T> {
        private DefaultProviderInvokerTubeFactory() {
        }

        @Override // com.sun.xml.internal.ws.api.server.ProviderInvokerTubeFactory
        public ProviderInvokerTube<T> doCreate(@NotNull Class<T> implType, @NotNull Invoker invoker, @NotNull ProviderArgumentsBuilder<?> argsBuilder, boolean isAsync) {
            return createDefault(implType, invoker, argsBuilder, isAsync);
        }
    }

    public static <T> ProviderInvokerTube<T> create(@Nullable ClassLoader classLoader, @NotNull Container container, @NotNull Class<T> cls, @NotNull Invoker invoker, @NotNull ProviderArgumentsBuilder<?> providerArgumentsBuilder, boolean z2) {
        Iterator<T> it = ServiceFinder.find(ProviderInvokerTubeFactory.class, classLoader, container).iterator();
        while (it.hasNext()) {
            ProviderInvokerTubeFactory providerInvokerTubeFactory = (ProviderInvokerTubeFactory) it.next();
            ProviderInvokerTube<T> providerInvokerTubeDoCreate = providerInvokerTubeFactory.doCreate(cls, invoker, providerArgumentsBuilder, z2);
            if (providerInvokerTubeDoCreate != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "{0} successfully created {1}", new Object[]{providerInvokerTubeFactory.getClass(), providerInvokerTubeDoCreate});
                }
                return providerInvokerTubeDoCreate;
            }
        }
        return DEFAULT.createDefault(cls, invoker, providerArgumentsBuilder, z2);
    }

    protected ProviderInvokerTube<T> createDefault(@NotNull Class<T> implType, @NotNull Invoker invoker, @NotNull ProviderArgumentsBuilder<?> argsBuilder, boolean isAsync) {
        return isAsync ? new AsyncProviderInvokerTube(invoker, argsBuilder) : new SyncProviderInvokerTube(invoker, argsBuilder);
    }
}
