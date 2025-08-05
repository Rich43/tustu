package com.sun.jndi.ldap.pool;

import com.sun.jndi.ldap.LdapPoolManager;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import javax.naming.NamingException;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/jndi/ldap/pool/Pool.class */
public final class Pool {
    static final boolean debug = LdapPoolManager.debug;
    private static final ReferenceQueue<ConnectionsRef> queue = new ReferenceQueue<>();
    private static final Collection<Reference<ConnectionsRef>> weakRefs = Collections.synchronizedList(new LinkedList());
    private final int maxSize;
    private final int prefSize;
    private final int initSize;
    private final Map<Object, ConnectionsRef> map = new WeakHashMap();

    public Pool(int i2, int i3, int i4) {
        this.prefSize = i3;
        this.maxSize = i4;
        this.initSize = i2;
    }

    public PooledConnection getPooledConnection(Object obj, long j2, PooledConnectionFactory pooledConnectionFactory) throws NamingException {
        Connections connections;
        d("get(): ", obj);
        if (debug) {
            synchronized (this.map) {
                d("size: ", this.map.size());
            }
        }
        expungeStaleConnections();
        synchronized (this.map) {
            connections = getConnections(obj);
            if (connections == null) {
                d("get(): creating new connections list for ", obj);
                connections = new Connections(obj, this.initSize, this.prefSize, this.maxSize, pooledConnectionFactory);
                ConnectionsRef connectionsRef = new ConnectionsRef(connections);
                this.map.put(obj, connectionsRef);
                weakRefs.add(new ConnectionsWeakRef(connectionsRef, queue));
            }
            d("get(): size after: ", this.map.size());
        }
        return connections.get(j2, pooledConnectionFactory);
    }

    private Connections getConnections(Object obj) {
        ConnectionsRef connectionsRef = this.map.get(obj);
        if (connectionsRef != null) {
            return connectionsRef.getConnections();
        }
        return null;
    }

    public void expire(long j2) {
        ArrayList<ConnectionsRef> arrayList;
        synchronized (this.map) {
            arrayList = new ArrayList(this.map.values());
        }
        ArrayList arrayList2 = new ArrayList();
        for (ConnectionsRef connectionsRef : arrayList) {
            Connections connections = connectionsRef.getConnections();
            if (connections.expire(j2)) {
                d("expire(): removing ", connections);
                arrayList2.add(connectionsRef);
            }
        }
        synchronized (this.map) {
            this.map.values().removeAll(arrayList2);
        }
        expungeStaleConnections();
    }

    private static void expungeStaleConnections() {
        while (true) {
            ConnectionsWeakRef connectionsWeakRef = (ConnectionsWeakRef) queue.poll();
            if (connectionsWeakRef != null) {
                Connections connections = connectionsWeakRef.getConnections();
                if (debug) {
                    System.err.println("weak reference cleanup: Closing Connections:" + ((Object) connections));
                }
                connections.close();
                weakRefs.remove(connectionsWeakRef);
                connectionsWeakRef.clear();
            } else {
                return;
            }
        }
    }

    public void showStats(PrintStream printStream) {
        printStream.println("===== Pool start ======================");
        printStream.println("maximum pool size: " + this.maxSize);
        printStream.println("preferred pool size: " + this.prefSize);
        printStream.println("initial pool size: " + this.initSize);
        synchronized (this.map) {
            printStream.println("current pool size: " + this.map.size());
            for (Map.Entry<Object, ConnectionsRef> entry : this.map.entrySet()) {
                printStream.println("   " + entry.getKey() + CallSiteDescriptor.TOKEN_DELIMITER + entry.getValue().getConnections().getStats());
            }
        }
        printStream.println("====== Pool end =====================");
    }

    public String toString() {
        String str;
        synchronized (this.map) {
            str = super.toString() + " " + this.map.toString();
        }
        return str;
    }

    private void d(String str, int i2) {
        if (debug) {
            System.err.println(((Object) this) + "." + str + i2);
        }
    }

    private void d(String str, Object obj) {
        if (debug) {
            System.err.println(((Object) this) + "." + str + obj);
        }
    }
}
