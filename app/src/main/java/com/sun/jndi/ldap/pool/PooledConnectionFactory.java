package com.sun.jndi.ldap.pool;

import javax.naming.NamingException;

/* loaded from: rt.jar:com/sun/jndi/ldap/pool/PooledConnectionFactory.class */
public interface PooledConnectionFactory {
    PooledConnection createPooledConnection(PoolCallback poolCallback) throws NamingException;
}
