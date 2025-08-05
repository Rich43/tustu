package sun.net.www.http;

/* loaded from: rt.jar:sun/net/www/http/KeepAliveCleanerEntry.class */
class KeepAliveCleanerEntry {
    KeepAliveStream kas;
    HttpClient hc;

    public KeepAliveCleanerEntry(KeepAliveStream keepAliveStream, HttpClient httpClient) {
        this.kas = keepAliveStream;
        this.hc = httpClient;
    }

    protected KeepAliveStream getKeepAliveStream() {
        return this.kas;
    }

    protected HttpClient getHttpClient() {
        return this.hc;
    }

    protected void setQueuedForCleanup() {
        this.kas.queuedForCleanup = true;
    }

    protected boolean getQueuedForCleanup() {
        return this.kas.queuedForCleanup;
    }
}
