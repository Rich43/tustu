package com.sun.jndi.ldap;

import com.sun.jndi.ldap.pool.PoolCallback;
import com.sun.jndi.ldap.pool.PooledConnection;
import com.sun.jndi.ldap.pool.PooledConnectionFactory;
import java.io.OutputStream;
import javax.naming.NamingException;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapClientFactory.class */
final class LdapClientFactory implements PooledConnectionFactory {
    private final String host;
    private final int port;
    private final String socketFactory;
    private final int connTimeout;
    private final int readTimeout;
    private final OutputStream trace;

    LdapClientFactory(String str, int i2, String str2, int i3, int i4, OutputStream outputStream) {
        this.host = str;
        this.port = i2;
        this.socketFactory = str2;
        this.connTimeout = i3;
        this.readTimeout = i4;
        this.trace = outputStream;
    }

    @Override // com.sun.jndi.ldap.pool.PooledConnectionFactory
    public PooledConnection createPooledConnection(PoolCallback poolCallback) throws NamingException {
        return new LdapClient(this.host, this.port, this.socketFactory, this.connTimeout, this.readTimeout, this.trace, poolCallback);
    }

    public String toString() {
        return this.host + CallSiteDescriptor.TOKEN_DELIMITER + this.port;
    }
}
