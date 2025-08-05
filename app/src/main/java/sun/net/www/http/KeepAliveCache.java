package sun.net.www.http;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import sun.java2d.marlin.MarlinConst;
import sun.security.action.GetIntegerAction;

/* loaded from: rt.jar:sun/net/www/http/KeepAliveCache.class */
public class KeepAliveCache extends HashMap<KeepAliveKey, ClientVector> implements Runnable {
    private static final long serialVersionUID = -2937172892064557949L;
    static final int MAX_CONNECTIONS = 5;
    static int result = -1;
    static final int LIFETIME = 5000;
    private Thread keepAliveTimer = null;

    static int getMaxConnections() {
        if (result == -1) {
            result = ((Integer) AccessController.doPrivileged(new GetIntegerAction("http.maxConnections", 5))).intValue();
            if (result <= 0) {
                result = 5;
            }
        }
        return result;
    }

    public synchronized void put(URL url, Object obj, HttpClient httpClient) {
        boolean z2 = this.keepAliveTimer == null;
        if (!z2 && !this.keepAliveTimer.isAlive()) {
            z2 = true;
        }
        if (z2) {
            clear();
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.net.www.http.KeepAliveCache.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                    while (true) {
                        ThreadGroup parent = threadGroup.getParent();
                        if (parent != null) {
                            threadGroup = parent;
                        } else {
                            KeepAliveCache.this.keepAliveTimer = new Thread(threadGroup, this, "Keep-Alive-Timer");
                            KeepAliveCache.this.keepAliveTimer.setDaemon(true);
                            KeepAliveCache.this.keepAliveTimer.setPriority(8);
                            KeepAliveCache.this.keepAliveTimer.setContextClassLoader(null);
                            KeepAliveCache.this.keepAliveTimer.start();
                            return null;
                        }
                    }
                }
            });
        }
        KeepAliveKey keepAliveKey = new KeepAliveKey(url, obj);
        ClientVector clientVector = (ClientVector) super.get(keepAliveKey);
        if (clientVector == null) {
            int keepAliveTimeout = httpClient.getKeepAliveTimeout();
            ClientVector clientVector2 = new ClientVector(keepAliveTimeout > 0 ? keepAliveTimeout * 1000 : 5000);
            clientVector2.put(httpClient);
            super.put(keepAliveKey, clientVector2);
            return;
        }
        clientVector.put(httpClient);
    }

    public synchronized void remove(HttpClient httpClient, Object obj) {
        KeepAliveKey keepAliveKey = new KeepAliveKey(httpClient.url, obj);
        ClientVector clientVector = (ClientVector) super.get(keepAliveKey);
        if (clientVector != null) {
            clientVector.remove(httpClient);
            if (clientVector.empty()) {
                removeVector(keepAliveKey);
            }
        }
    }

    synchronized void removeVector(KeepAliveKey keepAliveKey) {
        super.remove(keepAliveKey);
    }

    public synchronized HttpClient get(URL url, Object obj) {
        ClientVector clientVector = (ClientVector) super.get(new KeepAliveKey(url, obj));
        if (clientVector == null) {
            return null;
        }
        return clientVector.get();
    }

    @Override // java.lang.Runnable
    public void run() {
        do {
            try {
                Thread.sleep(MarlinConst.statDump);
            } catch (InterruptedException e2) {
            }
            synchronized (this) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                ArrayList arrayList = new ArrayList();
                for (KeepAliveKey keepAliveKey : keySet()) {
                    ClientVector clientVector = get(keepAliveKey);
                    synchronized (clientVector) {
                        int i2 = 0;
                        while (i2 < clientVector.size()) {
                            KeepAliveEntry keepAliveEntryElementAt = clientVector.elementAt(i2);
                            if (jCurrentTimeMillis - keepAliveEntryElementAt.idleStartTime <= clientVector.nap) {
                                break;
                            }
                            keepAliveEntryElementAt.hc.closeServer();
                            i2++;
                        }
                        clientVector.subList(0, i2).clear();
                        if (clientVector.size() == 0) {
                            arrayList.add(keepAliveKey);
                        }
                    }
                }
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    removeVector((KeepAliveKey) it.next());
                }
            }
        } while (size() > 0);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        throw new NotSerializableException();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        throw new NotSerializableException();
    }
}
