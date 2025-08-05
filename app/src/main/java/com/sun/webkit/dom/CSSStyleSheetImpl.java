package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSStyleSheetImpl.class */
public class CSSStyleSheetImpl extends StyleSheetImpl implements CSSStyleSheet {
    static native long getOwnerRuleImpl(long j2);

    static native long getCssRulesImpl(long j2);

    static native long getRulesImpl(long j2);

    static native int insertRuleImpl(long j2, String str, int i2);

    static native void deleteRuleImpl(long j2, int i2);

    static native int addRuleImpl(long j2, String str, String str2, int i2);

    static native void removeRuleImpl(long j2, int i2);

    CSSStyleSheetImpl(long peer) {
        super(peer);
    }

    static CSSStyleSheet getImpl(long peer) {
        return (CSSStyleSheet) create(peer);
    }

    @Override // org.w3c.dom.css.CSSStyleSheet
    public CSSRule getOwnerRule() {
        return CSSRuleImpl.getImpl(getOwnerRuleImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.CSSStyleSheet
    public CSSRuleList getCssRules() {
        return CSSRuleListImpl.getImpl(getCssRulesImpl(getPeer()));
    }

    public CSSRuleList getRules() {
        return CSSRuleListImpl.getImpl(getRulesImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.CSSStyleSheet
    public int insertRule(String rule, int index) throws DOMException {
        return insertRuleImpl(getPeer(), rule, index);
    }

    @Override // org.w3c.dom.css.CSSStyleSheet
    public void deleteRule(int index) throws DOMException {
        deleteRuleImpl(getPeer(), index);
    }

    public int addRule(String selector, String style, int index) throws DOMException {
        return addRuleImpl(getPeer(), selector, style, index);
    }

    public void removeRule(int index) throws DOMException {
        removeRuleImpl(getPeer(), index);
    }
}
