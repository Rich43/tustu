package com.sun.jndi.ldap.pool;

import com.sun.jndi.ldap.LdapPoolManager;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import javax.naming.CommunicationException;
import javax.naming.InterruptedNamingException;
import javax.naming.NamingException;

/* loaded from: rt.jar:com/sun/jndi/ldap/pool/Connections.class */
final class Connections implements PoolCallback {
    private static final boolean debug = Pool.debug;
    private static final boolean trace = LdapPoolManager.trace;
    private static final int DEFAULT_SIZE = 10;
    private final int maxSize;
    private final int prefSize;
    private final List<ConnectionDesc> conns;
    private boolean closed = false;
    private Reference<Object> ref;

    Connections(Object obj, int i2, int i3, int i4, PooledConnectionFactory pooledConnectionFactory) throws NamingException {
        this.maxSize = i4;
        if (i4 > 0) {
            this.prefSize = Math.min(i3, i4);
            i2 = Math.min(i2, i4);
        } else {
            this.prefSize = i3;
        }
        this.conns = new ArrayList(i4 > 0 ? i4 : 10);
        this.ref = new SoftReference(obj);
        d("init size=", i2);
        d("max size=", i4);
        d("preferred size=", i3);
        for (int i5 = 0; i5 < i2; i5++) {
            PooledConnection pooledConnectionCreatePooledConnection = pooledConnectionFactory.createPooledConnection(this);
            td("Create ", pooledConnectionCreatePooledConnection, pooledConnectionFactory);
            this.conns.add(new ConnectionDesc(pooledConnectionCreatePooledConnection));
        }
    }

    synchronized PooledConnection get(long j2, PooledConnectionFactory pooledConnectionFactory) throws NamingException {
        long jCurrentTimeMillis = j2 > 0 ? System.currentTimeMillis() : 0L;
        long jCurrentTimeMillis2 = j2;
        d("get(): before");
        while (true) {
            PooledConnection orCreateConnection = getOrCreateConnection(pooledConnectionFactory);
            if (orCreateConnection != null) {
                d("get(): after");
                return orCreateConnection;
            }
            if (j2 > 0 && jCurrentTimeMillis2 <= 0) {
                throw new CommunicationException("Timeout exceeded while waiting for a connection: " + j2 + "ms");
            }
            try {
                d("get(): waiting");
                if (jCurrentTimeMillis2 > 0) {
                    wait(jCurrentTimeMillis2);
                } else {
                    wait();
                }
                if (j2 > 0) {
                    jCurrentTimeMillis2 = j2 - (System.currentTimeMillis() - jCurrentTimeMillis);
                }
            } catch (InterruptedException e2) {
                throw new InterruptedNamingException("Interrupted while waiting for a connection");
            }
        }
    }

    private PooledConnection getOrCreateConnection(PooledConnectionFactory pooledConnectionFactory) throws NamingException {
        int size = this.conns.size();
        if (this.prefSize <= 0 || size >= this.prefSize) {
            for (int i2 = 0; i2 < size; i2++) {
                PooledConnection pooledConnectionTryUse = this.conns.get(i2).tryUse();
                if (pooledConnectionTryUse != null) {
                    d("get(): use ", pooledConnectionTryUse);
                    td("Use ", pooledConnectionTryUse);
                    return pooledConnectionTryUse;
                }
            }
        }
        if (this.maxSize > 0 && size >= this.maxSize) {
            return null;
        }
        PooledConnection pooledConnectionCreatePooledConnection = pooledConnectionFactory.createPooledConnection(this);
        td("Create and use ", pooledConnectionCreatePooledConnection, pooledConnectionFactory);
        this.conns.add(new ConnectionDesc(pooledConnectionCreatePooledConnection, true));
        return pooledConnectionCreatePooledConnection;
    }

    @Override // com.sun.jndi.ldap.pool.PoolCallback
    public synchronized boolean releasePooledConnection(PooledConnection pooledConnection) {
        List<ConnectionDesc> list = this.conns;
        ConnectionDesc connectionDesc = new ConnectionDesc(pooledConnection);
        int iIndexOf = list.indexOf(connectionDesc);
        d("release(): ", pooledConnection);
        if (iIndexOf >= 0) {
            if (this.closed || (this.prefSize > 0 && this.conns.size() > this.prefSize)) {
                d("release(): closing ", pooledConnection);
                td("Close ", pooledConnection);
                this.conns.remove(connectionDesc);
                pooledConnection.closeConnection();
            } else {
                d("release(): release ", pooledConnection);
                td("Release ", pooledConnection);
                this.conns.get(iIndexOf).release();
            }
            notifyAll();
            d("release(): notify");
            return true;
        }
        return false;
    }

    @Override // com.sun.jndi.ldap.pool.PoolCallback
    public synchronized boolean removePooledConnection(PooledConnection pooledConnection) {
        if (this.conns.remove(new ConnectionDesc(pooledConnection))) {
            d("remove(): ", pooledConnection);
            notifyAll();
            d("remove(): notify");
            td("Remove ", pooledConnection);
            if (this.conns.isEmpty()) {
                this.ref = null;
                return true;
            }
            return true;
        }
        d("remove(): not found ", pooledConnection);
        return false;
    }

    boolean expire(long j2) {
        ArrayList<ConnectionDesc> arrayList;
        boolean zIsEmpty;
        synchronized (this) {
            arrayList = new ArrayList(this.conns);
        }
        ArrayList arrayList2 = new ArrayList();
        for (ConnectionDesc connectionDesc : arrayList) {
            d("expire(): ", connectionDesc);
            if (connectionDesc.expire(j2)) {
                arrayList2.add(connectionDesc);
                td("expire(): Expired ", connectionDesc);
            }
        }
        synchronized (this) {
            this.conns.removeAll(arrayList2);
            zIsEmpty = this.conns.isEmpty();
        }
        return zIsEmpty;
    }

    synchronized void close() {
        expire(System.currentTimeMillis());
        this.closed = true;
    }

    String getStats() {
        int size;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        long useCount = 0;
        synchronized (this) {
            size = this.conns.size();
            for (int i5 = 0; i5 < size; i5++) {
                ConnectionDesc connectionDesc = this.conns.get(i5);
                useCount += connectionDesc.getUseCount();
                switch (connectionDesc.getState()) {
                    case 0:
                        i3++;
                        break;
                    case 1:
                        i2++;
                        break;
                    case 2:
                        i4++;
                        break;
                }
            }
        }
        return "size=" + size + "; use=" + useCount + "; busy=" + i3 + "; idle=" + i2 + "; expired=" + i4;
    }

    private void d(String str, Object obj) {
        if (debug) {
            d(str + obj);
        }
    }

    private void d(String str, int i2) {
        if (debug) {
            d(str + i2);
        }
    }

    private void d(String str) {
        if (debug) {
            System.err.println(((Object) this) + "." + str + "; size: " + this.conns.size());
        }
    }

    private void td(String str, Object obj, Object obj2) {
        if (trace) {
            td(str + obj + "[" + obj2 + "]");
        }
    }

    private void td(String str, Object obj) {
        if (trace) {
            td(str + obj);
        }
    }

    private void td(String str) {
        if (trace) {
            System.err.println(str);
        }
    }
}
