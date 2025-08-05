package com.sun.jndi.ldap.pool;

/* loaded from: rt.jar:com/sun/jndi/ldap/pool/ConnectionDesc.class */
final class ConnectionDesc {
    private static final boolean debug = Pool.debug;
    static final byte BUSY = 0;
    static final byte IDLE = 1;
    static final byte EXPIRED = 2;
    private final PooledConnection conn;
    private byte state;
    private long idleSince;
    private long useCount;

    ConnectionDesc(PooledConnection pooledConnection) {
        this.state = (byte) 1;
        this.useCount = 0L;
        this.conn = pooledConnection;
    }

    ConnectionDesc(PooledConnection pooledConnection, boolean z2) {
        this.state = (byte) 1;
        this.useCount = 0L;
        this.conn = pooledConnection;
        if (z2) {
            this.state = (byte) 0;
            this.useCount++;
        }
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof ConnectionDesc) && ((ConnectionDesc) obj).conn == this.conn;
    }

    public int hashCode() {
        return this.conn.hashCode();
    }

    synchronized boolean release() {
        d("release()");
        if (this.state == 0) {
            this.state = (byte) 1;
            this.idleSince = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    synchronized PooledConnection tryUse() {
        d("tryUse()");
        if (this.state == 1) {
            this.state = (byte) 0;
            this.useCount++;
            return this.conn;
        }
        return null;
    }

    synchronized boolean expire(long j2) {
        if (this.state == 1 && this.idleSince < j2) {
            d("expire(): expired");
            this.state = (byte) 2;
            this.conn.closeConnection();
            return true;
        }
        d("expire(): not expired");
        return false;
    }

    public String toString() {
        return this.conn.toString() + " " + (this.state == 0 ? "busy" : this.state == 1 ? "idle" : "expired");
    }

    int getState() {
        return this.state;
    }

    long getUseCount() {
        return this.useCount;
    }

    private void d(String str) {
        if (debug) {
            System.err.println("ConnectionDesc." + str + " " + toString());
        }
    }
}
