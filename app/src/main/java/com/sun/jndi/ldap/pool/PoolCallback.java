package com.sun.jndi.ldap.pool;

/* loaded from: rt.jar:com/sun/jndi/ldap/pool/PoolCallback.class */
public interface PoolCallback {
    boolean releasePooledConnection(PooledConnection pooledConnection);

    boolean removePooledConnection(PooledConnection pooledConnection);
}
