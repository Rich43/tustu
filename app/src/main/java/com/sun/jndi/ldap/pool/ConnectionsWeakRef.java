package com.sun.jndi.ldap.pool;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/* loaded from: rt.jar:com/sun/jndi/ldap/pool/ConnectionsWeakRef.class */
class ConnectionsWeakRef extends WeakReference<ConnectionsRef> {
    private final Connections conns;

    ConnectionsWeakRef(ConnectionsRef connectionsRef, ReferenceQueue<? super ConnectionsRef> referenceQueue) {
        super(connectionsRef, referenceQueue);
        this.conns = connectionsRef.getConnections();
    }

    Connections getConnections() {
        return this.conns;
    }
}
