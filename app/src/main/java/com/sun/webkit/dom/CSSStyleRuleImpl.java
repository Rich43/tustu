package com.sun.webkit.dom;

import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSStyleRuleImpl.class */
public class CSSStyleRuleImpl extends CSSRuleImpl implements CSSStyleRule {
    static native String getSelectorTextImpl(long j2);

    static native void setSelectorTextImpl(long j2, String str);

    static native long getStyleImpl(long j2);

    CSSStyleRuleImpl(long peer) {
        super(peer);
    }

    static CSSStyleRule getImpl(long peer) {
        return (CSSStyleRule) create(peer);
    }

    @Override // org.w3c.dom.css.CSSStyleRule
    public String getSelectorText() {
        return getSelectorTextImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSStyleRule
    public void setSelectorText(String value) {
        setSelectorTextImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.css.CSSStyleRule
    public CSSStyleDeclaration getStyle() {
        return CSSStyleDeclarationImpl.getImpl(getStyleImpl(getPeer()));
    }
}
