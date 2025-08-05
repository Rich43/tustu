package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.Node;
import org.w3c.dom.stylesheets.MediaList;
import org.w3c.dom.stylesheets.StyleSheet;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/StyleSheetImpl.class */
public class StyleSheetImpl implements StyleSheet {
    private final long peer;
    private static final int TYPE_CSSStyleSheet = 1;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    private static native int getCPPTypeImpl(long j2);

    static native String getTypeImpl(long j2);

    static native boolean getDisabledImpl(long j2);

    static native void setDisabledImpl(long j2, boolean z2);

    static native long getOwnerNodeImpl(long j2);

    static native long getParentStyleSheetImpl(long j2);

    static native String getHrefImpl(long j2);

    static native String getTitleImpl(long j2);

    static native long getMediaImpl(long j2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/StyleSheetImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            StyleSheetImpl.dispose(this.peer);
        }
    }

    StyleSheetImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static StyleSheet create(long peer) {
        if (peer == 0) {
            return null;
        }
        switch (getCPPTypeImpl(peer)) {
            case 1:
                return new CSSStyleSheetImpl(peer);
            default:
                return new StyleSheetImpl(peer);
        }
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof StyleSheetImpl) && this.peer == ((StyleSheetImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(StyleSheet arg) {
        if (arg == null) {
            return 0L;
        }
        return ((StyleSheetImpl) arg).getPeer();
    }

    static StyleSheet getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.stylesheets.StyleSheet
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.stylesheets.StyleSheet
    public boolean getDisabled() {
        return getDisabledImpl(getPeer());
    }

    @Override // org.w3c.dom.stylesheets.StyleSheet
    public void setDisabled(boolean value) {
        setDisabledImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.stylesheets.StyleSheet
    public Node getOwnerNode() {
        return NodeImpl.getImpl(getOwnerNodeImpl(getPeer()));
    }

    @Override // org.w3c.dom.stylesheets.StyleSheet
    public StyleSheet getParentStyleSheet() {
        return getImpl(getParentStyleSheetImpl(getPeer()));
    }

    @Override // org.w3c.dom.stylesheets.StyleSheet
    public String getHref() {
        return getHrefImpl(getPeer());
    }

    @Override // org.w3c.dom.stylesheets.StyleSheet
    public String getTitle() {
        return getTitleImpl(getPeer());
    }

    @Override // org.w3c.dom.stylesheets.StyleSheet
    public MediaList getMedia() {
        return MediaListImpl.getImpl(getMediaImpl(getPeer()));
    }
}
