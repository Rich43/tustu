package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSRuleListImpl.class */
public class CSSRuleListImpl implements CSSRuleList {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native int getLengthImpl(long j2);

    static native long itemImpl(long j2, int i2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSRuleListImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            CSSRuleListImpl.dispose(this.peer);
        }
    }

    CSSRuleListImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static CSSRuleList create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new CSSRuleListImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof CSSRuleListImpl) && this.peer == ((CSSRuleListImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(CSSRuleList arg) {
        if (arg == null) {
            return 0L;
        }
        return ((CSSRuleListImpl) arg).getPeer();
    }

    static CSSRuleList getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.css.CSSRuleList
    public int getLength() {
        return getLengthImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSRuleList
    public CSSRule item(int index) {
        return CSSRuleImpl.getImpl(itemImpl(getPeer(), index));
    }
}
