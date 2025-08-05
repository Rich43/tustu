package sun.net.www.http;

/* compiled from: KeepAliveCache.java */
/* loaded from: rt.jar:sun/net/www/http/KeepAliveEntry.class */
class KeepAliveEntry {
    HttpClient hc;
    long idleStartTime;

    KeepAliveEntry(HttpClient httpClient, long j2) {
        this.hc = httpClient;
        this.idleStartTime = j2;
    }
}
