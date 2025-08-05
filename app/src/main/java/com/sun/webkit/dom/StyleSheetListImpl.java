package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.stylesheets.StyleSheetList;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/StyleSheetListImpl.class */
public class StyleSheetListImpl implements StyleSheetList {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native int getLengthImpl(long j2);

    static native long itemImpl(long j2, int i2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/StyleSheetListImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            StyleSheetListImpl.dispose(this.peer);
        }
    }

    StyleSheetListImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static StyleSheetList create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new StyleSheetListImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof StyleSheetListImpl) && this.peer == ((StyleSheetListImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(StyleSheetList arg) {
        if (arg == null) {
            return 0L;
        }
        return ((StyleSheetListImpl) arg).getPeer();
    }

    static StyleSheetList getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.stylesheets.StyleSheetList
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    @Override // org.w3c.dom.stylesheets.StyleSheetList
    public StyleSheet item(int index) {
        return StyleSheetImpl.getImpl(itemImpl(getPeer(), index));
    }
}
