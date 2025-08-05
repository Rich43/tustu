package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.Component;
import com.sun.xml.internal.ws.api.ComponentEx;
import com.sun.xml.internal.ws.api.ComponentRegistry;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/Container.class */
public abstract class Container implements ComponentRegistry, ComponentEx {
    private final Set<Component> components = new CopyOnWriteArraySet();
    public static final Container NONE = new NoneContainer();

    protected Container() {
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/Container$NoneContainer.class */
    private static final class NoneContainer extends Container {
        private NoneContainer() {
        }
    }

    @Override // com.sun.xml.internal.ws.api.Component
    public <S> S getSPI(Class<S> cls) {
        if (this.components == null) {
            return null;
        }
        Iterator<Component> it = this.components.iterator();
        while (it.hasNext()) {
            S s2 = (S) it.next().getSPI(cls);
            if (s2 != null) {
                return s2;
            }
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.ComponentRegistry
    public Set<Component> getComponents() {
        return this.components;
    }

    @Override // com.sun.xml.internal.ws.api.ComponentEx
    @NotNull
    public <E> Iterable<E> getIterableSPI(Class<E> spiType) {
        Object spi = getSPI(spiType);
        if (spi != null) {
            Collection<E> c2 = Collections.singletonList(spi);
            return c2;
        }
        return Collections.emptySet();
    }
}
