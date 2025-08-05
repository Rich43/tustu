package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.stylesheets.MediaList;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSMediaRuleImpl.class */
public class CSSMediaRuleImpl extends CSSRuleImpl implements CSSMediaRule {
    static native long getMediaImpl(long j2);

    static native long getCssRulesImpl(long j2);

    static native int insertRuleImpl(long j2, String str, int i2);

    static native void deleteRuleImpl(long j2, int i2);

    CSSMediaRuleImpl(long peer) {
        super(peer);
    }

    static CSSMediaRule getImpl(long peer) {
        return (CSSMediaRule) create(peer);
    }

    @Override // org.w3c.dom.css.CSSMediaRule
    public MediaList getMedia() {
        return MediaListImpl.getImpl(getMediaImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.CSSMediaRule
    public CSSRuleList getCssRules() {
        return CSSRuleListImpl.getImpl(getCssRulesImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.CSSMediaRule
    public int insertRule(String rule, int index) throws DOMException {
        return insertRuleImpl(getPeer(), rule, index);
    }

    @Override // org.w3c.dom.css.CSSMediaRule
    public void deleteRule(int index) throws DOMException {
        deleteRuleImpl(getPeer(), index);
    }
}
