package com.sun.webkit.dom;

import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSStyleDeclaration;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSFontFaceRuleImpl.class */
public class CSSFontFaceRuleImpl extends CSSRuleImpl implements CSSFontFaceRule {
    static native long getStyleImpl(long j2);

    CSSFontFaceRuleImpl(long peer) {
        super(peer);
    }

    static CSSFontFaceRule getImpl(long peer) {
        return (CSSFontFaceRule) create(peer);
    }

    @Override // org.w3c.dom.css.CSSFontFaceRule
    public CSSStyleDeclaration getStyle() {
        return CSSStyleDeclarationImpl.getImpl(getStyleImpl(getPeer()));
    }
}
