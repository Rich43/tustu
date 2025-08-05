package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSValue;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSValueImpl.class */
public class CSSValueImpl implements CSSValue {
    private final long peer;
    public static final int CSS_INHERIT = 0;
    public static final int CSS_PRIMITIVE_VALUE = 1;
    public static final int CSS_VALUE_LIST = 2;
    public static final int CSS_CUSTOM = 3;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native String getCssTextImpl(long j2);

    static native void setCssTextImpl(long j2, String str);

    static native short getCssValueTypeImpl(long j2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSValueImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            CSSValueImpl.dispose(this.peer);
        }
    }

    CSSValueImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static CSSValue create(long peer) {
        if (peer == 0) {
            return null;
        }
        switch (getCssValueTypeImpl(peer)) {
            case 1:
                return new CSSPrimitiveValueImpl(peer);
            case 2:
                return new CSSValueListImpl(peer);
            default:
                return new CSSValueImpl(peer);
        }
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof CSSValueImpl) && this.peer == ((CSSValueImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(CSSValue arg) {
        if (arg == null) {
            return 0L;
        }
        return ((CSSValueImpl) arg).getPeer();
    }

    static CSSValue getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.css.CSSValue
    public String getCssText() {
        return getCssTextImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSValue
    public void setCssText(String value) throws DOMException {
        setCssTextImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.css.CSSValue
    public short getCssValueType() {
        return getCssValueTypeImpl(getPeer());
    }
}
