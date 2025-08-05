package com.sun.xml.internal.ws.api.server;

import java.util.HashSet;
import java.util.Set;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/LazyMOMProvider.class */
public enum LazyMOMProvider {
    INSTANCE;

    private final Set<WSEndpointScopeChangeListener> endpointsWaitingForMOM = new HashSet();
    private final Set<DefaultScopeChangeListener> listeners = new HashSet();
    private volatile Scope scope = Scope.STANDALONE;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/LazyMOMProvider$DefaultScopeChangeListener.class */
    public interface DefaultScopeChangeListener extends ScopeChangeListener {
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/LazyMOMProvider$Scope.class */
    public enum Scope {
        STANDALONE,
        GLASSFISH_NO_JMX,
        GLASSFISH_JMX
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/LazyMOMProvider$ScopeChangeListener.class */
    public interface ScopeChangeListener {
        void scopeChanged(Scope scope);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/LazyMOMProvider$WSEndpointScopeChangeListener.class */
    public interface WSEndpointScopeChangeListener extends ScopeChangeListener {
    }

    LazyMOMProvider() {
    }

    public void initMOMForScope(Scope scope) {
        if (this.scope != Scope.GLASSFISH_JMX) {
            if ((scope == Scope.STANDALONE && (this.scope == Scope.GLASSFISH_JMX || this.scope == Scope.GLASSFISH_NO_JMX)) || this.scope == scope) {
                return;
            }
            this.scope = scope;
            fireScopeChanged();
        }
    }

    private void fireScopeChanged() {
        for (ScopeChangeListener wsEndpoint : this.endpointsWaitingForMOM) {
            wsEndpoint.scopeChanged(this.scope);
        }
        for (ScopeChangeListener listener : this.listeners) {
            listener.scopeChanged(this.scope);
        }
    }

    public void registerListener(DefaultScopeChangeListener listener) {
        this.listeners.add(listener);
        if (!isProviderInDefaultScope()) {
            listener.scopeChanged(this.scope);
        }
    }

    private boolean isProviderInDefaultScope() {
        return this.scope == Scope.STANDALONE;
    }

    public Scope getScope() {
        return this.scope;
    }

    public void registerEndpoint(WSEndpointScopeChangeListener wsEndpoint) {
        this.endpointsWaitingForMOM.add(wsEndpoint);
        if (!isProviderInDefaultScope()) {
            wsEndpoint.scopeChanged(this.scope);
        }
    }

    public void unregisterEndpoint(WSEndpointScopeChangeListener wsEndpoint) {
        this.endpointsWaitingForMOM.remove(wsEndpoint);
    }
}
