package com.sun.webkit.dom;

import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.stylesheets.MediaList;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSImportRuleImpl.class */
public class CSSImportRuleImpl extends CSSRuleImpl implements CSSImportRule {
    static native String getHrefImpl(long j2);

    static native long getMediaImpl(long j2);

    static native long getStyleSheetImpl(long j2);

    CSSImportRuleImpl(long peer) {
        super(peer);
    }

    static CSSImportRule getImpl(long peer) {
        return (CSSImportRule) create(peer);
    }

    @Override // org.w3c.dom.css.CSSImportRule
    public String getHref() {
        return getHrefImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSImportRule
    public MediaList getMedia() {
        return MediaListImpl.getImpl(getMediaImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.CSSImportRule
    public CSSStyleSheet getStyleSheet() {
        return CSSStyleSheetImpl.getImpl(getStyleSheetImpl(getPeer()));
    }
}
