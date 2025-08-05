package sun.net;

import java.net.URL;

/* loaded from: rt.jar:sun/net/ProgressSource.class */
public class ProgressSource {
    private URL url;
    private String method;
    private String contentType;
    private long progress;
    private long lastProgress;
    private long expected;
    private State state;
    private boolean connected;
    private int threshold;
    private ProgressMonitor progressMonitor;

    /* loaded from: rt.jar:sun/net/ProgressSource$State.class */
    public enum State {
        NEW,
        CONNECTED,
        UPDATE,
        DELETE
    }

    public ProgressSource(URL url, String str) {
        this(url, str, -1L);
    }

    public ProgressSource(URL url, String str, long j2) {
        this.progress = 0L;
        this.lastProgress = 0L;
        this.expected = -1L;
        this.connected = false;
        this.threshold = 8192;
        this.url = url;
        this.method = str;
        this.contentType = "content/unknown";
        this.progress = 0L;
        this.lastProgress = 0L;
        this.expected = j2;
        this.state = State.NEW;
        this.progressMonitor = ProgressMonitor.getDefault();
        this.threshold = this.progressMonitor.getProgressUpdateThreshold();
    }

    public boolean connected() {
        if (!this.connected) {
            this.connected = true;
            this.state = State.CONNECTED;
            return false;
        }
        return true;
    }

    public void close() {
        this.state = State.DELETE;
    }

    public URL getURL() {
        return this.url;
    }

    public String getMethod() {
        return this.method;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String str) {
        this.contentType = str;
    }

    public long getProgress() {
        return this.progress;
    }

    public long getExpected() {
        return this.expected;
    }

    public State getState() {
        return this.state;
    }

    public void beginTracking() {
        this.progressMonitor.registerSource(this);
    }

    public void finishTracking() {
        this.progressMonitor.unregisterSource(this);
    }

    public void updateProgress(long j2, long j3) {
        this.lastProgress = this.progress;
        this.progress = j2;
        this.expected = j3;
        if (!connected()) {
            this.state = State.CONNECTED;
        } else {
            this.state = State.UPDATE;
        }
        if (this.lastProgress / this.threshold != this.progress / this.threshold) {
            this.progressMonitor.updateProgress(this);
        }
        if (this.expected != -1 && this.progress >= this.expected && this.progress != 0) {
            close();
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return getClass().getName() + "[url=" + ((Object) this.url) + ", method=" + this.method + ", state=" + ((Object) this.state) + ", content-type=" + this.contentType + ", progress=" + this.progress + ", expected=" + this.expected + "]";
    }
}
