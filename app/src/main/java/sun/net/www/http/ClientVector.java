package sun.net.www.http;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

/* compiled from: KeepAliveCache.java */
/* loaded from: rt.jar:sun/net/www/http/ClientVector.class */
class ClientVector extends Stack<KeepAliveEntry> {
    private static final long serialVersionUID = -8680532108106489459L;
    int nap;

    ClientVector(int i2) {
        this.nap = i2;
    }

    synchronized HttpClient get() {
        if (empty()) {
            return null;
        }
        HttpClient httpClient = null;
        long jCurrentTimeMillis = System.currentTimeMillis();
        do {
            KeepAliveEntry keepAliveEntryPop = pop();
            if (jCurrentTimeMillis - keepAliveEntryPop.idleStartTime > this.nap) {
                keepAliveEntryPop.hc.closeServer();
            } else {
                httpClient = keepAliveEntryPop.hc;
            }
            if (httpClient != null) {
                break;
            }
        } while (!empty());
        return httpClient;
    }

    synchronized void put(HttpClient httpClient) {
        if (size() >= KeepAliveCache.getMaxConnections()) {
            httpClient.closeServer();
        } else {
            push(new KeepAliveEntry(httpClient, System.currentTimeMillis()));
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        throw new NotSerializableException();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        throw new NotSerializableException();
    }
}
