package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.stylesheets.MediaList;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/MediaListImpl.class */
public class MediaListImpl implements MediaList {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native String getMediaTextImpl(long j2);

    static native void setMediaTextImpl(long j2, String str);

    static native int getLengthImpl(long j2);

    static native String itemImpl(long j2, int i2);

    static native void deleteMediumImpl(long j2, String str);

    static native void appendMediumImpl(long j2, String str);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/MediaListImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            MediaListImpl.dispose(this.peer);
        }
    }

    MediaListImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static MediaList create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new MediaListImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof MediaListImpl) && this.peer == ((MediaListImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(MediaList arg) {
        if (arg == null) {
            return 0L;
        }
        return ((MediaListImpl) arg).getPeer();
    }

    static MediaList getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.stylesheets.MediaList
    public String getMediaText() {
        return getMediaTextImpl(getPeer());
    }

    @Override // org.w3c.dom.stylesheets.MediaList
    public void setMediaText(String value) throws DOMException {
        setMediaTextImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.stylesheets.MediaList
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    @Override // org.w3c.dom.stylesheets.MediaList
    public String item(int index) {
        return itemImpl(getPeer(), index);
    }

    @Override // org.w3c.dom.stylesheets.MediaList
    public void deleteMedium(String oldMedium) throws DOMException {
        deleteMediumImpl(getPeer(), oldMedium);
    }

    @Override // org.w3c.dom.stylesheets.MediaList
    public void appendMedium(String newMedium) throws DOMException {
        appendMediumImpl(getPeer(), newMedium);
    }
}
