package java.awt;

/* compiled from: MediaTracker.java */
/* loaded from: rt.jar:java/awt/MediaEntry.class */
abstract class MediaEntry {
    MediaTracker tracker;
    int ID;
    MediaEntry next;
    int status;
    boolean cancelled;
    static final int LOADING = 1;
    static final int ABORTED = 2;
    static final int ERRORED = 4;
    static final int COMPLETE = 8;
    static final int LOADSTARTED = 13;
    static final int DONE = 14;

    abstract Object getMedia();

    abstract void startLoad();

    MediaEntry(MediaTracker mediaTracker, int i2) {
        this.tracker = mediaTracker;
        this.ID = i2;
    }

    static MediaEntry insert(MediaEntry mediaEntry, MediaEntry mediaEntry2) {
        MediaEntry mediaEntry3 = mediaEntry;
        MediaEntry mediaEntry4 = null;
        while (mediaEntry3 != null && mediaEntry3.ID <= mediaEntry2.ID) {
            mediaEntry4 = mediaEntry3;
            mediaEntry3 = mediaEntry3.next;
        }
        mediaEntry2.next = mediaEntry3;
        if (mediaEntry4 == null) {
            mediaEntry = mediaEntry2;
        } else {
            mediaEntry4.next = mediaEntry2;
        }
        return mediaEntry;
    }

    int getID() {
        return this.ID;
    }

    void cancel() {
        this.cancelled = true;
    }

    synchronized int getStatus(boolean z2, boolean z3) {
        if (z2 && (this.status & 13) == 0) {
            this.status = (this.status & (-3)) | 1;
            startLoad();
        }
        return this.status;
    }

    void setStatus(int i2) {
        synchronized (this) {
            this.status = i2;
        }
        this.tracker.setDone();
    }
}
