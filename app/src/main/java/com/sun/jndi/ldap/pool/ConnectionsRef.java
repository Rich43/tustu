package com.sun.jndi.ldap.pool;

/* loaded from: rt.jar:com/sun/jndi/ldap/pool/ConnectionsRef.class */
final class ConnectionsRef {
    private final Connections conns;

    ConnectionsRef(Connections connections) {
        this.conns = connections;
    }

    Connections getConnections() {
        return this.conns;
    }
}
