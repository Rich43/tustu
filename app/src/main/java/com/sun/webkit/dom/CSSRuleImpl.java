package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSRuleImpl.class */
public class CSSRuleImpl implements CSSRule {
    private final long peer;
    public static final int UNKNOWN_RULE = 0;
    public static final int STYLE_RULE = 1;
    public static final int CHARSET_RULE = 2;
    public static final int IMPORT_RULE = 3;
    public static final int MEDIA_RULE = 4;
    public static final int FONT_FACE_RULE = 5;
    public static final int PAGE_RULE = 6;
    public static final int KEYFRAMES_RULE = 7;
    public static final int KEYFRAME_RULE = 8;
    public static final int SUPPORTS_RULE = 12;
    public static final int WEBKIT_REGION_RULE = 16;
    public static final int WEBKIT_KEYFRAMES_RULE = 7;
    public static final int WEBKIT_KEYFRAME_RULE = 8;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native short getTypeImpl(long j2);

    static native String getCssTextImpl(long j2);

    static native void setCssTextImpl(long j2, String str);

    static native long getParentStyleSheetImpl(long j2);

    static native long getParentRuleImpl(long j2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSRuleImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            CSSRuleImpl.dispose(this.peer);
        }
    }

    CSSRuleImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static CSSRule create(long peer) {
        if (peer == 0) {
            return null;
        }
        switch (getTypeImpl(peer)) {
            case 1:
                return new CSSStyleRuleImpl(peer);
            case 2:
                return new CSSCharsetRuleImpl(peer);
            case 3:
                return new CSSImportRuleImpl(peer);
            case 4:
                return new CSSMediaRuleImpl(peer);
            case 5:
                return new CSSFontFaceRuleImpl(peer);
            case 6:
                return new CSSPageRuleImpl(peer);
            default:
                return new CSSRuleImpl(peer);
        }
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof CSSRuleImpl) && this.peer == ((CSSRuleImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(CSSRule arg) {
        if (arg == null) {
            return 0L;
        }
        return ((CSSRuleImpl) arg).getPeer();
    }

    static CSSRule getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.css.CSSRule
    public short getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSRule
    public String getCssText() {
        return getCssTextImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSRule
    public void setCssText(String value) throws DOMException {
        setCssTextImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.css.CSSRule
    public CSSStyleSheet getParentStyleSheet() {
        return CSSStyleSheetImpl.getImpl(getParentStyleSheetImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.CSSRule
    public CSSRule getParentRule() {
        return getImpl(getParentRuleImpl(getPeer()));
    }
}
