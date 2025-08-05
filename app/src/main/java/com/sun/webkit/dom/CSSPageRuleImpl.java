package com.sun.webkit.dom;

import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSStyleDeclaration;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSPageRuleImpl.class */
public class CSSPageRuleImpl extends CSSRuleImpl implements CSSPageRule {
    static native String getSelectorTextImpl(long j2);

    static native void setSelectorTextImpl(long j2, String str);

    static native long getStyleImpl(long j2);

    CSSPageRuleImpl(long peer) {
        super(peer);
    }

    static CSSPageRule getImpl(long peer) {
        return (CSSPageRule) create(peer);
    }

    @Override // org.w3c.dom.css.CSSPageRule
    public String getSelectorText() {
        return getSelectorTextImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSPageRule
    public void setSelectorText(String value) {
        setSelectorTextImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.css.CSSPageRule
    public CSSStyleDeclaration getStyle() {
        return CSSStyleDeclarationImpl.getImpl(getStyleImpl(getPeer()));
    }
}
