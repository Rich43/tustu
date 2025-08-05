package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSCharsetRule;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSCharsetRuleImpl.class */
public class CSSCharsetRuleImpl extends CSSRuleImpl implements CSSCharsetRule {
    static native String getEncodingImpl(long j2);

    static native void setEncodingImpl(long j2, String str);

    CSSCharsetRuleImpl(long peer) {
        super(peer);
    }

    static CSSCharsetRule getImpl(long peer) {
        return (CSSCharsetRule) create(peer);
    }

    @Override // org.w3c.dom.css.CSSCharsetRule
    public String getEncoding() {
        return getEncodingImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSCharsetRule
    public void setEncoding(String value) throws DOMException {
        setEncodingImpl(getPeer(), value);
    }
}
